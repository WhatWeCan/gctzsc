package com.hdl.gctzsc.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.customview.ToastUtils;
import com.hdl.gctzsc.modle.Goods;
import com.hdl.gctzsc.utils.ClassifyArrays;
import com.hdl.gctzsc.utils.DateUtils;
import com.hdl.gctzsc.utils.ProgressDialogUtils;
import com.hdl.gctzsc.utils.SysApplication;
import com.lidroid.xutils.BitmapUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;

public class GoodsListActivity extends Activity {

    private GridView gv_goodsList;
    private Intent mIntent;
    private TextView tv_actionBarTitle;
    private ImageView iv_refresh;
    private static final int WHAT_GET_ALL_GOODSINFO = 205;
    private String menu_title[] = {"全部", "最新发布", "热门", "拍卖", "免费送", "急售"};
    private static final int WHAT_GET_ALL_GOODSINFO_FAILED = 203;
    public static final int WHAT_GET_DATA_FINISHED = 200;
    private RotateAnimation animation;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            animation.cancel();//停止刷新
            progressDialogUtils.closeProgressDialog();
            switch (msg.what) {
                case WHAT_GET_ALL_GOODSINFO:
                    goodsList = (List<Goods>) msg.obj;
                    showDate();
                    break;
                case WHAT_GET_DATA_FINISHED:
                    ToastUtils.showInfo(GoodsListActivity.this, "加载成功");
                    goodsList = (List<Goods>) msg.obj;
                    showDate();
                    break;
                case WHAT_GET_ALL_GOODSINFO_FAILED:
                    ToastUtils.showInfo(GoodsListActivity.this, "数据加载失败");
                    break;
            }
        }
    };
    private EditText et_search;
    private Button btn_search;

    /**
     * 显示数据到视图上
     */
    private void showDate() {
        setAdapter();
    }

    private ProgressDialogUtils progressDialogUtils;
    private List<Goods> goodsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        progressDialogUtils = new ProgressDialogUtils(this, "加载数据", "拼命加载数据中......");
        initView();
        updateDate();
    }
