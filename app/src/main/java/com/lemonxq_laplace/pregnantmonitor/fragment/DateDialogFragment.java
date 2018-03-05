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
import android.widget.RadioGroup;
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
    private RadioGroup pregnantChoice;
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
        pregnantChoice = mView.findViewById(R.id.pregnant_choice);
        Date date = new Date();
        datePicker.setMaxDate(date.getTime());
        title.setText(getResources().getString(R.string.birthDate_hint));
        nextBtn.setText(getResources().getString(R.string.next));
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

                    // 下一步记录用户是否已怀孕
                    title.setText(getResources().getString(R.string.pregnant_hint));
                    nextBtn.setText(getResources().getString(R.string.next));
                    datePicker.setVisibility(View.GONE);
                    pregnantChoice.setVisibility(View.VISIBLE);
                }
                else if(COUNTER == 1){
                    int checkId = pregnantChoice.getCheckedRadioButtonId();
                    if(checkId == R.id.pregnant_yes){// 已怀孕的进入下一步，选择怀孕日
                        COUNTER ++;
                        datePicker.setVisibility(View.VISIBLE);
                        pregnantChoice.setVisibility(View.GONE);
                        title.setText(getResources().getString(R.string.pregnantDate_hint));
                        nextBtn.setText(getResources().getString(R.string.finish));
                        // 设置怀孕日期初始值为今天
                        Date date = new Date();
                        datePicker.init(Util.getYear(date),Util.getMonth(date),Util.getDay(date),null);
                    }else if(checkId == R.id.pregnant_no){// 没有怀孕的直接关闭对话框
                        COUNTER = 0;
                        saveDate(birthDate,null);
                        dismiss();
                    }
                }else{// 完成
                    COUNTER = 0;
                    // 记录怀孕日期
                    Calendar pregnantCalendar = Calendar.getInstance();
                    pregnantCalendar.set(datePicker.getYear(),
                                        datePicker.getMonth(),
                                        datePicker.getDayOfMonth());
                    pregnantDate = pregnantCalendar.getTime();
                    saveDate(birthDate,pregnantDate);
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
        // 保存至本地数据库中
        User user = UserManager.getCurrentUser();
        user.setBirthDate(birthDate);
        if(pregnantDate != null)
            user.setPregnantDate(pregnantDate);
        user.save();
        // 保存至服务器
        if(!user.isVisitor()){
            server.setBirthDate(birthDate);
            if(pregnantDate != null)
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
