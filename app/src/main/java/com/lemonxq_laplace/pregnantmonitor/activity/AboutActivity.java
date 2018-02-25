package com.lemonxq_laplace.pregnantmonitor.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.Util.Util;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/24
 */

public class AboutActivity extends BaseActivity {
    private ImageView back;
    private Button versionBtn;
    private Button checkUpdateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();
        setListeners();
    }
    void initView(){
        back = findViewById(R.id.back);
        versionBtn = findViewById(R.id.about_version);
        checkUpdateBtn = findViewById(R.id.about_checkUpdate);
    }

    void setListeners(){
        // 设置返回键监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 检查更新
        checkUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.makeToast(AboutActivity.this,"当前已是最新版本");
            }
        });
    }
}
