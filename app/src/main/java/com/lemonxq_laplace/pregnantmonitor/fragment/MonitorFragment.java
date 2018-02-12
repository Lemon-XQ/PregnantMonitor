package com.lemonxq_laplace.pregnantmonitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.Util.UserManager;
import com.lemonxq_laplace.pregnantmonitor.Util.Util;
import com.lemonxq_laplace.pregnantmonitor.activity.MonitorHistoryActivity;

import java.util.Date;

import am.widget.circleprogressbar.CircleProgressBar;

/**
 * @author: Lemon-XQ
 * @date: 2018/1/24
 * @description: 实时监测界面
 */
public class MonitorFragment extends Fragment {

    private CircleProgressBar dayProgressBar;// 环形进度条，显示怀孕天数
    private TextView weekText;
    private TextView monthText;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_monitor, null);
        initView();
        setListeners();
        return mView;
    }

    void initView(){
        dayProgressBar = mView.findViewById(R.id.circleProgressBar);
        weekText = mView.findViewById(R.id.monitor_week);
        monthText = mView.findViewById(R.id.monitor_month);

        Date pregnantDate = UserManager.getCurrentUser().getPregnantDate();
        if(pregnantDate != null){
            // 更新UI界面
            long days = Util.getPregnantDays(pregnantDate);
            long months = Util.getPregnantMonths(pregnantDate);
            long weeks = Util.getPregnantWeeks(pregnantDate);
            updateShow(days,months,weeks);
        }
    }

    void setListeners(){
        dayProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MonitorHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    public void updateShow(long days, long months, long weeks){
        dayProgressBar.animationToProgress((int)days);
        weekText.setText(String.valueOf((int)weeks));
        monthText.setText(String.valueOf((int)months));
    }
}
