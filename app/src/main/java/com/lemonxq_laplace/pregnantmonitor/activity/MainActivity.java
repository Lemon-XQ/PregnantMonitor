package com.lemonxq_laplace.pregnantmonitor.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lemonxq_laplace.pregnantmonitor.Data.Record;
import com.lemonxq_laplace.pregnantmonitor.Data.User;
import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.ToolBar;
import com.lemonxq_laplace.pregnantmonitor.Util.ACache;
import com.lemonxq_laplace.pregnantmonitor.Util.ActivityController;
import com.lemonxq_laplace.pregnantmonitor.Util.CommonRequest;
import com.lemonxq_laplace.pregnantmonitor.Util.CommonResponse;
import com.lemonxq_laplace.pregnantmonitor.Util.Consts;
import com.lemonxq_laplace.pregnantmonitor.Util.Database;
import com.lemonxq_laplace.pregnantmonitor.Util.HttpUtil;
import com.lemonxq_laplace.pregnantmonitor.Util.PhotoUtil;
import com.lemonxq_laplace.pregnantmonitor.Util.Server;
import com.lemonxq_laplace.pregnantmonitor.Util.SharedPreferencesUtil;
import com.lemonxq_laplace.pregnantmonitor.Util.UserManager;
import com.lemonxq_laplace.pregnantmonitor.Util.Util;
import com.lemonxq_laplace.pregnantmonitor.fragment.DateDialogFragment;
import com.lemonxq_laplace.pregnantmonitor.fragment.MonitorFragment;
import com.lemonxq_laplace.pregnantmonitor.fragment.ToolFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LemonXQ
 * @version 1.0
 * @time 2017/11/5
 * @updateAuthor
 */
public class MainActivity extends BaseActivity implements DialogInterface.OnDismissListener{

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private ViewPager mMViewPager;
    private LinearLayout mMLinearLayout;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private CircleImageView avatarImage;
    private TextView nickNameText;
    private ToolBar mMToolBar;
    private Toolbar mTitleBar;
    private MonitorFragment monitorFragment = new MonitorFragment();
    final DateDialogFragment dateDialog = new DateDialogFragment();
    private PhotoUtil photoUtil = new PhotoUtil();
    private Server server = new Server(this);
    private String[] bottomTitleArr = new String[2];
    private int[] bottomPic = {R.drawable.icon_message,R.drawable.icon_selfinfo};
    private ACache aCache;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    avatarImage.setImageBitmap((Bitmap)msg.obj);
                    break;
                case 2:
                    nickNameText.setText((String)msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListeners();
        initData();

        // 是否弹出初始日期设置框（出生年月及怀孕日期）
        showDateDialog();
    }

