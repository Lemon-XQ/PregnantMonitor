package com.lemonxq_laplace.pregnantmonitor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.Util.ScreenUtil;

import java.text.NumberFormat;

/**
 * @author: Lemon-XQ
 * @date: 2017/12/4
 */

public class DountChartView extends View {

    /**
     * 默认起点扇形角度
     */
    private static final float mStartAngle = -90;
    /**
     * 第一圈圆环的颜色
     */
    private int mRingColor = Color.parseColor("#F05A4A");
    /**
     * 第二圈的颜色
     */
    private int mSectorColor = Color.parseColor("#29AB91");

    // 颜色及对应标注数组
    private int[] color = {mRingColor,mSectorColor};
    //    private String[] text = {"妊娠期糖尿病","正常"};
    private String[] text = {getResources().getString(R.string.GDM),
            getResources().getString(R.string.normal)};

    /**
     * 终点扇形角度
     */
    private float mEndAngle = mStartAngle;
    /**
     * 扇形扫角
     */
    private float mSweepAngle = 0;
    /**
     * 圈环的宽度
     */
    private float mCircleWidth = 0;
    /**
     * 圆点的半径
     */
    private float mDotRadius = 6f;
    /**
     * 字体的大小
     */
    private float mTextSize = ScreenUtil.sp2px(15,null);

    /**
     * 中心x坐标
     */
    private float centerX;
    /**
     * 中心y坐标
     */
    private float centerY;
    /**
     * 外圆的半径
     */
    private float mOuterRadius;
    /**
     * 内圆的半径
     */
    private float mInnerRadius;
    /**
     * 绘制圆环的画笔
     */
    private Paint mPaint;
    /**
     * 绘制文本的画笔
     */
    private Paint mTextPaint;
    /**
     * 绘制标签的画笔
     */
    private Paint mLabelPaint;
    /**
     * 用于绘制圆环的路径
     */
    private Path mPath;

    public DountChartView(Context context) {
        super(context);
        init(context);
    }

