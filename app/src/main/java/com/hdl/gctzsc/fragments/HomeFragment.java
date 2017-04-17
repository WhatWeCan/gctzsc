package com.hdl.gctzsc.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.modle.Goods;
import com.hdl.gctzsc.utils.NetUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 首页
 */
public class HomeFragment extends Fragment {

    private View view_container;
    private SwipeRefreshLayout srlGoods;
    private RecyclerView rvGoods;
    private TextView tvTitle;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view_container == null) {
            view_container = inflater.inflate(R.layout.fragment_main, container, false);
        }
        ViewGroup parent = (ViewGroup) view_container.getParent();
        if (parent != null) {
            parent.removeAllViewsInLayout();
        }
        return view_container;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        initView();

    }

    private void initView() {
        srlGoods = (SwipeRefreshLayout) view_container.findViewById(R.id.srl_goods);
        rvGoods = (RecyclerView) view_container.findViewById(R.id.rv_goods);
        tvTitle = (TextView) view_container.findViewById(R.id.tv_header_title_txt);
        tvTitle.setText("最新发布");

        setSwipeRefreshLayout();

        rvGoods.setAdapter(new GoodsAdapter());
        rvGoods.setLayoutManager(new LinearLayoutManager(mContext));
        rvGoods.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 设置下拉刷新 SwipeRefreshLayout
     */
    private void setSwipeRefreshLayout() {
        //设置刷新的背景
        srlGoods.setProgressBackgroundColorSchemeResource(android.R.color.white);
        //刷新过程 刷新动画 变化的颜色
        srlGoods.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srlGoods.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        srlGoods.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //获取数据
                getData();
            }
        });
    }

    private void getData() {
        if (NetUtils.isConnected(mContext)) {
            srlGoods.setRefreshing(true);
            BmobQuery<Goods> query = new BmobQuery<>();
            query.findObjects(new FindListener<News>() {
                @Override
                public void done(List<News> list, BmobException e) {
                    if (e == null) {
                        datas.clear();
                        datas.addAll(list);
                        commonAdapter.notifyDataSetChanged();
                        showDataView();
                        Toast.makeText(mContext, "数据更新完成", Toast.LENGTH_SHORT).show();
                    } else {
                        showNoticeView("获取数据失败，点击重试");
                    }
                    srlCollection.setRefreshing(false);
                }
            });
        } else {
            Toast.makeText(mContext, "无网络", Toast.LENGTH_SHORT).show();
        }
    }
}
