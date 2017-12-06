package com.lemonxq_laplace.pregnantmonitor.fragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.view.DountChartView;

/**
 * @author: Lemon-XQ
 * @date: 2017/12/4
 */

public class GDMResultFragment extends Fragment {

    private View view;
    private DountChartView dountChartView;
    private TextView resultText;
    private float GDM_Prob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gdmresult, container, false);
        GDM_Prob = this.getArguments().getFloat("GDMProb");
        Init();
        return view;
    }

    private void Init(){
        resultText = view.findViewById(R.id.GDMResult);
        dountChartView = view.findViewById(R.id.dountChart);
        Resources resources = this.getActivity().getApplicationContext().getResources();

        // 计算、设置环形图占比，更改结果显示
        float GDM_per = GDM_Prob * 100;
//      dountChartView.setPercentage(GDM_per,100-GDM_per);

        // 患病
        if(Float.compare(GDM_Prob, 1.0f) == 0){
            resultText.setTextColor(Color.RED);
            resultText.setText("妊娠期糖尿病");
            Drawable drawable = resources.getDrawable(R.drawable.redoutlinerect);
            resultText.setBackground(drawable);
            dountChartView.setPercentage(75,25);
        }
        // 不患病
        else if(Float.compare(GDM_Prob, -1.0f) == 0){
            resultText.setTextColor(Color.GREEN);
            resultText.setText("正常");
            Drawable drawable = resources.getDrawable(R.drawable.greenoutlinerect);
            resultText.setBackground(drawable);
            dountChartView.setPercentage(25,75);
        }
    }


}