    public DountChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // 圆环画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 消除锯齿
        mPaint.setStyle(Paint.Style.FILL); // 设置填充
        // 绘制文本的画笔
        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(2); // 设置线和字体的宽度
        mTextPaint.setAntiAlias(true); // 消除锯齿
        mTextPaint.setTextSize(mTextSize);// 设置字体大小
        // 标注画笔
        mLabelPaint = new Paint();
        mLabelPaint.setAntiAlias(true);// 消除锯齿
        mLabelPaint.setStyle(Paint.Style.FILL); // 设置填充
        mLabelPaint.setStrokeWidth(2); // 设置线和字体的宽度
        // Path
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取圆心的x坐标
        centerX = getWidth() / 2f;
        // 获取圆心的y坐标
        centerY = getHeight() / 2f - ScreenUtil.dp2px(20,this.getContext());
        // 计算外圆和内圆的半径
        if (centerX < centerY) {
            mOuterRadius = centerX / 2f;// 外圆半径
        } else {
            mOuterRadius = centerY / 2f;
        }
        if (mCircleWidth <= 0 || mCircleWidth > mOuterRadius) {
            mInnerRadius = mOuterRadius / 2f;// 内圆半径
        } else {
            mInnerRadius = mOuterRadius - mCircleWidth;
        }
        // 设置外圆的颜色
        mPaint.setColor(mRingColor);
        mPath.addCircle(centerX, centerY, mOuterRadius, Path.Direction.CCW);
        mPath.close();
        // 绘制外圆
        canvas.drawPath(mPath, mPaint);
        mPath.reset();
        // 绘制进度扇形区域
        getSectorClip(canvas, mStartAngle);
        // 设置内圆的颜色
        mPaint.setColor(Color.WHITE);
        mPath.addCircle(centerX, centerY, mInnerRadius, Path.Direction.CCW);
        mPath.close();
        // 绘制内圆
        canvas.drawPath(mPath, mPaint);
        mPath.reset();
        // 获取进度圆弧的中心点
        float textX1 = (float) (centerX + mOuterRadius * Math.cos((mSweepAngle / 2 - 90) * Math.PI / 180));
        float textY1 = (float) (centerY + mOuterRadius * Math.sin((mSweepAngle / 2 - 90) * Math.PI / 180));
        // 设置画笔颜色
        mTextPaint.setColor(mSectorColor);
        drawText(canvas, getPer(mSweepAngle, 360), textX1, textY1, mTextPaint);
        // 获取进度圆弧的中心点
        float textX2 = (float) (centerX + mOuterRadius * Math.cos(((mSweepAngle - 360) / 2 - 90) * Math.PI / 180));
        float textY2 = (float) (centerY + mOuterRadius * Math.sin(((mSweepAngle - 360) / 2 - 90) * Math.PI / 180));
        // 设置画笔颜色
        mTextPaint.setColor(mRingColor);
        drawText(canvas, getPer(360 - mSweepAngle, 360), textX2, textY2, mTextPaint);
        // 画标注
        drawLabels(canvas);
    }

    private float margin = ScreenUtil.dp2px(30,this.getContext());
    private float labeltextMargin = ScreenUtil.dp2px(10,this.getContext());
    private float textMargin = ScreenUtil.dp2px(150,this.getContext());
    private float labelRadius = ScreenUtil.dp2px(10,this.getContext());
    private float textMargin2 = ScreenUtil.dp2px(25,this.getContext());

    private void drawLabels(Canvas canvas){
        for (int i = 0; i < 2; i++){
            // 画标注
//            float labelX = (float) (centerX - mOuterRadius + textMargin*i);
//            float labelY = (float) (centerY + mOuterRadius + margin);
            float labelX = (float) (centerX - 1.5*mOuterRadius );
            float labelY = (float) (centerY + mOuterRadius + margin +textMargin2*i);
            mLabelPaint.setColor(color[i]);// 设置画笔颜色
            canvas.drawCircle(labelX, labelY, labelRadius, mLabelPaint);
            // 画文字
            float textX = (float) (labelX + labeltextMargin);
            float textY = (float) (labelY + labeltextMargin);
            mTextPaint.setColor(color[i]);// 设置画笔颜色
            canvas.drawText(text[i], textX, textY, mTextPaint);
        }
    }

    /**
     * 绘制一个扇形的裁剪区
     *
     * @param canvas 画布
     * @param startAngle 扇形的起始角度
     */
    private void getSectorClip(Canvas canvas, float startAngle) {
        // 进度的颜色
        mPaint.setColor(mSectorColor);
        Path path = new Path();
        // 以下获得一个三角形的裁剪区
        // 圆心
        path.moveTo(centerX, centerY);
        // 起始点角度在圆上对应的横坐标
        float mStartAngleX = (float) (centerX + mOuterRadius * Math.cos(startAngle * Math.PI / 180));
        // 起始点角度在圆上对应的纵坐标
        float mStartAngleY = (float) (centerY + mOuterRadius * Math.sin(startAngle * Math.PI / 180));
        path.lineTo(mStartAngleX, mStartAngleY);
        // 终点角度在圆上对应的横坐标
        float mEndAngleX = (float) (centerX + mOuterRadius * Math.cos(mEndAngle * Math.PI / 180));
        // 终点角度在圆上对应的纵坐标
        float mEndAngleY = (float) (centerY + mOuterRadius * Math.sin(mEndAngle * Math.PI / 180));
        path.lineTo(mEndAngleX, mEndAngleY);
        // 回到初始点形成封闭的曲线
        path.close();
        // 设置一个正方形，内切圆
        RectF rectF = new RectF(centerX - mOuterRadius, centerY - mOuterRadius, centerX + mOuterRadius, centerY + mOuterRadius);
        // 获得弧形剪裁区的方法
        path.addArc(rectF, startAngle, mEndAngle - startAngle);
        canvas.drawPath(path, mPaint);
    }

    /**
     * 计算百分比
     *
     * @return 返回比例
     */
    private String getPer(float now, float total) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format(now / total * 100);
        return result + "%";
    }

    /**
     * 设置圆环宽度
     *
     * @param width 圆环宽度
     */
    public void setCircleWidth(int width) {
        mCircleWidth = width;
        invalidate();
    }

    /**
     * 设置当前进度
     */
    public void setPercentage(float ring, float sector) {
        mSweepAngle = (sector / (ring + sector)) * 360f;
        mEndAngle = mStartAngle + mSweepAngle;
        postInvalidate();
    }

    private float mOffset1X = 20;
    private float mOffset1Y = 20;
    private float mOffset2X = 150;
    private float mOffset2Y = 20;
    private float mPointOffset = 10;

    /**
     * 绘制文字
     *
     * @param canvas 画布
     * @param string 绘制的内容
     * @param firstX 起始点X坐标
     * @param firstY 起始点Y坐标
     * @param paint 文字的画笔
     */
    private void drawText(Canvas canvas, String string, float firstX, float firstY, Paint paint) {
        float endX1 = 0;
        float endY1 = 0;
        float endX2 = 0;
        float endY2 = 0;
        float textX = 0;
        float textY = 0;
        //初始点位于第四象限
        if (firstX <= centerX && firstY <= centerY) {
            firstX = firstX - mPointOffset;
            firstY = firstY - mPointOffset;
            endX1 = firstX - mOffset1X;
            endY1 = firstY - mOffset1Y;
            endX2 = firstX - mOffset2X;
            endY2 = firstY - mOffset2Y;
            textX = endX2;
            textY = endY2 - 10;
        }
        //初始点位于第三象限
        if (firstX < centerX && firstY > centerY) {
            firstX = firstX - mPointOffset;
            firstY = firstY + mPointOffset;
            endX1 = firstX - mOffset1X;
            endY1 = firstY + mOffset1Y;
            endX2 = firstX - mOffset2X;
            endY2 = firstY + mOffset2Y;
            textX = endX2;
            textY = endY2 + 30;
        }
        //初始点位于第一象限
        if (firstX > centerX && firstY < centerY) {
            firstX = firstX + mPointOffset;
            firstY = firstY - mPointOffset;
            endX1 = firstX + mOffset1X;
            endY1 = firstY - mOffset1Y;
            endX2 = firstX + mOffset2X;
            endY2 = firstY - mOffset2Y;
            textX = endX1;
            textY = endY2 - 10;
        }
        //初始点位于第二象限
        if (firstX >= centerX && firstY >= centerY) {
            firstX = firstX + mPointOffset;
            firstY = firstY + mPointOffset;
            endX1 = firstX + mOffset1X;
            endY1 = firstY + mOffset1Y;
            endX2 = firstX + mOffset2X;
            endY2 = firstY + mOffset2Y;
            textX = endX1;
            textY = endY2 + 30;
        }
        canvas.drawCircle(firstX, firstY, mDotRadius, paint);
        canvas.drawLine(firstX, firstY, endX1, endY1, paint);
        canvas.drawLine(endX1, endY1, endX2, endY2, paint);
        canvas.drawText(string, textX, textY, paint);
    }
}