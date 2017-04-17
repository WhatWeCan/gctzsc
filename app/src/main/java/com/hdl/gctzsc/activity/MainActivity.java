package com.hdl.gctzsc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.customview.ToastUtils;
import com.hdl.gctzsc.fragments.HomeFragment;
import com.hdl.gctzsc.fragments.MineFragment;
import com.hdl.gctzsc.fragments.PublishFragment;
import com.hdl.gctzsc.utils.SysApplication;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost mFragmentTabHost;
    private String[] tab_titles;// tabhost子项的标题
    private String[] tab_tags;// tabhost的标记
    private Fragment[] fragments;// fragment数组
    private int[] selector_tabitems;// tab的选择器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏actionbar
        initView();
        initData();
        initTabHostItem();
    }

    private void initData() {
        tab_titles = new String[]{"首页", "发布", "我的"};
        tab_tags = new String[]{"main", "pulish", "mine"};
        fragments = new Fragment[]{new HomeFragment(), new PublishFragment(), new MineFragment()};
        selector_tabitems = new int[]{R.drawable.selector_main_tab1_item,
                R.drawable.selector_main_tab3_item,
                R.drawable.selector_main_tab5_item};
    }

    /**
     * 初始化tabhost的子项
     */
    private void initTabHostItem() {
        // 设置
        // 先安装注册
        mFragmentTabHost.setup(this, getSupportFragmentManager(),
                R.id.fl_main_pager_container);// 必须要这一步哦
        for (int i = 0; i < tab_tags.length; i++) {
            TabSpec tab = mFragmentTabHost.newTabSpec(tab_tags[i]);
            View view = View.inflate(this, R.layout.tabhost_item, null);
            // 设置图片
            ImageView iv = (ImageView) view
                    .findViewById(R.id.iv_tabhost_item_img);
            iv.setImageResource(selector_tabitems[i]);
            // 设置标题
            TextView tv = (TextView) view
                    .findViewById(R.id.tv_tabhost_item_title);
            tv.setText(tab_titles[i]);
            // 为tab添加视图
            tab.setIndicator(view);
            // 为fragment添加tab
            mFragmentTabHost.addTab(tab, fragments[i].getClass(), new Bundle());
            // mFragmentTabHost.addTab(tabSpec);
        }
        mFragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        setContentView(R.layout.activity_main);
        mFragmentTabHost = (FragmentTabHost) findViewById(R.id.fth_main_parent);
    }

    private long exitTime = 0;

    /**
     * 按两次返回键退出系统
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showInfo(this, "再按一次退出跳蚤市场");
                exitTime = System.currentTimeMillis();
            } else {
                SysApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