    void initView(){
        mMViewPager = (ViewPager) findViewById(R.id.viewpagge);
        mMLinearLayout = (LinearLayout) findViewById(R.id.llbootom_container);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mTitleBar = (Toolbar) findViewById(R.id.titlebar);
        nickNameText = mNavView.getHeaderView(0).findViewById(R.id.nav_nickname);
        avatarImage = mNavView.getHeaderView(0).findViewById(R.id.nav_avatar);
        aCache = ACache.get(MainActivity.this);

        //数据源添加Fragment
        mFragments.add(monitorFragment);
        mFragments.add(new ToolFragment());

        // 顶部导航条设置
        setSupportActionBar(mTitleBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 显示导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu2);// 设置导航按钮图标
        }
    }

    void setListeners(){
        // 底部导航栏设置
        bottomTitleArr[0] = getResources().getString(R.string.Monitor);
        bottomTitleArr[1] = getResources().getString(R.string.Tool);
        mMViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        mMToolBar = new ToolBar();
        // 给底部tab添加新的控件
        mMToolBar.addBottomTab(mMLinearLayout, bottomTitleArr, bottomPic);
        mMToolBar.changeColor(0);// 设置默认第一个tab颜色为选中状态
        // viewpager监听器
        mMViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 某项ViewPage选中的时候调用。在这里顺便改变底部Tab的颜色
                mMToolBar.changeColor(position);
                // 同时修改对应fragment的标题
                mTitleBar.setTitle(bottomTitleArr[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 接口回调的方式，点击底部Tab，切换不同的页面
        mMToolBar.setOnToolBarChangeListener(new ToolBar.OnToolBarChangeListener() {
            @Override
            public void onToolBarChange(int position) {
                // 切换对应的fragment
                mMViewPager.setCurrentItem(position);
            }
        });

        // 左拉菜单栏设置
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //处理任意菜单项点击
                switch (item.getItemId()) {
                    case R.id.nav_center:// 个人中心
                        autoStartActivity(CenterActivity.class);
                        break;

                    case R.id.nav_task:// 每日目标
                        autoStartActivity(SetPlanActivity.class);
                        break;

                    case R.id.nav_setting:// 设置
                        autoStartActivity(SettingActivity.class);
                        break;

                    case R.id.nav_info:// 关于
                        autoStartActivity(AboutActivity.class);
                        break;

                    case R.id.nav_logout:// 注销
                        SharedPreferencesUtil spu = new SharedPreferencesUtil(MainActivity.this);
                        spu.setParam("isAutoLogin",false);
                        ActivityController.finishAll(LogInActivity.class);
                        ActivityController.clearAcache();
                        UserManager.clear();
                        break;

                    case R.id.nav_exit:// 退出
                        ActivityController.finishAll();
                        break;

                    default:
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        // 监听按下头像事件
        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoDialog();
            }
        });

        // 头像工具类回调
        photoUtil.setOnPhotoResultListener(new PhotoUtil.OnPhotoResultListener() {
            // 当选择图片或者拍摄图片拿到结果之后
            @Override
            public void onPhotoResult(final Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    final Bitmap bitmap = PhotoUtil.decodeUriAsBitmap(MainActivity.this,uri);
                    // 上传头像
                    if(UserManager.getCurrentUser().isVisitor()){// 游客不进行服务器端头像存储
                        avatarImage.setImageBitmap(bitmap);
                        return;
                    }
                    HttpUtil.uploadImage(Consts.URL_UploadImg,bitmap,new okhttp3.Callback(){
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            CommonResponse res = new CommonResponse(response.body().string());
                            String resCode = res.getResCode();
                            String resMsg = res.getResMsg();
                            // 上传成功
                            if(resCode.equals(Consts.SUCCESSCODE_UPLOADIMG)){
                                saveAvatar(bitmap);
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
        // 初始化用户记录
        User user = UserManager.getCurrentUser();
        List<Record> recordList = Database.findRecords(user);
        // 本地数据库中无记录，则在服务器端加载
        if(user.isVisitor()){
            if(!recordList.isEmpty()){
                user.getRecordList().addAll(recordList);
                user.save();
            }
        }else{
            if(!recordList.isEmpty() || !(recordList = server.getRecords()).isEmpty()){
                user.getRecordList().addAll(recordList);
                user.save();
            }
        }

        // 初始化昵称
        String nickname = user.getNickname();
        if(user.isVisitor()){
            if(nickname!=null) {
                user.setNickname(nickname);
                user.save();
            }else{
                nickname = "Visitor";
            }
        }else{
            if(nickname != null || !(nickname = server.getNickname()).equals("null")){
                Log.e("INIT",nickname);
                user.setNickname(nickname);
                user.save();
            }else {
                nickname = "UserName";
            }
        }
        nickNameText.setText(nickname);

        // 加载头像顺序：缓存->本地数据库->服务器
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
//                showResponse("数据库加载头像");
                bitmap = BitmapFactory.decodeByteArray(user.getAvatarImage(),0,
                        user.getAvatarImage().length);
                avatarImage.setImageBitmap(bitmap);
            }
        }else {
//            showResponse("缓存加载头像");
            avatarImage.setImageBitmap(bitmap);
        }
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
//            showResponse("头像："+imgStr);
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
//            showResponse("使用默认头像");
            // 使用默认头像
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    avatarImage.setImageResource(R.drawable.ic_launcher);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 导航按钮
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            // 其他ToolBar按钮
            default:
                break;
        }
        return true;
    }

    private void showDateDialog(){
        User user = UserManager.getCurrentUser();
        if(user.isVisitor()){
            if(Database.findBirthDateByUsername(UserManager.getCurrentUser().getAccount()) == null){
                // 本地无日期信息，则弹日期填写框
                dateDialog.setCancelable(false);
                dateDialog.show(getFragmentManager(),"date");
            }
        }else{
            if(Database.findBirthDateByUsername(UserManager.getCurrentUser().getAccount()) == null){
                Date birthDate, pregnantDate;
                if((birthDate = server.getBirthDate()) != null ){
                    // 同步服务端信息至本地数据库
                    user.setBirthDate(birthDate);
                    if((pregnantDate = server.getPregnantDate())!=null)
                        user.setPregnantDate(pregnantDate);
                    user.save();
                }else {
                    // 本地和服务端均无日期信息，则弹日期填写框
                    dateDialog.setCancelable(false);
                    dateDialog.show(getFragmentManager(),"date");
                }
            }
        }
    }

    void showPhotoDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.uploadAvatar));
        final String[] choices = {getResources().getString(R.string.takePhoto), getResources().getString(R.string.chooseFromGallery)};
        // 设置一个下拉的列表选择项
        builder.setItems(choices, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch(which)
                {
                    case 0:// 相机
                        photoUtil.takePicture(MainActivity.this);
                        break;
                    case 1:// 本地相册
                        photoUtil.selectPicture(MainActivity.this);
                        break;
                }
            }
        });
        builder.show();
    }

    private void showResponse(final String msg) {
        Log.e("MainActivity",msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finishInitDate(dateDialog.pregnantDate);
    }

    void finishInitDate(Date pregnantDate){
        if(pregnantDate != null){
            // 更新UI界面
            long days = Util.getPregnantDays(pregnantDate);
            long months = Util.getPregnantMonths(pregnantDate);
            long weeks = Util.getPregnantWeeks(pregnantDate);
            monitorFragment.updateShow(days,months,weeks);
        }
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    //存放上次点击“返回键”的时刻
    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差,大于2000ms是误操作
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, getResources().getString(R.string.exit_hint), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 记录本次点击“返回键”的时刻
            } else {
                //小于2000ms退出程序
                ActivityController.finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

}
