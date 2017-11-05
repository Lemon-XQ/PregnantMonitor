package com.lemonxq_laplace.pregnantmonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * @author: Lemon-XQ
 * @date: 2017/11/5
 */

public class AnalyzeActivity extends AppCompatActivity {

    private ImageView back;
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
}
