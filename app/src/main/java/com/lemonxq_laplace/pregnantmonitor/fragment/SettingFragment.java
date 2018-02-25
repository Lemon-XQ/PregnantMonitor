package com.lemonxq_laplace.pregnantmonitor.fragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.Util.ActivityController;
import com.lemonxq_laplace.pregnantmonitor.Util.SharedPreferencesUtil;
import com.lemonxq_laplace.pregnantmonitor.Util.Util;

/**
 * @author Lemon-XQ
 */

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceManager().setSharedPreferencesName("mysetting");
    }

    // 各个preference项的点击事件响应及保存
    @Override
     public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
             Preference preference) {
        switch (preference.getKey()){
            case "messageNotify"://消息通知
                CheckBoxPreference messageNotify = (CheckBoxPreference)findPreference("messageNotify");
                Boolean checked = messageNotify.isChecked();
                if(checked){

                }else{

                }
                break;

            case "dailyPush"://消息推送
                break;

            case "clearCache"://清空缓存
                ActivityController.clearAcache();
                SharedPreferencesUtil spu = new SharedPreferencesUtil(getActivity());
                spu.clear();
                Util.makeToast(getActivity(),"清除缓存成功");
                break;

            default:
                break;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


}
