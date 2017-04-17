package com.hdl.gctzsc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hdl.gctzsc.modle.Goods;

import java.util.List;

/**
 * 商品的适配器
 */
// TODO: 2017/4/17 修改结束位置

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsHolder> {
    private List<Goods> goods;
    private Context context;

    @Override
    public GoodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(GoodsHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    public class GoodsHolder extends RecyclerView.ViewHolder {
        public GoodsHolder(View itemView) {
            super(itemView);
        }
    }
}
