package com.hdl.gctzsc.activity.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.activity.home.GoodsDetailActivity;
import com.hdl.gctzsc.customview.ToastUtils;
import com.hdl.gctzsc.customview.TopBackAndTitleView;
import com.hdl.gctzsc.utils.ProgressDialogUtils;
import com.lidroid.xutils.BitmapUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManagerGoodsActivity extends Activity {
    public static final String TAG = "ManagerGoodsActivity";
    public static final int WHAT_DATA_PARSE_FAILED = 203;
    public static final int WHAT_GET_PAI_FINISHED = 205;
    private static final int WHAT_GOODS_UNEXISTED = 207;
    private static final int WHATE_DELETE_GOODS = 240;
    private static final int WHATE_DELETE_GOODS_FAILED = 241;
    private RotateAnimation animation;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            animation.cancel();//停止刷新
            progressDialogUtils.closeProgressDialog();
            switch (msg.what) {
                case WHAT_DATA_PARSE_FAILED:
                    ToastUtils.showInfo(ManagerGoodsActivity.this, "数据解析失败，请检查网络");
                    break;
                case WHATE_DELETE_GOODS:
                    ToastUtils.showInfo(ManagerGoodsActivity.this, "操作成功");
                    updateDate();
                    break;
                case WHATE_DELETE_GOODS_FAILED:
                    ToastUtils.showInfo(ManagerGoodsActivity.this, "请求数据失败了，请重试");
                    break;
                case WHAT_GET_PAI_FINISHED:
                    goodsList = (List<PaiGoods>) msg.obj;
                    showDataToVier();
//                    Log.e(TAG, "获取到数据了" + goodsList.size());
                    ToastUtils.showInfo(ManagerGoodsActivity.this, "加载成功");
                    break;
                case WHAT_GOODS_UNEXISTED:
                    break;
            }
        }
    };


    private ListView lv_nomerl;
    private ListView lv_off;
    private ProgressDialogUtils progressDialogUtils;
    private List<PaiGoods> goodsList;
    private List<PaiGoods> goodsList_nomerl;
    private List<PaiGoods> goodsList_off;
    private LinearLayout ll_of;
    private LinearLayout ll_nomerl;
    private ImageView iv_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        updateDate();
    }
    /**
     * 设置刷新按钮的动画
     */
    private void setAnimation() {
        animation = new RotateAnimation(0, 360f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(40);
    }
    /**
     * 设置监听器
     */
    private void setListener() {
        if (goodsList_nomerl.size() > 0) {
            lv_nomerl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent it = new Intent(ManagerGoodsActivity.this, GoodsDetailActivity.class);
                    it.putExtra("gId", goodsList_nomerl.get(i).getgId());
                    startActivity(it);
                }
            });
        }
        if (goodsList_off.size() > 0) {
            lv_off.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent it = new Intent(ManagerGoodsActivity.this, GoodsDetailActivity.class);
                    it.putExtra("gId", goodsList_off.get(i).getgId());
                    startActivity(it);
                }
            });
        }
    }

    /**
     * 刷新数据
     */
    private void updateDate() {
        progressDialogUtils.showProgressDialog();
        getGoods();
    }

    /**
     * 加载数据到视图了
     */
    private void showDataToVier() {
        goodsList_nomerl = new ArrayList<PaiGoods>();
        goodsList_off = new ArrayList<PaiGoods>();
        for (PaiGoods goods : goodsList) {
//            Log.e(TAG, "商品简介" + goods.getgDesc());
            if (goods.isgIsSaled()) {
                goodsList_off.add(goods);
            } else {
                goodsList_nomerl.add(goods);
            }
        }
        setAdapter();
        setListener();
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        ll_of.removeAllViews();
        ll_nomerl.removeAllViews();
        if (goodsList_nomerl.size() == 0) {
            TextView tv_show = new TextView(this);
            tv_show.setText("暂无在架的商品，快去发布吧！");
            ll_nomerl.addView(tv_show);
        }
        lv_nomerl.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return goodsList_nomerl.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            class ViewHolder {
                Button btn_off, btn_delete;
                TextView tv_title, tv_desc, tv_price;
                ImageView iv_img;
                View view;

                public ViewHolder(View view) {
                    this.view = view;
                    initItemView();
                }

                /**
                 * 初始化视图
                 */
                private void initItemView() {
                    btn_off = (Button) view.findViewById(R.id.btn_manager_xj);
                    btn_delete = (Button) view.findViewById(R.id.btn_manager_delete);
                    tv_title = (TextView) view.findViewById(R.id.tv_manager_title);
                    tv_desc = (TextView) view.findViewById(R.id.tv_manager_desc);
                    tv_price = (TextView) view.findViewById(R.id.tv_manager_price);
                    iv_img = (ImageView) view.findViewById(R.id.iv_manager_img);
                }

                /**
                 * 初始化视图
                 */
                private void setData(int position) {
                    final PaiGoods goods = goodsList_nomerl.get(position);
                    new BitmapUtils(ManagerGoodsActivity.this).display(iv_img, goods.getgImgUrls().get(0));
                    tv_title.setText(goods.getgTitle() + "");
                    tv_desc.setText(goods.getgDesc() + "");
                    tv_price.setText("￥" + goods.getgPrice() + ".00");
                    if (goods.getgPrice() == 0) {
                        tv_price.setText("免费");
                    }
                    if (goods.getgPublishType() == 1) {
                        tv_price.setText("￥" + goods.getgPrice() + ".00(拍卖中)点击查看详细信息");
                    }
                    btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ManagerGoodsActivity.this);
                            builder.setTitle("永久删除");
                            builder.setMessage("您确定要永久删除商品吗？（不可找回）");
                            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                HttpStreamUtils.sendHttpRequest(handler, WHATE_DELETE_GOODS, getResources().getString(R.string.url_manage_goods) + "?gid=" + goods.getgId() + "&option=delete");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                handler.sendEmptyMessage(WHATE_DELETE_GOODS_FAILED);
                                            }
                                        }
                                    }.start();
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                    btn_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ManagerGoodsActivity.this);
                            builder.setTitle("下架商品");
                            builder.setMessage("您确定要下架正在出售的商品吗？");
                            builder.setPositiveButton("下架", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                HttpStreamUtils.sendHttpRequest(handler, WHATE_DELETE_GOODS, getResources().getString(R.string.url_manage_goods) + "?gid=" + goods.getgId() + "&option=off");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                handler.sendEmptyMessage(WHATE_DELETE_GOODS_FAILED);
                                            }
                                        }
                                    }.start();
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                }
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = View.inflate(ManagerGoodsActivity.this, R.layout.listview_item_manger_goods, null);
                    ViewHolder viewHolder = new ViewHolder(view);
                    view.setTag(viewHolder);
                }
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.setData(i);
                return view;
            }
        });

        if (goodsList_off.size() == 0) {
            TextView tv_show = new TextView(this);
            tv_show.setText("暂无已经下架的商品，如果已经卖出了亲及时下架");
            ll_of.addView(tv_show);
        }
        lv_off.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return goodsList_off.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            class ViewHolder {
                Button btn_off, btn_delete;
                TextView tv_title, tv_desc, tv_price;
                ImageView iv_img;
                View view;

                public ViewHolder(View view) {
                    this.view = view;
                    initItemView();
                }

                /**
                 * 初始化视图
                 */
                private void initItemView() {
                    btn_off = (Button) view.findViewById(R.id.btn_manager_xj);
                    btn_off.setVisibility(View.GONE);
                    btn_delete = (Button) view.findViewById(R.id.btn_manager_delete);
                    tv_title = (TextView) view.findViewById(R.id.tv_manager_title);
                    tv_desc = (TextView) view.findViewById(R.id.tv_manager_desc);
                    tv_price = (TextView) view.findViewById(R.id.tv_manager_price);
                    iv_img = (ImageView) view.findViewById(R.id.iv_manager_img);
                }

                /**
                 * 初始化视图
                 */
                private void setData(int position) {
                    PaiGoods goods = goodsList_off.get(position);
                    final int gId = goods.getgId();
                    new BitmapUtils(ManagerGoodsActivity.this).display(iv_img, goods.getgImgUrls().get(0));
                    tv_title.setText(goods.getgTitle() + "");
                    tv_desc.setText(goods.getgDesc() + "");
                    tv_price.setText("￥" + goods.getgPrice() + ".00");
                    if (goods.getgPrice() == 0) {
                        tv_price.setText("免费");
                    }
                    if (goods.getgPublishType() == 1) {
                        tv_price.setText("￥" + goods.getgPrice() + ".00(拍卖结束)点击查看详细信息");
                    }
                    btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ManagerGoodsActivity.this);
                            builder.setTitle("永久删除");
                            builder.setMessage("您确定要永久删除商品吗？（不可找回）");
                            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                HttpStreamUtils.sendHttpRequest(handler, WHATE_DELETE_GOODS,
                                                        getResources().getString(R.string.url_manage_goods) + "?gid=" + gId + "&option=delete");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                handler.sendEmptyMessage(WHATE_DELETE_GOODS_FAILED);
                                            }
                                        }
                                    }.start();
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                }
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = View.inflate(ManagerGoodsActivity.this, R.layout.listview_item_manger_goods, null);
                    ViewHolder viewHolder = new ViewHolder(view);
                    view.setTag(viewHolder);
                }
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.setData(i);
                return view;
            }
        });

    }

    /**
     * 获取商品
     */
    private void getGoods() {
        //开启线程读取数据
        new Thread() {
            @Override
            public void run() {
                GoodsInfoQueryHttpUtils.getGoodsInfoToList(ManagerGoodsActivity.this,
                        getResources().getString(R.string.url_query_goods) + "?type=person&username="
                                + getSharedPreferences("login", MODE_PRIVATE).getString("username", ""), handler);
            }
        }.start();
    }

    private void initView() {
        setContentView(R.layout.activity_manager_personel_goods);
        TopBackAndTitleView action_bar_manager = (TopBackAndTitleView) findViewById(R.id.action_bar_manager);
        action_bar_manager.setText("管理个人商品");
        lv_nomerl = (ListView) findViewById(R.id.lv_manager_nomerl);
        lv_off = (ListView) findViewById(R.id.lv_manager_off);
        ll_nomerl = (LinearLayout) findViewById(R.id.ll_manager_show_nomerl);
        ll_of = (LinearLayout) findViewById(R.id.ll_manager_show_off);
        iv_refresh = (ImageView) findViewById(R.id.iv_manager_refresh);
        progressDialogUtils = new ProgressDialogUtils(this, "加载数据", "主银,正在玩命加载中......");
        setAnimation();
    }

    /**
     * 刷新
     *
     * @param view
     */
    public void onRefresh(View view) {
        iv_refresh.startAnimation(animation);//开始旋转
        updateDate();
    }
}
