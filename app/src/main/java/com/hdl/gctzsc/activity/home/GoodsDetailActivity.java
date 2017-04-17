package com.hdl.gctzsc.activity.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.customview.ToastUtils;
import com.hdl.gctzsc.modle.CommentInfo;
import com.hdl.gctzsc.utils.ClassifyArrays;
import com.hdl.gctzsc.utils.DateUtils;
import com.hdl.gctzsc.utils.ProgressDialogUtils;
import com.hdl.gctzsc.utils.SysApplication;
import com.lidroid.xutils.BitmapUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GoodsDetailActivity extends Activity {

    private static final int WHAT_SEND_COMMENT_FAILED = 209;
    private static final int WHAT_ADD_BROWCOUNT_FAIL = 210;
    private static final int WHAT_ADD_BROWCOUNT = 211;
    private static final int WHAT_OFFER = 212;
    private int[] goods_imgs = {R.mipmap.gi1, R.mipmap.gi2, R.mipmap.gi3, R.mipmap.gi4};
    private LinearLayout ll_container_imgs;
    private LinearLayout ll_comment;
    private Intent mIntent;
    private static int gId;
    public static final int WHAT_DATA_PARSE_FAILED = 203;
    public static final int WHAT_GET_GOODS_DETIAL_FINISHED = 206;
    private static final int WHAT_GOODS_UNEXISTED = 207;//商品不存在
    public static final int WHAT_GET_DATA_FAILED = 201;
    public static final int WHAT_SEND_COMMENT = 208;
    private String phone = "";
    private boolean isFirst = true;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.closeProgressDialog();
            switch (msg.what) {
                case WHAT_GET_GOODS_DETIAL_FINISHED:
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        animation.cancel();//停止刷新
                        ToastUtils.showInfo(GoodsDetailActivity.this, "刷新完成");
                    }
                    goods = (PaiGoods) msg.obj;
                    Bundle data = msg.getData();
                    commentList = data.getParcelableArrayList("commentList");
                    phone = data.getString("phone");
                    showDataToView();
                    showGoodsImgs();
                    break;
                case WHAT_GOODS_UNEXISTED:
                    ToastUtils.showInfo(GoodsDetailActivity.this, "改商品已经被卖出或已下架");
                    break;
                case WHAT_DATA_PARSE_FAILED://json解析出错了
                    ToastUtils.showInfo(GoodsDetailActivity.this, "数据解析出错了");
                    break;
                case WHAT_GET_DATA_FAILED://连接出错了
                    ToastUtils.showInfo(GoodsDetailActivity.this, "网络异常");
                    break;
                case WHAT_SEND_COMMENT:
                    updateData();
                    break;
                case WHAT_SEND_COMMENT_FAILED:
                    ToastUtils.showInfo(GoodsDetailActivity.this, "评论失败，请查看网络设置");
                    break;
                case WHAT_ADD_BROWCOUNT_FAIL:
                    ToastUtils.showInfo(GoodsDetailActivity.this, "请查看网络设置");
                    break;
                case WHAT_ADD_BROWCOUNT:
                    break;
                case WHAT_OFFER:
                    break;
            }
        }
    };
    private TextView tv_nickName;
    private TextView tv_browCount;
    private TextView tv_classify;
    private TextView tv_commentCount;
    private TextView tv_desc;
    private TextView tv_nice;
    private TextView tv_oldPrice;
    private TextView tv_pinkage;
    private TextView tv_price;
    private TextView tv_time;
    private TextView tv_address;
    private ImageView iv_refresh;
    private RotateAnimation animation;
    private ArrayList<CommentInfo> commentList;
    private TextView tv_gurgent;
    private TextView tv_maxPrice;
    private Button btn_givePrice;

    /**
     * 显示数据到布局中
     */
    private void showDataToView() {
        for (CommentInfo info : commentList) {
//            Log.e("mylog", "得到的评论数据为:" + info);
        }
//        Log.e("mylog", goods.toString());
        tv_nickName.setText(goods.getuNickName());
        try {
            tv_time.setText(DateUtils.getTimeAfterFormat(goods.getgTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_address.setText(goods.getgAddress());

        if (goods.isUrgent()) {
            tv_gurgent.setVisibility(View.VISIBLE);
        } else {
            tv_gurgent.setVisibility(View.GONE);
        }
        if (goods.getgPublishType() == 0) {
            btn_givePrice.setVisibility(View.GONE);
            tv_maxPrice.setVisibility(View.GONE);
            tv_price.setText("￥" + goods.getgPrice());
            if (goods.getgPrice() == 0) {
                tv_price.setText("免费");
                tv_price.setTextColor(Color.RED);
                tv_oldPrice.setVisibility(View.GONE);
            } else {
                tv_price.setText("￥" + goods.getgPrice() + ".00");
            }
        } else if (goods.getgPublishType() == 1) {
            tv_maxPrice.setVisibility(View.VISIBLE);
            btn_givePrice.setVisibility(View.VISIBLE);
            tv_price.setText("底价:￥" + goods.getgPrice());
        } else {
            btn_givePrice.setVisibility(View.GONE);
            tv_maxPrice.setVisibility(View.GONE);
            tv_price.setText("免费");
            tv_price.setTextColor(Color.RED);
        }

        tv_oldPrice.setText("原价: ￥" + goods.getgOldPrice());
        if (goods.getgOldPrice() == 0) {
            tv_oldPrice.setVisibility(View.INVISIBLE);
        }
        tv_maxPrice.setText("Max:" + goods.getgMaxPrice());
        tv_pinkage.setText(goods.isPinkage() ? "送货上门" : "自取");
        tv_browCount.setText("浏览:" + goods.getgBrowCount());
        tv_nice.setText("赞:" + goods.getgNice());
        tv_classify.setText(ClassifyArrays.titls[goods.getgClassify()] + "");
        tv_desc.setText("    " + goods.getgDesc());
        tv_commentCount.setText("评论(" + goods.getgCommentCount() + ")");
        showComment();
    }

    private ProgressDialogUtils mProgressDialog;
    private PaiGoods goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        mIntent = getIntent();
        gId = mIntent.getIntExtra("gId", 1);
        mProgressDialog = new ProgressDialogUtils(this, "加载数据", "获取数据中......");
        updateData();
        initView();
        addBrowCount();
    }

    /**
     * 预览图片
     *
     * @param tag
     */
    private void previewImg(int tag) {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageView iv = new ImageView(this);
        iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        new BitmapUtils(this).display(iv, goods.getgImgUrls().get(tag));
        dialog.setContentView(iv);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//设置背景为全透明
        dialog.show();
    }

    /**
     * 增加浏览量
     */
    private void addBrowCount() {
        new Thread() {
            @Override
            public void run() {
                try {
                    HttpStreamUtils.sendHttpRequest(handler, WHAT_ADD_BROWCOUNT, getResources().getString(R.string.url_browcount_add) + "?gid=" + gId);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(WHAT_ADD_BROWCOUNT_FAIL);
                }
            }
        }.start();
    }


    /**
     * 更新数据
     */
    private void updateData() {
        getGoodsDetail();
    }

    /**
     * 获取商品详细信息
     */
    private void getGoodsDetail() {
        mProgressDialog.showProgressDialog();
        new Thread() {
            @Override
            public void run() {
//                Log.e("请求地址----------", getResources().getString(R.string.url_query_goodsdetail) + "?gId=" + gId + "&type=pai");
                GoodsInfoQueryHttpUtils.getGoodsDetailToList(GoodsDetailActivity.this,
                        getResources().getString(R.string.url_query_goodsdetail) + "?gId=" + gId + "&type=pai", handler);
            }

        }.start();
    }

    /**
     * 初始化数据
     */
    private void initView() {
        setContentView(R.layout.activity_goods_detailinfo);
        ll_container_imgs = (LinearLayout) findViewById(R.id.ll_detailinfo_imgcontainer);
        ll_comment = (LinearLayout) findViewById(R.id.ll_detail_comment);
        tv_nickName = (TextView) findViewById(R.id.tv_detail_nickname);
        tv_browCount = (TextView) findViewById(R.id.tv_detail_gbrowcount);
        tv_classify = (TextView) findViewById(R.id.tv_detail_gclaaify);
        tv_commentCount = (TextView) findViewById(R.id.tv_detail_gcommentcount);
        tv_desc = (TextView) findViewById(R.id.tv_detail_gdesc);
        tv_nice = (TextView) findViewById(R.id.tv_detail_gnice);
        tv_oldPrice = (TextView) findViewById(R.id.tv_detail_godlprice);
        tv_oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_pinkage = (TextView) findViewById(R.id.tv_detail_gpinkage);
        tv_price = (TextView) findViewById(R.id.tv_detail_gprice);
        tv_time = (TextView) findViewById(R.id.tv_detail_gtime);
        tv_address = (TextView) findViewById(R.id.tv_detail_address);
        iv_refresh = (ImageView) findViewById(R.id.iv_detail_fresh);
        tv_gurgent = (TextView) findViewById(R.id.tv_detail_gurgent);
        tv_maxPrice = (TextView) findViewById(R.id.tv_detail_gmaxprice);
        btn_givePrice = (Button) findViewById(R.id.btn_detail_giveprice);
        setAnimation();
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
     * 显示评论
     */
    private void showComment() {
        ll_comment.removeAllViews();
        if (commentList.size() == 0) {
            TextView tv_show = new TextView(this);
            tv_show.setText("暂无评论,快来成为第一个评论的人吧");
            ll_comment.addView(tv_show);
        } else {
            for (int i = 0; i < commentList.size(); i++) {
                View view = View.inflate(this, R.layout.listview_item_detailinfo_comment, null);
                TextView tv_time = (TextView) view.findViewById(R.id.tv_comment_time);
                CommentInfo info = commentList.get(i);
                try {
                    tv_time.setText(DateUtils.getTimeAfterFormat(info.getcTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    ToastUtils.showInfo(this, "发布时间解析出错,请重试");
                }
                TextView tv_nickName = (TextView) view.findViewById(R.id.tv_comment_nickname);
                tv_nickName.setText(info.getuNickName());
                TextView tv_content = (TextView) view.findViewById(R.id.tv_comment_content);
                tv_content.setText("    " + info.getcContent());
                if (info.getcContent().contains("【出价】￥")) {
                    tv_content.setTextColor(Color.RED);
                }
                ll_comment.addView(view);
            }
        }
    }

    /**
     * 显示商品图片
     */
    private void showGoodsImgs() {
        ll_container_imgs.removeAllViews();
        List<String> urls = goods.getgImgUrls();
        if (urls.size() > 0) {
            for (int i = 0; i < urls.size(); i++) {
                final ImageView iv = new ImageView(this);
                iv.setTag(i);
//                iv.setImageResource(goods_imgs[i]);
                new BitmapUtils(this).display(iv, urls.get(i));
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int key = Integer.parseInt(iv.getTag().toString());
                        previewImg(key);
                    }
                });
                WindowManager wManager = getWindowManager();
                Display display = wManager.getDefaultDisplay();
                int height = display.getHeight();
                int width = display.getWidth();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / 2, height / 4);
                lp.setMargins(2, 2, 2, 2);
                iv.setLayoutParams(lp);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                ll_container_imgs.addView(iv);
            }
        }
    }

    /**
     * 立即评论按钮
     *
     * @param view
     */
    public void onComment(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("评论");
        final EditText et_comment = new EditText(this);
        et_comment.setBackgroundResource(R.drawable.shape_input);
        et_comment.setLines(5);
        et_comment.setGravity(Gravity.TOP);
        et_comment.setBackgroundResource(R.mipmap.et_comment_bg);
        builder.setView(et_comment);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String content = et_comment.getText().toString().trim();
                try {
                    content = URLEncoder.encode(content, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                final String url = getResources().getString(R.string.url_send_comment) + "?username=" +
                        getSharedPreferences("login", MODE_PRIVATE).getString("username", "") +
                        "&content=" + content + "&gid=" + gId;
//                    Log.e("mylog", "评论的请求地址为：" + url);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            HttpStreamUtils.sendHttpRequest(handler, WHAT_SEND_COMMENT, url);
                        } catch (IOException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(WHAT_SEND_COMMENT_FAILED);
                        }
                    }
                }.start();


                ToastUtils.showInfo(GoodsDetailActivity.this, "评论成功了");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ToastUtils.showInfo(GoodsDetailActivity.this, "取消评论");
            }
        });
        AlertDialog commentDialog = builder.create();
        commentDialog.show();
    }

    /**
     * 联系卖家
     *
     * @param view
     */
    public void onCall(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_call_seller);
        TextView tv_tell = (TextView) dialog.findViewById(R.id.tv_detailinfo_dialog_tellseller);
        tv_tell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //拨打电话
                Intent intent = new Intent();// 实例化意图对象
                intent.setAction(Intent.ACTION_CALL);// 为意图对象设置动作
                intent.setData(Uri.parse("tel:" + phone));// 为意图对象设置内容
                startActivity(intent);// 执行动作
                dialog.dismiss();
            }
        });
        TextView tv_sms = (TextView) dialog.findViewById(R.id.tv_detailinfo_dialog_smsseller);
        tv_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送短信
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
                sendIntent.putExtra("sms_body", "您好! 我在‘贵财跳蚤市场’上看到你的【" + goods.getgTitle()
                        + "(" + ClassifyArrays.titls[goods.getgClassify()] + ")，价格：￥" + goods.getgPrice() + "】，我有购买的意向，请问现在有时间否？");
                startActivity(sendIntent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 刷新
     *
     * @param view
     */
    public void onRefresh(View view) {
        updateData();
        iv_refresh.startAnimation(animation);//开始旋转
    }

    /**
     * 出价
     *
     * @param view
     */
    public void onGivePrice(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View givePrice = View.inflate(this, R.layout.dialog_detailinfo_giveprice, null);
        builder.setTitle("出价（底价为:￥" + goods.getgPrice() + "）");
        final EditText et_comment = (EditText) givePrice.findViewById(R.id.et_detailinfo_giveprice);
        builder.setView(givePrice);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String content = "【出价】￥" + et_comment.getText().toString().trim();
                try {
                    content = URLEncoder.encode(content, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String userName = getSharedPreferences("login", MODE_PRIVATE).getString("username", "");

                final String url = getResources().getString(R.string.url_send_comment) + "?username=" +
                        userName + "&content=" + content + "&gid=" + gId;
                final String url_offer = getResources().getString(R.string.url_offer) + "?username=" +
                        userName + "&price=" + et_comment.getText().toString().trim() + "&gid=" + gId;
//                    Log.e("mylog", "评论的请求地址为：" + url);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            HttpStreamUtils.sendHttpRequest(handler, WHAT_OFFER, url_offer);
                            HttpStreamUtils.sendHttpRequest(handler, WHAT_SEND_COMMENT, url);
                        } catch (IOException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(WHAT_SEND_COMMENT_FAILED);
                        }
                    }
                }.start();


                ToastUtils.showInfo(GoodsDetailActivity.this, "出价成功");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog commentDialog = builder.create();
        commentDialog.show();
    }

    /**
     * 返回
     */
    public void onBack(View view) {
        finish();
    }
}
