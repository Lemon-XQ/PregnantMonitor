package com.lemonxq_laplace.pregnantmonitor.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.lemonxq_laplace.pregnantmonitor.R;

/**
 * @author Lemon-XQ
 */

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    // TODO 各个preference项的点击事件响应及保存

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState){
//        View view = inflater.inflate(R.layout.fragment_setting,container,false);
//        View view = inflater.inflate(R.layout.fragment_setting,container,false);
//        back = view.findViewById(R.id.back);
//        back.setOnClickListener(this);
//        return view;
//    }


}
