package com.lemonxq_laplace.pregnantmonitor.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.Util.CommonRequest;
import com.lemonxq_laplace.pregnantmonitor.Util.CommonResponse;
import com.lemonxq_laplace.pregnantmonitor.Util.Consts;
import com.lemonxq_laplace.pregnantmonitor.Util.HttpUtil;
import com.lemonxq_laplace.pregnantmonitor.fragment.AnalyzeFragment;
import com.lemonxq_laplace.pregnantmonitor.fragment.GDMResultFragment;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author: Lemon-XQ
 * @date: 2017/11/5
 */

public class AnalyzeActivity extends AppCompatActivity {

    private ImageView back;
    private int age;
    private float height;
    private float weight;
    private float ogtt;


    // 定义一个Handler用于接收黄色碎片给Activity发出来的指令
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        Bundle bundle = msg.getData();
                        age = bundle.getInt("age");
                        weight = bundle.getFloat("weight");
                        height = bundle.getFloat("height");
                        ogtt = bundle.getFloat("OGTT");
                        Log.d("Analyse", "age:" + age + "height:" + height + "weight:" + weight + "ogtt:" + ogtt);
                        if(checkDataValid(age,height,weight,ogtt)){
                            LoadProgressBar(1500);
                        }
                        else {
                            showResponse(getResources().getString(R.string.InvalidInput));
                        }
                        break;

                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        // 动态添加碎片
        final AnalyzeFragment fragment = new AnalyzeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.analyzeContainer,
                fragment).commit();

        // 设置返回键监听
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fragment返回栈不为空则弹出fragment，否则结束activity
                if(getSupportFragmentManager().getBackStackEntryCount() <= 0){
                    finish();
                }else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    private void AnalyseDIAB(int age, float height, float weight, float ogtt) {

        // 创建请求体对象
        CommonRequest request = new CommonRequest();

        // 填充参数
        request.addRequestParam("age",age + "");
        request.addRequestParam("height",height + "");
        request.addRequestParam("weight",weight + "");
        request.addRequestParam("OGTT",ogtt + "");

        // 发送请求
        HttpUtil.sendPost(Consts.URL_Analyse, request.getJsonStr(), new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CommonResponse res = new CommonResponse(response.body().string());
                String resCode = res.getResCode();
                String resMsg = res.getResMsg();
                HashMap<String,String> property = res.getPropertyMap();

                if(resCode.equals(Consts.SUCCESSCODE_GDMANALYSE)){
                    // 分析完成，启动分析结果界面
                    float GDM_Prob = Float.parseFloat(property.get("GDMProb"));
                    Log.d("GDM_Prob",GDM_Prob+"");
                    replaceFragment(new GDMResultFragment(),R.id.analyzeContainer,
                            "GDMProb",GDM_Prob);
                }else{
                    showResponse(resMsg);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean checkDataValid(int age, float height, float weight, float ogtt) {
        if(age == 0 || height == 0 || weight == 0 || ogtt == 0)
            return false;
        return true;
    }

    private void showResponse(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AnalyzeActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 带参数启动Fragment
    private void replaceFragment(Fragment fragment, int layoutID, String tag, float value){
        // 参数传递
        Bundle bundle = new Bundle();
        bundle.putFloat(tag, value);
        fragment.setArguments(bundle);
        // 开始Fragment事务
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(layoutID,fragment);
        transaction.addToBackStack(null);// 添加事务到返回栈中
        transaction.commit();
    }

    void LoadProgressBar(final long time){
        final ProgressDialog progressDialog = new ProgressDialog
                (AnalyzeActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.thinking));
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AnalyseDIAB(age, height, weight, ogtt);
                    }
                });
            }
        }).start();
    }

}
