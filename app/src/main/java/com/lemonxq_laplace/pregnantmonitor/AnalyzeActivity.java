package com.lemonxq_laplace.pregnantmonitor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lemonxq_laplace.pregnantmonitor.Util.Consts;
import com.lemonxq_laplace.pregnantmonitor.Util.HttpUtil;

import java.io.IOException;
import java.util.LinkedHashMap;

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
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg!=null){
                switch (msg.what) {
                    case 1:
                        Bundle bundle = msg.getData();
                        age = bundle.getInt("age");
                        weight = bundle.getFloat("weight");
                        height = bundle.getFloat("height");
                        ogtt = bundle.getFloat("OGTT");
                        Log.d("Analyse","age:"+age+"height:"+height+"weight:"+weight+"ogtt:"+ogtt);
                        AnalyseDIAB(age,height,weight,ogtt);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.analyzeContainer,
                new AnalyzeFragment()).commit();

        // 设置返回键监听
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void AnalyseDIAB(int age,float height,float weight,float ogtt){
        // 校验参数
        if(checkDataValid(age,height,weight,ogtt)){
            // 构造POST参数列表
            LinkedHashMap<String,String> params=new LinkedHashMap<>();
            // 填充参数
            params.put("age",age+"");
            params.put("height",height+"");
            params.put("weight",weight+"");
            params.put("OGTT",ogtt+"");
            System.out.println("age:"+age+" height:"+height+" weight:"+weight+" ogtt:"+ogtt);

            HttpUtil.sendPost(Consts.URL_Analyse,params,new okhttp3.Callback(){
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String resCode = response.body().string();
                    String resMsg = "";
                    System.out.println("resCode:"+resCode);

                    if(resCode.equals(Consts.ERRORCODE_NULL))
                        resMsg = "年龄、身高、体重、OGTT"+Consts.ERRORMSG_NULL;
                    else if(resCode.equals(Consts.SUCCESSCODE_DIAB))
                        resMsg = Consts.SUCCESSMSG_DIAB;
                    else if(resCode.equals(Consts.SUCCESSCODE_NOTDIAB))
                        resMsg = Consts.SUCCESSMSG_NOTDIAB;

                    showResponse(resMsg);
                }

                @Override
                public void onFailure(Call call, IOException e){
                    e.printStackTrace();
                }
            });
        }else{
            Toast.makeText(AnalyzeActivity.this,Consts.ERROR_FORMAT,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkDataValid(int age,float height,float weight,float ogtt){
        if(age <= 0 || age >= 150)
            return false;
        if(height <= 0 || height >= 3)
            return false;
        if(weight <= 0 || weight >= 300 )
            return false;
        if(ogtt <= 0)
            return false;
        return true;
    }

    private void showResponse(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AnalyzeActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
