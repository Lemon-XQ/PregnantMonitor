package com.lemonxq_laplace.pregnantmonitor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lemonxq_laplace.pregnantmonitor.Data.User;
import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.Util.ACache;
import com.lemonxq_laplace.pregnantmonitor.Util.ActivityController;
import com.lemonxq_laplace.pregnantmonitor.Util.CommonRequest;
import com.lemonxq_laplace.pregnantmonitor.Util.CommonResponse;
import com.lemonxq_laplace.pregnantmonitor.Util.Consts;
import com.lemonxq_laplace.pregnantmonitor.Util.HttpUtil;
import com.lemonxq_laplace.pregnantmonitor.Util.PhotoUtil;
import com.lemonxq_laplace.pregnantmonitor.Util.Server;
import com.lemonxq_laplace.pregnantmonitor.Util.SharedPreferencesUtil;
import com.lemonxq_laplace.pregnantmonitor.Util.UserManager;
import com.lemonxq_laplace.pregnantmonitor.Util.Util;
import com.lemonxq_laplace.pregnantmonitor.view.WheelView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/12
 */

public class CenterActivity extends BaseActivity {

    private ImageView back;
    private CircleImageView avatarImage;
    private TextView accountText;
    private Button nicknameBtn;
    private Button pwdBtn;
    private Button birthBtn;
    private Button heightBtn;
    private Button historyBtn;
    private Button exitBtn;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;

    private ArrayList<String> heightList = new ArrayList<String>();
    private PhotoUtil photoUtil = new PhotoUtil();
    private ACache aCache;
    private Handler handler;
    private Server server = new Server(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        initView();
        setListeners();
        initData();
    }

