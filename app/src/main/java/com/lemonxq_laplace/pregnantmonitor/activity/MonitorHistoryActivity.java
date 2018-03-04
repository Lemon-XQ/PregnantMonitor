package com.lemonxq_laplace.pregnantmonitor.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.lemonxq_laplace.pregnantmonitor.Data.Record;
import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.Util.Database;
import com.lemonxq_laplace.pregnantmonitor.Util.UserManager;
import com.lemonxq_laplace.pregnantmonitor.Util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: Lemon-XQ
 * @date: 2018/2/8
 */

public class MonitorHistoryActivity extends BaseActivity {

    private MaterialSpinner dateSpinner;
    private CalendarView calendarView;
    private TextView ageText;
    private TextView heightText;
    private TextView weightText;
    private TextView ogttText;
    private TextView resText;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_history);
        initView();
        initData();
        setListeners();
    }

    void initView(){
        dateSpinner = findViewById(R.id.dateSpinner);
        calendarView = findViewById(R.id.history_calendarView);
        ageText = findViewById(R.id.history_age);
        heightText = findViewById(R.id.history_height);
        weightText = findViewById(R.id.history_weight);
        ogttText = findViewById(R.id.history_ogtt);
        resText = findViewById(R.id.history_result);
        back = findViewById(R.id.back);
    }

    private int currentIndex;
    void initData(){
        currentIndex = 1;
        int cur_year = Util.getYear(new Date());
        int cur_month = Util.getMonth(new Date());

        List<String> dateList = new ArrayList<>();
        for(int year=2018; year<=2050; year++){
            for(int month=1;month<=12;month++){
                dateList.add(year+"-"+month);
                if(year <= cur_year && month < cur_month) currentIndex++;
            }
        }
        dateSpinner.setItems(dateList);
        dateSpinner.setSelectedIndex(currentIndex);
        List<Record> recordList = Database.findRecords(UserManager.getCurrentUser());
        if(!recordList.isEmpty()){
            List<Calendar> recordDateList = new ArrayList<>();
            for(Record record : recordList){
                Log.d("MonitorHistory","date:"+record.getDate());
                Log.d("MonitorHistory","year:"+Util.dateToCalendar(record.getDate()).getYear());
                Log.d("MonitorHistory","month:"+Util.dateToCalendar(record.getDate()).getMonth());
                Log.d("MonitorHistory","day:"+Util.dateToCalendar(record.getDate()).getDay());
                recordDateList.add(Util.dateToCalendar(record.getDate()));
            }
            calendarView.setSchemeDate(recordDateList);
        }
    }

    void setListeners(){
        // 年月选择监听
        dateSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                String[] dateStr = item.split("-");
                int year = Integer.parseInt(dateStr[0]);
                int month = Integer.parseInt(dateStr[1]);
                calendarView.scrollToCalendar(year,month,1);
            }
        });

        // 日期选择监听
        calendarView.setOnDateSelectedListener(new CalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Calendar calendar, boolean isClick) {
//                Util.makeToast(MonitorHistoryActivity.this,calendar.toString());

                // 找当前日期当前用户的记录
                Record cur_record = Database.findRecord(String.valueOf(Util.formDate(calendar).getTime()),
                                                        UserManager.getCurrentUser());
                if(cur_record != null){
                    ageText.setText(String.valueOf(cur_record.getAge()));
                    weightText.setText(String.valueOf(cur_record.getWeight()));
                    heightText.setText(String.valueOf(cur_record.getHeight()*100));
                    ogttText.setText(String.valueOf(cur_record.getOgtt()));
                    resText.setText(Util.getResultStr(cur_record.isHealthy()));
                }else {
                    // 置为无记录
                    String str = getResources().getString(R.string.no_record);
                    ageText.setText(str);
                    weightText.setText(str);
                    heightText.setText(str);
                    ogttText.setText(str);
                    resText.setText(str);
                }
            }
        });
        // 滑动日历时更改Title显示
        calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                int cur_month = calendarView.getCurMonth();
                int cur_year = calendarView.getCurYear();
                int offset;
                if(cur_year == year){
                    offset = month - cur_month;
                }else{
                    offset = (year - cur_year)*12 + month - cur_month;
                }
                dateSpinner.setSelectedIndex(currentIndex + offset);
            }
        });
        // 返回键监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }
}
