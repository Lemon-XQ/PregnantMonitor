package com.lemonxq_laplace.pregnantmonitor.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lemonxq_laplace.pregnantmonitor.Data.User;
import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.ToolBar;
import com.lemonxq_laplace.pregnantmonitor.Util.ActivityController;
import com.lemonxq_laplace.pregnantmonitor.Util.Database;
import com.lemonxq_laplace.pregnantmonitor.Util.UserManager;
import com.lemonxq_laplace.pregnantmonitor.Util.Util;
import com.lemonxq_laplace.pregnantmonitor.fragment.DateDialogFragment;
import com.lemonxq_laplace.pregnantmonitor.fragment.MonitorFragment;
import com.lemonxq_laplace.pregnantmonitor.fragment.ToolFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author LemonXQ
 * @version 1.0
 * @time 2017/11/5
 * @updateAuthor
 */

public class MainActivity extends BaseActivity  implements DialogInterface.OnDismissListener{

    List<Fragment> mFragments = new ArrayList<Fragment>();
    private ViewPager mMViewPager;
    private LinearLayout mMLinearLayout;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private ToolBar mMToolBar;
    private Toolbar mTitleBar;
    private MonitorFragment monitorFragment = new MonitorFragment();
    final DateDialogFragment dateDialog = new DateDialogFragment();

    String[] bottomTitleArr = new String[2];
    int[] bottomPic = {R.drawable.icon_message,R.drawable.icon_selfinfo};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomTitleArr[0] = getResources().getString(R.string.Monitor);
        bottomTitleArr[1] = getResources().getString(R.string.Tool);

        //数据源添加Fragment
        mFragments.add(monitorFragment);
        mFragments.add(new ToolFragment());

        // 初始化
        mMViewPager = (ViewPager) findViewById(R.id.viewpagge);
        mMLinearLayout = (LinearLayout) findViewById(R.id.llbootom_container);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mTitleBar = (Toolbar) findViewById(R.id.titlebar);

        // 底部导航栏设置
        // 给viewpage设置适配器
        mMViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        mMToolBar = new ToolBar();
        // 给底部tab添加新的控件
        mMToolBar.addBottomTab(mMLinearLayout, bottomTitleArr, bottomPic);
        // 设置默认第一个tab颜色为选中状态
        mMToolBar.changeColor(0);
        // viewpage监听器
        mMViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 某项ViewPage选中的时候调用。在这里顺便改变底部Tab的颜色
                mMToolBar.changeColor(position);
                // 同时修改对应fragment的标题
                mTitleBar.setTitle(bottomTitleArr[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 接口回调的方式，点击底部Tab，切换不同的页面
        mMToolBar.setOnToolBarChangeListener(new ToolBar.OnToolBarChangeListener() {
            @Override
            public void onToolBarChange(int position) {
                // 切换对应的fragment
                mMViewPager.setCurrentItem(position);
            }
        });

        // 顶部导航条设置
        setSupportActionBar(mTitleBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 显示导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu2);// 设置导航按钮图标
        }

        // 左拉菜单栏设置
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //处理任意菜单项点击
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_analyze:
                        intent = new Intent(MainActivity.this, AnalyzeActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_task:
                        intent = new Intent(MainActivity.this, TaskActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_setting:
                        intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_info:

                        break;
                    default:
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        // 是否弹出初始日期设置框（出生年月及怀孕日期）
        showDateDialog();

        initUserData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 导航按钮
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            // 其他ToolBar按钮
            default:
                break;
        }
        return true;
    }

    private void showDateDialog(){
//        SharedPreferencesUtil spu = new SharedPreferencesUtil(this);
//        spu.setParam("isInitialized",false);
        // 若本地尚未初始化日期
//        if(!(Boolean)spu.getParam("isInitialized",false)){
        if(Database.findDateByUsername(UserManager.getCurrentUser().getAccount()) == null){
            //TODO 服务器是否存有该用户日期信息
            dateDialog.setCancelable(false);
            dateDialog.show(getFragmentManager(),"date");
//            spu.setParam("isInitialized",true);
        }
    }

    void initUserData(){

    }

    void finishInitDate(Date pregnantDate){
        // 更新UI界面
        long days = Util.getPregnantDays(pregnantDate);
        long months = Util.getPregnantMonths(pregnantDate);
        long weeks = Util.getPregnantWeeks(pregnantDate);
        monitorFragment.updateShow(days,months,weeks);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // 保存生日及怀孕日期至数据库中
        User user = UserManager.getCurrentUser();
        user.setBirthDate(dateDialog.birthDate);
        user.setPregnantDate(dateDialog.pregnantDate);
        user.save();

        finishInitDate(dateDialog.pregnantDate);
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms退出程序
                ActivityController.finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
