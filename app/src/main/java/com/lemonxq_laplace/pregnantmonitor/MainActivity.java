package com.lemonxq_laplace.pregnantmonitor;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

//import android.app.Fragment;

/**
 * @author LemonXQ
 * @version 1.0
 * @time 2017/11/5
 * @updateAuthor
 */

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    List<Fragment> mFragments = new ArrayList<Fragment>();
    private ViewPager mMViewPager;
    private LinearLayout mMLinearLayout;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private ToolBar mMToolBar;
    private Toolbar mTitleBar;

    String[] bottomTitleArr = {"实时监控","智能护士","医孕平台"} ;
    // 在这里修改tap中的文字
    int[] bottomPic = {R.drawable.icon_message,R.drawable.icon_nurse,R.drawable.icon_selfinfo};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //数据源添加Fragment
        mFragments.add(new OneFragment());
        mFragments.add(new TwoFragment());
        mFragments.add(new ThreeFragment());

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
        mMToolBar.addBottomTab(mMLinearLayout,bottomTitleArr,bottomPic);
        // 设置默认第一个tab颜色为选中状态
        mMToolBar.changeColor(0);
        // viewpage监听器
        mMViewPager.setOnPageChangeListener(this);
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
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);// 显示导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);// 设置导航按钮图标
        }

        // 左拉菜单栏设置
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //处理任意菜单项点击
                Intent intent;
                switch(item.getItemId()){
                    case R.id.nav_analyze:
                        intent = new Intent(MainActivity.this,AnalyzeActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_task:
                        intent = new Intent(MainActivity.this,TaskActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_setting:
                        intent = new Intent(MainActivity.this,SettingActivity.class);
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

    }

//    private void replaceFragment(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragment_layout,fragment);
//        transaction.addToBackStack(null);// 添加事务到返回栈中
//        transaction.commit();
//    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
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

    class MyFragmentAdapter extends FragmentPagerAdapter{

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
}
