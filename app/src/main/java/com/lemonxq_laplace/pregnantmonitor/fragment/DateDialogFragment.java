package com.lemonxq_laplace.pregnantmonitor.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.lemonxq_laplace.pregnantmonitor.Data.User;
import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.Util.Server;
import com.lemonxq_laplace.pregnantmonitor.Util.SharedPreferencesUtil;
import com.lemonxq_laplace.pregnantmonitor.Util.UserManager;
import com.lemonxq_laplace.pregnantmonitor.Util.Util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/9
 */

public class DateDialogFragment extends DialogFragment{

    public Date birthDate;
    public Date pregnantDate;
    private Button nextBtn;
    private DatePicker datePicker;
    private TextView title;
    private View mView;
    private static int COUNTER = 0;
    private SharedPreferencesUtil spu;
    private Server server;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_date, null);
        init();
        builder.setView(mView);
        return builder.create();
    }

    private void init() {
        server = new Server(getActivity());
        spu = new SharedPreferencesUtil(getActivity());
        title = mView.findViewById(R.id.title);
        nextBtn = mView.findViewById(R.id.btn_next);
        datePicker = mView.findViewById(R.id.datePicker);
        Date date = new Date();
        datePicker.setMaxDate(date.getTime());
        title.setText("请选择您的出生日期");
        nextBtn.setText("下一步");
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(COUNTER == 0){// 下一步
                    COUNTER++;
                    // 记录生日
                    Calendar birthCalendar = Calendar.getInstance();
                    birthCalendar.set(datePicker.getYear(),
                                  datePicker.getMonth(),
                                  datePicker.getDayOfMonth());
                    birthDate = birthCalendar.getTime();
//                    spu.setParam("birthDate",birthDate); // 保存生日

                    title.setText("请选择您的怀孕日期");
                    nextBtn.setText("完成设置");
                    // 设置怀孕日期初始值为今天
                    Date date = new Date();
                    datePicker.init(Util.getYear(date),Util.getMonth(date),Util.getDay(date),null);
                }
                else{// 完成
                    COUNTER = 0;
                    // 记录怀孕日期
                    Calendar pregnantCalendar = Calendar.getInstance();
                    pregnantCalendar.set(datePicker.getYear(),
                                        datePicker.getMonth(),
                                        datePicker.getDayOfMonth());
                    pregnantDate = pregnantCalendar.getTime();
                    saveDate(birthDate,pregnantDate);
//                    spu.setParam("pregnantDate",pregnantDate); // 保存怀孕日
                    dismiss();
                }
            }
        });
    }
    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    private void saveDate(Date birthDate,Date pregnantDate){
        // 本地保存
//        spu.setParam("birthDate",birthDate);
//        spu.setParam("pregnantDate",pregnantDate); // 保存怀孕日
        // 保存至本地数据库中
        User user = UserManager.getCurrentUser();
        user.setBirthDate(birthDate);
        user.setPregnantDate(pregnantDate);
        user.save();
        // 保存至服务器
        if(!user.isVisitor()){
            server.setBirthDate(birthDate);
            server.setPregnantDate(pregnantDate);
        }
    }

    private void showResponse(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
