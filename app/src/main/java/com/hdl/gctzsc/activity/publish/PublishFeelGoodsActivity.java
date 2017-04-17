package com.hdl.gctzsc.activity.publish;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.customview.ToastUtils;
import com.hdl.gctzsc.customview.XCRoundRectIv;
import com.hdl.gctzsc.modle.GoodsPublishInfo;
import com.hdl.gctzsc.utils.Base64Coder;
import com.hdl.gctzsc.utils.ObjectUtils;
import com.hdl.gctzsc.utils.ProgressDialogUtils;
import com.hdl.gctzsc.utils.SysApplication;
import com.hdl.gctzsc.utils.ZoomBitmap;
import com.lidroid.xutils.BitmapUtils;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PublishFeelGoodsActivity extends Activity {
    private XCRoundRectIv iv_addimg;
    private ArrayList<String> imgPath_list;
    private LinearLayout ll_look_img;
    private HorizontalScrollView hsv_show_img;
    private ImageView iv_addimg_plus;
    private RadioGroup rg;
    private Spinner sp_classify;
    private String[] classify_big = {"校园代步", "手机", "电脑", "电脑耗材", "数码", "电器", "运动健身", "衣物伞帽", "图书教材", "校园网", "生活娱乐", "其他"};
    private String[] address = {"樱花苑1栋", "樱花苑2栋", "樱花苑3栋", "樱花苑4栋", "翠竹苑1栋", "翠竹苑2栋",
            "翠竹苑3栋", "丹桂苑1栋", "丹桂苑2栋", "丹桂苑3栋", "丹桂苑4栋", "玉兰苑1栋", "玉兰苑2栋", "玉兰苑3栋",
            "D17", "公租房", "其他(校内)", "其他(校外)"};
    private Spinner sp_address;
    private GoodsPublishInfo gInfo;
    private int classify_name = 0;
    private String address_name = address[0];
    private EditText et_title;
    private EditText et_desc;
    private String gId = "";
    private static final int WHAT_PUBLISH_NEW_GOODS_FINISHED = 1000;
    private static final int WHAT_CONNECTION_FAILED = 1001;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pDialog.closeProgressDialog();
            switch (msg.what) {
                case WHAT_PUBLISH_NEW_GOODS_FINISHED:
                    try {
                        String line = HttpStreamUtils.getPublishInfo();
//                        Log.e("mylog", "请求结果为：" + line);
                        String[] lines = line.split("=-=");
                        gId = lines[1];
                        goStartUploadImg();
                        if ("successful".equals(lines[0])) {
                            ToastUtils.showInfo(PublishFeelGoodsActivity.this, "发布成功");
                            finish();
                        } else if ("failed".equals(lines[0])) {
                            ToastUtils.showInfo(PublishFeelGoodsActivity.this, "发布失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtils.showInfo(PublishFeelGoodsActivity.this, "数据加载失败");
                    }
                    break;
                case WHAT_CONNECTION_FAILED:
                    ToastUtils.showInfo(PublishFeelGoodsActivity.this, "网络链接失败");
                    break;
            }
        }
    };

    /**
     * 开启上传图片
     */
    private void goStartUploadImg() {
        new Thread() {
            @Override
            public void run() {
                for (String path : imgPath_list) {
                    if (!"deleted".equals(path)) {
                        upload(path);
                    }
                }
            }
        }.start();

    }

    private ProgressDialogUtils pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        initView();
    }

    // 上传图片
    public void upload(String imgPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        float wight = bitmap.getWidth();
        float height = bitmap.getHeight();
        Bitmap upbitmap = ZoomBitmap.zoomImage(bitmap,(wight*3) / 4, (height*3) / 4);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        upbitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] b = stream.toByteArray();
        // 将图片流以字符串形式存储下来
        String file = new String(Base64Coder.encodeLines(b));
        HttpClient client = new DefaultHttpClient();
        // 设置上传参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("file", file));
        formparams.add(new BasicNameValuePair("username", getSharedPreferences("login", MODE_PRIVATE).getString("username", "1")));
        formparams.add(new BasicNameValuePair("gId", gId));
        HttpPost post = new HttpPost(getResources().getString(R.string.url_upload_img));
        UrlEncodedFormEntity entity;
        try {
            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.addHeader("Accept",
                    "text/javascript, text/html, application/xml, text/xml");
            post.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
            post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
            post.addHeader("Connection", "Keep-Alive");
            post.addHeader("Cache-Control", "no-cache");
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity e = response.getEntity();
            System.out.println(EntityUtils.toString(e));
            if (200 == response.getStatusLine().getStatusCode()) {
                System.out.println("上传完成");
            } else {
                System.out.println("上传失败");
            }
            client.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 完成按钮
     *
     * @param view
     */
    public void onFinshed(View view) {
        int countImgs = 0;
        if (imgPath_list != null && imgPath_list.size() > 0) {
            for (String path : imgPath_list) {
                if (!"deleted".equals(path)) {
                    countImgs++;
                }
            }
        }
        if (countImgs == 0) {
            ToastUtils.showInfo(this, "还没有商品的图片喔,快去拍一个吧");
            return;
        }
        gInfo = new GoodsPublishInfo();
        gInfo.setgImgs_list(imgPath_list);//图片

        String title = et_title.getText().toString();
        if (TextUtils.isEmpty(title)) {
            ToastUtils.showInfo(this, "标题不能为空");
            et_title.requestFocus();
            et_title.setCursorVisible(true);
            return;
        }
        gInfo.setgTitle(title);


        String desc = et_desc.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            ToastUtils.showInfo(this, "不能没有商品简介喔");
            et_desc.requestFocus();
            et_desc.setCursorVisible(true);
            return;
        }
        gInfo.setgDesc(desc);

        if (rg.getCheckedRadioButtonId() == R.id.rb_publish_fell_pinkcage) {
            gInfo.setPinkage(true);
        } else if (rg.getCheckedRadioButtonId() == R.id.rb_publish_fell_unpinkcage) {
            gInfo.setPinkage(false);
        }
        gInfo.setgClassify(classify_name);
        gInfo.setgPublishType("feel");
        gInfo.setgAddress(address_name);
        final Map<String, Object> params = ObjectUtils.objToMap(gInfo);
        params.put("username", getSharedPreferences("login", MODE_PRIVATE).getString("username", ""));

//        Log.e("FeelGoodsActivity", params.toString());
        pDialog.showProgressDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    HttpStreamUtils.sendHttpRequestByPost(handler, WHAT_PUBLISH_NEW_GOODS_FINISHED, getResources().getString(R.string.url_publish_goods), params);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(WHAT_CONNECTION_FAILED);
                }
            }
        }.start();
    }

    /**
     * 返回主页面
     *
     * @param view
     */
    public void onBack(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("放弃发布的商品");
        builder.setMessage("您真的放弃本次商品发布吗？");
        builder.setPositiveButton("放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("继续编辑发布", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        setContentView(R.layout.activity_pulish_feel);
        hsv_show_img = (HorizontalScrollView) findViewById(R.id.hsv_publish_feel_showimg);
        ll_look_img = (LinearLayout) findViewById(R.id.ll_publish_feel_look_img);
        iv_addimg = (XCRoundRectIv) findViewById(R.id.iv_publish_feel_addimg);
        iv_addimg_plus = (ImageView) findViewById(R.id.iv_publish_feel_addimg_plus);
        et_title = (EditText) findViewById(R.id.et_publish_feel_title);
        et_desc = (EditText) findViewById(R.id.et_publish_feel_desc);


        rg = (RadioGroup) findViewById(R.id.rg_publish_feel_pinckage);
        rg.check(R.id.rb_publish_fell_unpinkcage);

        sp_classify = (Spinner) findViewById(R.id.sp_publish_feel_classify);
        sp_classify.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, classify_big));


        sp_address = (Spinner) findViewById(R.id.sp_publish_feel_address);
        sp_address.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, address));
        pDialog = new ProgressDialogUtils(this, "发布免费商品", "发送中......");
        setListener();
    }

    /**
     * 用户点击返回键的处理效果
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("放弃发布的商品");
            builder.setMessage("您真的放弃本次商品发布吗？");
            builder.setPositiveButton("放弃", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("继续编辑发布", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return true;
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        iv_addimg_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPhoto(60);
            }
        });
        iv_addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPhoto(50);
            }
        });

        sp_classify.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classify_name = position;
//                classify_name = classify_big[position];
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sp_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("mylog", "position=" + i + " address[position]=" + address[i]);
                address_name = address[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * 开始拍照（跳转到拍照页面）
     */
    private void startPhoto(int requestCode) {
        Intent it = new Intent(PublishFeelGoodsActivity.this, MyCamera.class);
        it.putExtra("requestCode", requestCode);
        startActivityForResult(it, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
//            Log.e("mylog", "接收到了-----------------------");
            if (requestCode == 50 && resultCode == 55) {
//                Log.e("mylog", "数据回传了,接收到的图片是55555555555555");
                imgPath_list = data.getStringArrayListExtra("listPath");
            }
            if (requestCode == 60 && resultCode == 66) {
//                Log.e("mylog", "数据回传了,接收到的图片是6666666666");
                imgPath_list.addAll(data.getStringArrayListExtra("listPath"));
            }
            showImg();
        }
    }

    /**
     * 预览图片
     *
     * @param path
     */
    private void previewImg(String path) {
        Dialog dialog = new Dialog(PublishFeelGoodsActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageView iv = new ImageView(PublishFeelGoodsActivity.this);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        iv.setImageBitmap(bitmap);
        dialog.setContentView(iv);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//设置背景为全透明
        dialog.show();
    }

    /**
     * 显示图片
     */
    private void showImg() {
        ll_look_img.removeAllViews();
        if (hsv_show_img.getVisibility() != View.VISIBLE) {
            hsv_show_img.setVisibility(View.VISIBLE);
        }
        iv_addimg.setVisibility(View.GONE);
        for (int i = 0; i < imgPath_list.size(); i++) {
            if (!imgPath_list.get(i).equals("deleted")) {
                final View view_img = View.inflate(this, R.layout.imgview_item_look_delete, null);
                final ImageView iv_img = (ImageView) view_img.findViewById(R.id.iv_publish_img);
                iv_img.setTag(i);
                BitmapUtils utils = new BitmapUtils(PublishFeelGoodsActivity.this);
                utils.display(iv_img, imgPath_list.get(i));

                iv_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int key = Integer.parseInt(iv_img.getTag().toString());
                        String path = imgPath_list.get(key);
                        previewImg(path);
                    }
                });

                final ImageView iv_deleteBtn = (ImageView) view_img.findViewById(R.id.iv_publish_deletebtn);
                iv_deleteBtn.setTag(i);

                iv_deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Log.e("mylog", "删除图片了");
                        ll_look_img.removeView(view_img);
                        int key = Integer.parseInt(iv_deleteBtn.getTag().toString());
//                    imgPath_list.remove(key);
                        imgPath_list.set(key, "deleted");
                    }
                });

                ll_look_img.addView(view_img);
            }
        }
    }
}
