package com.lemonxq_laplace.pregnantmonitor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lemonxq_laplace.pregnantmonitor.Util.Consts;

/**
 * @author: Lemon-XQ
 * @date: 2017/11/5
 */

public class AnalyzeFragment extends Fragment {

    private Button analyseBtn;
    private EditText ageText;
    private EditText heightText;
    private EditText weightText;
    private EditText ogttText;
    private View view;
    private int age;
    private float height;
    private float weight;
    private float ogtt;

    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AnalyzeActivity activity=(AnalyzeActivity) getActivity();
        handler=activity.handler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_analyze,container,false);
        InitComponent();
        SetListeners();
        Log.e("Analyse","create view");
        return view;
    }

    private void InitComponent(){
        Log.d("Init", "InitComponent");
        analyseBtn = view.findViewById(R.id.btn_analyse);
        ageText = view.findViewById(R.id.age);
        weightText = view.findViewById(R.id.weight);
        heightText = view.findViewById(R.id.height);
        ogttText = view.findViewById(R.id.ogtt);
    }

    private void SetListeners(){
        // 设置分析按钮监听
        analyseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 发送指令给Activity
                Log.d("Analyse","send msg begin");
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("age",age);
                bundle.putFloat("height",height);
                bundle.putFloat("weight",weight);
                bundle.putFloat("OGTT",ogtt);
                msg.what = 1;
                msg.setData(bundle);
                handler.sendMessage(msg);
                Log.d("Analyse","send msg finish");
            }
        });

        // 输入框文本内容监听
        ageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    age = Integer.parseInt(ageText.getText().toString());
                }catch(NumberFormatException e){
                    Toast.makeText(getActivity(), Consts.AGE_INVALID,Toast.LENGTH_SHORT).show();
                    age = 0;
                }

            }
        });
        weightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    weight = Float.parseFloat(weightText.getText().toString());
                }catch(NumberFormatException e){
                    Toast.makeText(getActivity(), Consts.WEIGHT_INVALID,Toast.LENGTH_SHORT).show();
                    weight = 0;
                }

            }
        });
        heightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    height = Float.parseFloat(heightText.getText().toString());
                }catch(NumberFormatException e){
                    Toast.makeText(getActivity(), Consts.HEIGHT_INVALID,Toast.LENGTH_SHORT).show();
                    height = 0;
                }

            }
        });
        ogttText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    ogtt = Float.parseFloat(ogttText.getText().toString());
                }catch(NumberFormatException e){
                    Toast.makeText(getActivity(), Consts.OGTT_INVALID,Toast.LENGTH_SHORT).show();
                    ogtt = 0;
                }

            }
        });
    }

}
