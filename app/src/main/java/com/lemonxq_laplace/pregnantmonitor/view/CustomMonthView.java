package com.lemonxq_laplace.pregnantmonitor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.lemonxq_laplace.pregnantmonitor.R;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/12
 */

public class CustomMonthView extends MonthView {
    private int mRadius;

    public CustomMonthView(Context context) {
        super(context);
        mSchemePaint.setColor(Color.WHITE);
        mSchemeTextPaint.setColor(Color.GREEN);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mCurMonthTextPaint.setColor(Color.WHITE);
        mSchemePaint.setStyle(Paint.Style.FILL);
//        mSchemePaint.setShadowLayer(15, 1, 3, 0xAA333333);// 阴影
        mSelectTextPaint.setColor(getResources().getColor(R.color.colorPrimary));
        setLayerType( LAYER_TYPE_SOFTWARE , null);

    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
//        mSelectTextPaint.setColor(getResources().getColor(R.color.colorPrimary));
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + 4*mItemWidth / 5;
        int cy = y + mItemHeight / 5;
//        int cx = x + mItemWidth / 2;
//        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius/4, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        }else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