//    private String[] titls = {"校园代步", "手机", "电脑", "数码配件", "数码", "电器", "运动健身", "衣物伞帽", "图书教材", "校园网", "生活娱乐", "其他"};

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
     * 更新数据
     */
    private void updateDate() {
        progressDialogUtils.showProgressDialog();

        mIntent = getIntent();
        String type = mIntent.getStringExtra("type");
        if ("search".equals(type)) {

        } else if ("classify".equals(type)) {
            final int position = mIntent.getIntExtra("position", 0);
            //开启线程读取数据
            new Thread() {
                @Override
                public void run() {
                    GoodsInfoQueryHttpUtils.getNewGoodsInfoToList(GoodsListActivity.this,
                            getResources().getString(R.string.url_query_goods) + "?type=all&position=" + position, handler);
                }
            }.start();
        } else if ("other".equals(type)) {
            int title = mIntent.getIntExtra("title", 0);
//            Log.e("mylog", "查询的是哪里的？" + title + "----");
            switch (title) {
                case 0:
                    getAllGoodsInfo();
                    break;
                case 1:
                    getNewGoodsInfo();
                    break;
                case 2:
                    getHotGoodsInfo();
                    break;
                case 3:
                    getPaiGoodsInfo();
                    break;
                case 4:
                    getFreeGoodsInfo();
                    break;
                case 5:
                    getUrgentGoodsInfo();
                    break;
            }
        }

    }

    /**
     * 获取正在拍卖的商品
     */
    private void getPaiGoodsInfo() {
        //开启线程读取数据
        new Thread() {
            @Override
            public void run() {
                GoodsInfoQueryHttpUtils.getNewGoodsInfoToList(GoodsListActivity.this,
                        getResources().getString(R.string.url_query_goods) + "?type=pai", handler);
            }
        }.start();
    }

    /**
     * 获取紧急出售的商品
     */
    private void getUrgentGoodsInfo() {
        //开启线程读取数据
        new Thread() {
            @Override
            public void run() {
                GoodsInfoQueryHttpUtils.getNewGoodsInfoToList(GoodsListActivity.this,
                        getResources().getString(R.string.url_query_goods) + "?type=new&flag=urgent", handler);
            }
        }.start();
    }

    /**
     * 获取免费的商品
     */
    private void getFreeGoodsInfo() {
        //开启线程读取数据
        new Thread() {
            @Override
            public void run() {
                GoodsInfoQueryHttpUtils.getNewGoodsInfoToList(GoodsListActivity.this,
                        getResources().getString(R.string.url_query_goods) + "?type=new&flag=free", handler);
            }
        }.start();
    }

    /**
     * 获取最热门的
     */
    private void getHotGoodsInfo() {
        //开启线程读取数据
        new Thread() {
            @Override
            public void run() {
                GoodsInfoQueryHttpUtils.getNewGoodsInfoToList(GoodsListActivity.this,
                        getResources().getString(R.string.url_query_goods) + "?type=new&flag=hot", handler);
            }
        }.start();
    }

    /**
     * 获取最新发布的商品
     */
    private void getNewGoodsInfo() {
        //开启线程读取数据
        new Thread() {
            @Override
            public void run() {
                GoodsInfoQueryHttpUtils.getNewGoodsInfoToList(GoodsListActivity.this,
                        getResources().getString(R.string.url_query_goods) + "?type=new&flag=all", handler);
            }
        }.start();
    }

    public void getAllGoodsInfo() {
        //开启线程读取数据
        new Thread() {
            @Override
            public void run() {
                GoodsInfoQueryHttpUtils.getGoodsInfoToList(GoodsListActivity.this,
                        getResources().getString(R.string.url_query_goods) + "?type=all&position=100", handler);
            }
        }.start();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        setContentView(R.layout.activity_goodslist);
        gv_goodsList = (GridView) findViewById(R.id.gv_goodslist);
        tv_actionBarTitle = (TextView) findViewById(R.id.tv_goodslist_custom_actionbar_title);
        iv_refresh = (ImageView) findViewById(R.id.iv_goodslist_refresh);
        et_search = (EditText) findViewById(R.id.et_goodslist_search);
        btn_search = (Button) findViewById(R.id.btn_goodslist_search);
        setTopTitle();
//        setAdapter();
        setListener();
        setAnimation();
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String select_after = et_search.getText().toString().trim();
                try {
                    select_after = URLEncoder.encode(select_after, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                final String select_befor = select_after;
                new Thread() {
                    @Override
                    public void run() {
                        GoodsInfoQueryHttpUtils.getGoodsInfoToList(GoodsListActivity.this,
                                getResources().getString(R.string.url_query_goods) + "?type=search&select=" + select_befor, handler);
                    }
                }.start();
            }
        });
        gv_goodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gotoPager(i);
            }
        });
