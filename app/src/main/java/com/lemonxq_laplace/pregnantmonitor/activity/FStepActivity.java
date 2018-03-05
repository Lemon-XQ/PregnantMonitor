package com.lemonxq_laplace.pregnantmonitor.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.step.UpdateUiCallBack;
import com.lemonxq_laplace.pregnantmonitor.step.service.StepService;
import com.lemonxq_laplace.pregnantmonitor.step.utils.SharedPreferencesUtils;
import com.lemonxq_laplace.pregnantmonitor.view.StepArcView;

/**
 * 计步主页
 */
public class FStepActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_data;
    private StepArcView cc;
    private TextView tv_set;
    private TextView tv_isSupport;
    private SharedPreferencesUtils sp;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footcounter);
        initView();
        initData();
        setListeners();
    }
    
    private void initView() {
        tv_data = (TextView) findViewById(R.id.tv_data);
        cc = (StepArcView) findViewById(R.id.cc);
        tv_set = (TextView) findViewById(R.id.tv_set);
        tv_isSupport = (TextView) findViewById(R.id.tv_isSupport);
        back = findViewById(R.id.back);
    }

    private void setListeners() {
        tv_set.setOnClickListener(this);
        tv_data.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        sp = new SharedPreferencesUtils(this);
        //获取用户设置的计划锻炼步数，没有设置过的话默认3000
        String planSteps = (String) sp.getParam("planSteps", "3000");
        //设置当前步数为0
        cc.setCurrentCount(Integer.parseInt(planSteps), 0);
        tv_isSupport.setText(getResources().getString(R.string.stepHint));
        startService();
    }

    private boolean isBind = false;

    /**
     * 开启计步服务
     */
    private void startService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起与Service的连接时调用，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planSteps = (String) sp.getParam("planSteps", "3000");
            cc.setCurrentCount(Integer.parseInt(planSteps), stepService.getStepCount());

            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planSteps = (String) sp.getParam("planSteps", "3000");
                    cc.setCurrentCount(Integer.parseInt(planSteps), stepCount);
                }
            });
        }

        /**
         * 当与Service之间的连接丢失时调用，常发生在Service所在的进程崩溃或者被Kill的时候，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_set:
                startActivity(new Intent(this, SetPlanActivity.class));
                break;
            case R.id.tv_data:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(conn);
        }
    }
}