    void initView(){
        aCache = ACache.get(CenterActivity.this);
        MainActivity activity = (MainActivity) ActivityController.getActivity(MainActivity.class);
        handler = activity.handler;

        avatarImage = findViewById(R.id.center_avatar);
        accountText = findViewById(R.id.center_account);
        nicknameBtn = findViewById(R.id.center_nickname_btn);
        pwdBtn = findViewById(R.id.center_pwd_btn);
        birthBtn = findViewById(R.id.center_birth_btn);
        heightBtn = findViewById(R.id.center_height_btn);
        historyBtn = findViewById(R.id.center_history_btn);
        exitBtn = findViewById(R.id.center_exit_btn);
        toolbar = findViewById(R.id.titlebar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.arrow_back_white);
        }
        collapsingToolbar.setCollapsedTitleGravity(Gravity.START);
    }

    void setListeners(){
        // 更换头像
        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoDialog();
            }
        });

        // 修改昵称
        nicknameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View nicknameView = LayoutInflater.from(CenterActivity.this).inflate(R.layout.dialog_edittext,null);
                final EditText nicknameEdit = nicknameView.findViewById(R.id.edit);
                // 弹昵称修改框
                AlertDialog.Builder builder = new AlertDialog.Builder(CenterActivity.this);
                builder.setView(nicknameView);
                builder.setTitle(getResources().getString(R.string.Nickname));
                builder.setNegativeButton(getResources().getString(R.string.cancel),null);
                builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 保存昵称
                        String nickname = nicknameEdit.getText().toString();
                        User user = UserManager.getCurrentUser();
                        user.setNickname(nickname);
                        user.save();
                        if(!user.isVisitor())
                            server.setNickname(nickname);
                        // 更改显示
                        collapsingToolbar.setTitle(nickname);
                        Util.makeToast(CenterActivity.this,getResources().getString(R.string.modify_success));
                        // 通知MainActivity更新昵称
                        Message message = new Message();
                        message.what = 2;
                        message.obj = nickname;
                        handler.sendMessage(message);
                    }
                });
                builder.show();
            }
        });

        // 修改密码
        pwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = UserManager.getCurrentUser();
                if(!user.isVisitor()){
                    autoStartActivity(ModifyPwdActivity.class);
                }else{
                    showResponse("游客不开放此功能");
                }

            }
        });

        // 修改生日
        birthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                View dateView = LayoutInflater.from(CenterActivity.this).inflate(R.layout.dialog_birthdate,null);
                final DatePicker datePicker = dateView.findViewById(R.id.datePicker);
                datePicker.setMaxDate(date.getTime());// 生日最大只能为今天
                // 弹生日修改框
                AlertDialog.Builder builder = new AlertDialog.Builder(CenterActivity.this);
                builder.setView(dateView);
                builder.setTitle(getResources().getString(R.string.Birth));
                builder.setNegativeButton(getResources().getString(R.string.cancel),null);
                builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 保存生日
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth()+1;
                        int day = datePicker.getDayOfMonth();
                        Date birthDate = Util.formDate(year,month,day);
                        User user = UserManager.getCurrentUser();
                        user.setBirthDate(birthDate);
                        user.save();
                        if(!user.isVisitor())
                            server.setBirthDate(birthDate);

                        // 更改显示
                        String dateStr = year+"-"+month+"-"+day;
                        birthBtn.setText(dateStr);
                        Util.makeToast(CenterActivity.this,getResources().getString(R.string.modify_success));
                    }
                });
                builder.show();
            }
        });

        // 修改身高
        heightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View heightView = LayoutInflater.from(CenterActivity.this).inflate(R.layout.dialog_wheelview, null);
                final WheelView wv = heightView.findViewById(R.id.wheel_view);
                wv.setItems(heightList);
                wv.setSeletion(3);

                // 弹身高修改框
                AlertDialog.Builder builder = new AlertDialog.Builder(CenterActivity.this);
                builder.setView(heightView);
                builder.setTitle(getResources().getString(R.string.Height));
                builder.setNegativeButton(getResources().getString(R.string.cancel),null);
                builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 保存身高
                        String heightStr = wv.getSeletedItem();
                        float height = Float.parseFloat(heightStr)/100.0f;
                        User user = UserManager.getCurrentUser();
                        user.setHeight(height);
                        user.save();
                        if(!user.isVisitor())
                            server.setHeight(height+"");

                        // 更改显示
                        heightBtn.setText(heightStr);
                        Util.makeToast(CenterActivity.this,getResources().getString(R.string.modify_success));
                    }
                });
                builder.show();
            }
        });

        // 查看历史检测记录
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoStartActivity(MonitorHistoryActivity.class);
            }
        });

        // 退出登录
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesUtil spu = new SharedPreferencesUtil(CenterActivity.this);
                spu.setParam("isAutoLogin",false);
                ActivityController.finishAll(LogInActivity.class);
                ActivityController.clearAcache();
                UserManager.clear();
            }
        });

        // 头像工具类回调
        photoUtil.setOnPhotoResultListener(new PhotoUtil.OnPhotoResultListener() {
            // 当选择图片或者拍摄图片拿到结果之后
            @Override
            public void onPhotoResult(final Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    final Bitmap bitmap = PhotoUtil.decodeUriAsBitmap(CenterActivity.this,uri);
                    if(UserManager.getCurrentUser().isVisitor()){// 游客不进行服务器端头像存储
                        avatarImage.setImageBitmap(bitmap);
                        return;
                    }
                    // 上传头像
                    HttpUtil.uploadImage(Consts.URL_UploadImg,bitmap,new okhttp3.Callback(){
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            CommonResponse res = new CommonResponse(response.body().string());
                            String resCode = res.getResCode();
                            String resMsg = res.getResMsg();
                            // 上传成功
                            if(resCode.equals(Consts.SUCCESSCODE_UPLOADIMG)){
                                saveAvatar(bitmap);
                                // 通知MainActivity更新头像
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
                            showResponse(resMsg);
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            showResponse("Network ERROR!");
                        }
                    });
                }
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }

    void initData(){
        // 初始化身高列表
        for (int i = 150; i <= 180; i++){
            heightList.add(i+""); // height: 150-180
        }

        // 初始化昵称
        User user = UserManager.getCurrentUser();
        if(user.getNickname() != null)
            collapsingToolbar.setTitle(user.getNickname());
        else
            collapsingToolbar.setTitle(getResources().getString(R.string.Center));
        // 初始化用户名
        accountText.setText(user.getAccount());
        // 初始化生日
        String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(user.getBirthDate());
        birthBtn.setText(dateStr);
        // 初始化身高
        Float height = user.getHeight();
        if(height==0 && !user.isVisitor()){
            height = Float.parseFloat(server.getHeight());
        }
        if(height != 0){
            heightBtn.setText(String.valueOf(height*100));
            user.setHeight(height);
            user.save();
        }

        // 初始化头像：缓存->本地数据库->服务器
        Bitmap bitmap;
        if((bitmap = aCache.getAsBitmap("avatar")) == null){// cache加载失败
            // 数据库加载失败则从服务器加载（仅非游客有效）
            if(user.isVisitor()){
                if(user.getAvatarImage() != null){
                    // 数据库加载
                    bitmap = BitmapFactory.decodeByteArray(user.getAvatarImage(),0,
                            user.getAvatarImage().length);
                    avatarImage.setImageBitmap(bitmap);
                }else{
                    avatarImage.setImageResource(R.drawable.ic_launcher);// 默认头像
                }
                return;
            }
            // 非游客
            if(user.getAvatarImage() == null){
                CommonRequest request = new CommonRequest();
                request.addRequestParam("account",user.getAccount());
                HttpUtil.sendPost(Consts.URL_DownloadImg,request.getJsonStr(),new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        CommonResponse res = new CommonResponse(response.body().string());
                        String resCode = res.getResCode();
                        String resMsg = res.getResMsg();
                        HashMap<String,String> property = res.getPropertyMap();
                        String imgStr = property.get("avatar");
                        loadAvatar(resCode,imgStr);
                    }
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        showResponse("Network ERROR");
                    }
                });
            }else{// 数据库加载
                bitmap = BitmapFactory.decodeByteArray(user.getAvatarImage(),0,
                        user.getAvatarImage().length);
                avatarImage.setImageBitmap(bitmap);
            }
        }else {
            avatarImage.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PhotoUtil.INTENT_CROP:
            case PhotoUtil.INTENT_TAKE:
            case PhotoUtil.INTENT_SELECT:
                photoUtil.onActivityResult(this, requestCode, resultCode, data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void showPhotoDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CenterActivity.this);
        builder.setTitle("上传头像");
        final String[] choices = {"拍照", "从相册选择"};
        // 设置一个下拉的列表选择项
        builder.setItems(choices, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch(which)
                {
                    case 0:// 相机
                        photoUtil.takePicture(CenterActivity.this);
                        break;
                    case 1:// 本地相册
                        photoUtil.selectPicture(CenterActivity.this);
                        break;
                }
            }
        });
        builder.show();
    }

    private void saveAvatar(final Bitmap bitmap){
        // 设置头像
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                avatarImage.setImageBitmap(bitmap);
            }
        });
        // 保存头像到本地数据库
        User user = UserManager.getCurrentUser();
        user.setAvatarImage(PhotoUtil.bitmap2Bytes(bitmap));
        user.save();
        // 写入缓存
        aCache.put("avatar",bitmap);
    }

    private void loadAvatar(String resCode,String imgStr){
        if (resCode.equals(Consts.SUCCESSCODE_DOWNLOADIMG) && !imgStr.equals("null") && !imgStr.equals("")) {
            // 获取头像成功
            byte[] bytes = Base64.decode(imgStr, Base64.DEFAULT);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    avatarImage.setImageBitmap(bitmap);
                }
            });
        }else{
            // 使用默认头像
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    avatarImage.setImageResource(R.drawable.ic_launcher);
                }
            });
        }
    }

    private void showResponse(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CenterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
