package com.lemonxq_laplace.pregnantmonitor.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lemonxq_laplace.pregnantmonitor.Util.ActivityController;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/9
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }


}
