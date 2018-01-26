package com.lemonxq_laplace.pregnantmonitor;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ToolBar {

    //添加孩子布局的集合
    List<TextView> mTextViews = new ArrayList<TextView>();

    public void addBottomTab(LinearLayout container, String[] bottomTitleArr, int[] bottomPic) {

        for (int i = 0; i < bottomTitleArr.length; i++) {
            TextView childView = (TextView) View.inflate(container.getContext(), R.layout.bottom_tab_textview, null);
            //给TextView添加文本
            childView.setText(bottomTitleArr[i]);
            //修改对应位置的图片.参数代表位于TextView的哪个方位。仅仅位于上方
            childView.setCompoundDrawablesWithIntrinsicBounds(0, bottomPic[i], 0, 0);

            //把两个底部tab平分秋色.使用paramas对象

            int w = 0;
            int h = LinearLayout.LayoutParams.WRAP_CONTENT;
            //创建params对象，并绘制具体的控件的宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
            //weight设置为1，才是真正的均分父容器宽度
            params.weight = 1;

            /*点击textView，传递点击的位置给活动*/
            final int finalI = i;
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //3、在需要通过接口回调传值的地方调用一次接口回调方法
                    mOnToolBarChangeListener.onToolBarChange(finalI);
                }
            });

            container.addView(childView, params);

            //孩子添加到集合中保存
            mTextViews.add(childView);
        }
    }

    /**
     * 进入某项fragment，对之改变Tab图标的颜色.在ViewPage切换到一项fragment的时候调用
     */
    public void changeColor(int position) {
        //还原所有颜色
        for (TextView textView : mTextViews) {
            //在这里就知道我为什么定义状态选择器的时候，使用selected属性了
            textView.setSelected(false);
        }

        //让当前fragment位置的tab改变颜色
        mTextViews.get(position).setSelected(true);
    }

    /***使用接口传递控件点击的位置*/

    //定义接口和接口回调的方法
    public interface OnToolBarChangeListener {
        void onToolBarChange(int position);
    }

    //创建接口变量，作为接口对象
    OnToolBarChangeListener mOnToolBarChangeListener;

    public void setOnToolBarChangeListener(OnToolBarChangeListener onToolBarChangeListener) {
        mOnToolBarChangeListener = onToolBarChangeListener;
    }
}
