package com.hdl.gctzsc.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.activity.mine.LoginActivity;
import com.hdl.gctzsc.utils.SysApplication;

public class GuideActivity extends AppCompatActivity {
    private static final String SHAERDPREF_FILENAME = "setting";// sharedpreference中存储的文件名
    private ViewPager vp_guide_bg_img_parent;// 导航页的背景图片的容器
    private int[] dh_imgs;// 导航页的背景图片
    private LinearLayout ll_guide_point_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏actionbar
        initView();
        initData();
    }

    private void initData() {
        dh_imgs = new int[]{R.mipmap.dh1, R.mipmap.dh2, R.mipmap.dh3,
                R.mipmap.dh4};
        vp_guide_bg_img_parent.setAdapter(new MyPagerAdapter());
        // 初始化完数据之后就监听viewpager的页面改变事件
        initPageChange();
    }

    /**
     * 初始化页面改变的监听事件
     */
    private void initPageChange() {
        vp_guide_bg_img_parent
                .setOnPageChangeListener(new OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        // 设置当前显示的点为position
                        updatePointBg(position);
                    }

                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
    }

    /**
     * 更新点的背景颜色
     */
    private void updatePointBg(int position) {
        // 设置点的显示位置
        // 现将所有的设置为不选中，
        for (int i = 0; i < dh_imgs.length; i++) {
            ll_guide_point_parent.getChildAt(i).setBackgroundResource(
                    R.drawable.shape_guide_point_unselected);
        }
        // 最后将position设置为选中即可
        ll_guide_point_parent.getChildAt(position).setBackgroundResource(
                R.drawable.shape_guide_point_selected);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        setContentView(R.layout.activity_guide);
        vp_guide_bg_img_parent = (ViewPager) findViewById(R.id.vp_guide_pagechanger);
        ll_guide_point_parent = (LinearLayout) findViewById(R.id.ll_home_point_parent);
    }

    /**
     * Viewpager的适配器类
     *
     * @author HDL
     */
    class MyPagerAdapter extends PagerAdapter {
        /**
         * 页面的个数
         */
        @Override
        public int getCount() {
            return dh_imgs.length;
        }

        /**
         * 固定写法
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 销毁页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        /**
         * 实例化子项
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(GuideActivity.this,
                    R.layout.viewpager_guide_dhimg, null);
            // 设置每一页的背景图片
            RelativeLayout rl_guide_dhimg = (RelativeLayout) view
                    .findViewById(R.id.rl_guide_dhimg);
            rl_guide_dhimg.setBackgroundResource(dh_imgs[position]);
            // 如果是最后一页就显示立即体验按钮(默认情况下应该是隐藏的)
            Button btn_start = (Button) view.findViewById(R.id.btn_guide_start);
            if (position == dh_imgs.length - 1) {
                btn_start.setVisibility(View.VISIBLE);
            }
            // 当点击btn的时候需要将SharedPreference中的isFirstStart设置为false,并跳转至主页面
            btn_start.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 设置软件为不是第一次启动了
                    SharedPreferences mSharedPreferences = getSharedPreferences(
                            SHAERDPREF_FILENAME, MODE_PRIVATE);
                    Editor editor = mSharedPreferences.edit();
                    editor.putBoolean("isFirstStart", false);
                    editor.commit();
                    // 跳转到主页面
                    Intent mIntent = new Intent(GuideActivity.this,
                            LoginActivity.class);
                    startActivity(mIntent);
                    // 此时需要销毁导航页面，
                    finish();
                }
            });
            // 将view添加到container中
            container.addView(view);
            return view;
        }
    }
}
