package com.hdl.gctzsc.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.activity.mine.LoginActivity;
import com.hdl.gctzsc.activity.mine.ManagerGoodsActivity;
import com.hdl.gctzsc.activity.mine.SettingsActivity;
import com.hdl.gctzsc.activity.mine.UserInfoActivity;

public class MineFragment extends Fragment {

    private View view_container;
    private LinearLayout ll_settings;
    private LinearLayout ll_userInfo;
    private LinearLayout ll_manager_goods;
    private static final int WHAT_UPREATE_NICKNAME = 1000;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_UPREATE_NICKNAME) {
                tv_nickName.setText((String) msg.obj);
            }
        }
    };


    private static TextView tv_nickName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view_container == null) {
            view_container = inflater.inflate(R.layout.fragment_mine, null);
        }
        ViewGroup parent = (ViewGroup) view_container.getParent();
        if (parent != null) {
            parent.removeAllViewsInLayout();
        }
        return view_container;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        tv_nickName = (TextView) view_container.findViewById(R.id.tv_mine_nickname);
        tv_nickName.setText(getActivity().getSharedPreferences("login", Context.MODE_PRIVATE).getString("nickname", "我是小跳蚤"));
        TextView tv_title = (TextView) view_container.findViewById(R.id.tv_classify_custom_actionbar_title);
        tv_title.setText("个人中心");
//        btn_login = (Button) view_container.findViewById(R.id.btn_mine_login);
        ll_userInfo = (LinearLayout) view_container.findViewById(R.id.ll_mine_userinfo);
        ll_settings = (LinearLayout) view_container.findViewById(R.id.ll_mine_settings);
        ll_manager_goods = (LinearLayout) view_container.findViewById(R.id.ll_mine_manager_goods);
        setListener();
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        ll_manager_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ManagerGoodsActivity.class));
            }
        });
        ll_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });
        ll_userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
            }
        });
    }
}
