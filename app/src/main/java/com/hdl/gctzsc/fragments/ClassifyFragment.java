package com.hdl.gctzsc.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.activity.home.GoodsListActivity;


public class ClassifyFragment extends Fragment {

    private View view_container;
    private GridView gv_classify_menu;
    private int[] imgs = {R.mipmap.ddc, R.mipmap.phone, R.mipmap.computer, R.mipmap.keyback,
            R.mipmap.camaro, R.mipmap.jiadian, R.mipmap.football, R.mipmap.cclothes,
            R.mipmap.book, R.mipmap.network, R.mipmap.taideng, R.mipmap.cgood};
    private String[] titls = {"校园代步", "手机", "电脑", "数码配件", "数码", "电器", "运动健身", "衣物伞帽", "图书教材", "校园网", "生活娱乐", "其他"};
    private String[] contents = {"自行车 电动车", "iPhone 小米 魅族 金立 华为", "联想 戴尔 Mac 小米", "耳机 U盘 键盘",
            "iPad 相机 单反 游戏机", "电扇 台灯 空气净化器", "篮球 足球 球拍", "上衣 裤子 背包",
            "教材 考研 申论 公务员 银行", "自行车 道具 服装 学士服", "乐器 日常 会员卡", "可能有您感兴趣的东西"};
    private LinearLayout ll_autoBicycle;
    private LinearLayout ll_bicycle;
    private LinearLayout ll_computer;
    private LinearLayout ll_book;
    private ImageView iv_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view_container == null) {
            view_container = inflater.inflate(R.layout.fragment_classify, null);
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
     * 初始化视图
     */
    private void initView() {
        gv_classify_menu = (GridView) view_container.findViewById(R.id.gv_classify_menu);
        ll_bicycle = (LinearLayout) view_container.findViewById(R.id.ll_classify_bicycle);
        ll_autoBicycle = (LinearLayout) view_container.findViewById(R.id.ll_classify_autobicycle);
        ll_computer = (LinearLayout) view_container.findViewById(R.id.ll_classify_computer);
        ll_book = (LinearLayout) view_container.findViewById(R.id.ll_classify_book);
        iv_search = (ImageView) view_container.findViewById(R.id.iv_classify_search);
        setGridViewAdapter();
        setListener();
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), GoodsListActivity.class);
                it.putExtra("type", "search");
                startActivity(it);
            }
        });
        ll_bicycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goGoodsList("自行车", 0);
            }
        });
        ll_autoBicycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goGoodsList("电动车", 0);
            }
        });
        ll_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goGoodsList("教材", 8);
            }
        });
        ll_computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goGoodsList("电脑", 2);
            }
        });
        gv_classify_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goGoodsList(titls[i], i);
            }
        });
    }

    /**
     * 跳转到商品列表页面
     *
     * @param title
     */
    private void goGoodsList(String title, int position) {
        Intent it = new Intent(getActivity(), GoodsListActivity.class);
        it.putExtra("type", "classify");
        it.putExtra("title", title);
        it.putExtra("position", position);
        startActivity(it);
    }

    /**
     * 设置适配器
     */
    private void setGridViewAdapter() {
        gv_classify_menu.setAdapter(new MyGridViewApater());
    }

    class MyGridViewApater extends BaseAdapter {

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View item_view = View.inflate(getActivity(), R.layout.gridview_item_classify_menu, null);
            ImageView iv_icon = (ImageView) item_view.findViewById(R.id.iv_classify_icon);
            iv_icon.setImageResource(imgs[i]);
            TextView tv_title = (TextView) item_view.findViewById(R.id.tv_classify_title);
            tv_title.setText(titls[i]);
            TextView tv_content = (TextView) item_view.findViewById(R.id.tv_classify_content);
            tv_content.setText(contents[i]);
            return item_view;
        }
    }
}
