package com.lemonxq_laplace.pregnantmonitor.fragment;

import android.content.res.Resources;
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
    private TextView msgText;
    private TextView resultText;
    private float GDM_Prob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gdmresult_en, container, false);
        GDM_Prob = this.getArguments().getFloat("GDMProb");
        Init();
        return view;
    }

    private void Init(){
        resultText = view.findViewById(R.id.GDMResult);
        dountChartView = view.findViewById(R.id.dountChart);
        msgText = view.findViewById(R.id.AnalyseRes);
        Resources resources = this.getActivity().getApplicationContext().getResources();

        // 计算、设置环形图占比，更改结果显示
        float GDM_per = GDM_Prob * 100;
        dountChartView.setPercentage(GDM_per,100-GDM_per);

        // 患病（大于等于0.5）
        if(Float.compare(GDM_Prob, 0.5f) >= 0){
            // 设置检测结果文本、字体颜色
            msgText.setText(resources.getString(R.string.GDM_msg_en));
            msgText.setTextColor(resources.getColor(R.color.red));
            // 设置结果方框样式[文本、红色字体，红色框]
            resultText.setTextColor(resources.getColor(R.color.red));
            resultText.setText(resources.getString(R.string.GDM_en));
            Drawable drawable = resources.getDrawable(R.drawable.redoutlinerect);
            resultText.setBackground(drawable);
        }
        // 不患病
        else {
            // 设置检测结果文本、字体颜色
            msgText.setText(resources.getString(R.string.normal_msg_en));
            msgText.setTextColor(resources.getColor(R.color.green));
            // 设置结果方框样式[文本、绿色字体，绿色框]
            resultText.setTextColor(resources.getColor(R.color.green));
            resultText.setText(resources.getString(R.string.normal_en));
            Drawable drawable = resources.getDrawable(R.drawable.greenoutlinerect);
            resultText.setBackground(drawable);
        }
    }
}
