package com.lemonxq_laplace.pregnantmonitor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lemonxq_laplace.pregnantmonitor.R;

/**
 * @author: Lemon-XQ
 * @date: 2018/1/24
 * @description: 实时监测界面
 */
public class MonitorFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monitor, null);
        return view;
    }

}
