package com.lemonxq_laplace.pregnantmonitor.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.fragment.TaskFragment;

/**
 * @author Lemon-XQ
 */

public class TaskActivity extends AppCompatActivity {

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // 动态添加碎片
        getSupportFragmentManager().beginTransaction().replace(R.id.taskContainer,
                new TaskFragment()).commit();

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
