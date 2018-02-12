package com.lemonxq_laplace.pregnantmonitor.view;

import android.content.Context;
import android.view.LayoutInflater;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekBar;
import com.lemonxq_laplace.pregnantmonitor.R;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/12
 */

public class EnglishWeekbar extends WeekBar {
    private int mPreSelectedIndex;

    public EnglishWeekbar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.english_week_bar, this, true);
//        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDateSelected(Calendar calendar, boolean isClick) {
//        getChildAt(mPreSelectedIndex).setSelected(false);
//        getChildAt(calendar.getWeek()).setSelected(true);
//        mPreSelectedIndex = calendar.getWeek();
    }
}