//        iv_refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updateDate();
//            }
//        });
    }

    /**
     * 跳转到相应的页面
     *
     * @param i
     */
    private void gotoPager(int i) {
        Intent it = new Intent(this, GoodsDetailActivity.class);
        it.putExtra("gId", goodsList.get(i).getgId());
        startActivity(it);
    }

    /**
     * 设置顶部标题栏
     */
    private void setTopTitle() {
        mIntent = getIntent();
        String type = mIntent.getStringExtra("type");
        if ("new".equals(type)) {
            tv_actionBarTitle.setText("最新发布");
        } else if ("pai".equals(type)) {
            tv_actionBarTitle.setText("拍卖");
        } else if ("urgent".equals(type)) {
            tv_actionBarTitle.setText("紧急出售");
        } else if ("other".equals(type)) {
            int title = mIntent.getIntExtra("title", 0);
            tv_actionBarTitle.setText(menu_title[title]);
        } else if ("classify".equals(type)) {
            String title = mIntent.getStringExtra("title");
            tv_actionBarTitle.setText(title);
        } else if ("search".equals(type)) {
            tv_actionBarTitle.setText("搜索");
            getHotGoodsInfo();
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_detailinfo_actionbar);
            rl.setVisibility(View.GONE);
        }
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        gv_goodsList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return goodsList.size();
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
                if (view == null) {
                    view = View.inflate(GoodsListActivity.this, R.layout.gridview_item_main_showgoods, null);
                    ViewHodler vHolder = new ViewHodler(view);
                    view.setTag(vHolder);
                }
                ViewHodler vHolder = (ViewHodler) view.getTag();
                vHolder.setData(i);
                return view;
            }

            class ViewHodler {
                View item_view;
                ImageView iv_pic;
                TextView tv_oldPrice, tv_gPinkage, tv_nickName, tv_gTime, tv_gPrice, tv_gBrowCount, tv_gClassify, tv_gTitle;

                public ViewHodler(View item_view) {
                    this.item_view = item_view;
                    initViewItem();
                }

                /**
                 * 初始化视图
                 */
                private void initViewItem() {
                    iv_pic = (ImageView) item_view.findViewById(R.id.iv_home_gridview_gimg);
                    tv_oldPrice = (TextView) item_view.findViewById(R.id.tv_home_gridview_newgoods_oldprice);
                    tv_nickName = (TextView) item_view.findViewById(R.id.tv_home_gridview_nickname);
                    tv_gTime = (TextView) item_view.findViewById(R.id.tv_home_gridview_gtime);
                    tv_gPrice = (TextView) item_view.findViewById(R.id.tv_home_listview_pai_maxprice);
                    tv_gBrowCount = (TextView) item_view.findViewById(R.id.tv_home_listview_pai_address);
                    tv_gPinkage = (TextView) item_view.findViewById(R.id.tv_home_listview_pai_gpinkage);
                    tv_gClassify = (TextView) item_view.findViewById(R.id.tv_home_listview_pai_gclassify);
                    tv_gTitle = (TextView) item_view.findViewById(R.id.tv_home_listview_pai_gtitle);
                }

                /**
                 * 设置数据
                 * @param position
                 */
                public void setData(int position) {
                    Goods goods = goodsList.get(position);
                    if (goods.getgImgUrls().size() > 0) {
                        new BitmapUtils(GoodsListActivity.this).display(iv_pic, goods.getgImgUrls().get(0));
                    } else {
//                        new BitmapUtils(GoodsListActivity.this).display(iv_pic, R.mipmap.download_pic);
                        iv_pic.setBackgroundResource(R.mipmap.download_pic);
                    }
                    tv_oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    tv_oldPrice.setText("￥" + goods.getgOldPrice() + ".00");
                    if (goods.getgOldPrice() == 0) {
                        tv_oldPrice.setVisibility(View.INVISIBLE);
                    }

                    tv_nickName.setText(goods.getuNickName());

                    try {
                        String time = DateUtils.getTimeAfterFormat(goods.getgTime());
                        tv_gTime.setText(time + "");
                    } catch (ParseException e) {
                        e.printStackTrace();
                        ToastUtils.showInfo(GoodsListActivity.this, "日期解析出问题了");
                    }

//                    tv_gPrice.setText("￥" + goods.getgPrice() + ".00");
                    if (goods.getgPrice() == 0) {
                        tv_gPrice.setText("免费");
                        tv_gPrice.setTextColor(Color.RED);
                        tv_oldPrice.setVisibility(View.GONE);
                    } else {
                        tv_gPrice.setText("￥" + goods.getgPrice() + ".00");
                    }
                    tv_gBrowCount.setText("浏览量：" + goods.getgBrowCount());

                    tv_gPinkage.setText(goods.isPinkage() ? "包邮" : "自取");
                    tv_gClassify.setText(ClassifyArrays.titls[goods.getgClassify()] + "");

                    tv_gTitle.setText(goods.getgTitle());
                }
            }
        });
    }

    /**
     * 返回按钮
     *
     * @param view
     */
    public void onBack(View view) {
        finish();
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
