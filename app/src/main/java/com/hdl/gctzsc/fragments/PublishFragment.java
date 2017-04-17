package com.hdl.gctzsc.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.activity.publish.PublishFeelGoodsActivity;
import com.hdl.gctzsc.activity.publish.PublishNewGoodsActivity;
import com.hdl.gctzsc.activity.publish.PublishPaiGoodsActivity;
import com.hdl.gctzsc.customview.ToastUtils;
import com.hdl.gctzsc.modle.RequestGoodsInfo;
import com.hdl.gctzsc.utils.ObjectUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

public class PublishFragment extends Fragment {
    private RequestGoodsInfo gInfo;
    private View view_container;
    private LinearLayout lPublishNewGoods;
    private LinearLayout lPublishPaiGoods;
    private LinearLayout lPublishFeelGoods;
    private LinearLayout lPublishRequestGoods;
    private static final int WHAT_PUBLISH_REQUEST_FINISHED = 1000;
    private static final int WHAT_CONNECTION_FAILED = 1001;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_PUBLISH_REQUEST_FINISHED:
                    try {
                        String info = HttpStreamUtils.getRequestInfo();
//                        Log.e("mylog", "结果为" + info);
                        if ("successful".equals(info)) {
                            ToastUtils.showInfo(getActivity(), "发布成功");
                        } else if ("failed".equals(info)) {
                            ToastUtils.showInfo(getActivity(), "发布失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtils.showInfo(getActivity(), "数据访问出错");
                    }
                    break;
                case WHAT_CONNECTION_FAILED:
                    ToastUtils.showInfo(getActivity(), "网络链接失败");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_container = inflater.inflate(R.layout.fragment_publish, null);
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
        TextView tv_title = (TextView) view_container.findViewById(R.id.tv_classify_custom_actionbar_title);
        tv_title.setText("发布");
        lPublishNewGoods = (LinearLayout) view_container.findViewById(R.id.ll_publish_newgoods);
        lPublishPaiGoods = (LinearLayout) view_container.findViewById(R.id.ll_publish_pai);
        lPublishFeelGoods = (LinearLayout) view_container.findViewById(R.id.ll_publish_feel);
        lPublishRequestGoods = (LinearLayout) view_container.findViewById(R.id.ll_publish_request);
        setListener();
    }

    private void setListener() {
        lPublishFeelGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PublishFeelGoodsActivity.class));
            }
        });
        lPublishPaiGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PublishPaiGoodsActivity.class));
            }
        });
        lPublishNewGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e("mylog", "点击了");
                startActivity(new Intent(getActivity(), PublishNewGoodsActivity.class));
            }
        });
        lPublishRequestGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPublishRequestGoodsDialog();
            }
        });
    }

    /**
     * 显示求购商品对话
     */
    private void showPublishRequestGoodsDialog() {
        gInfo = new RequestGoodsInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("求购");
        View view = View.inflate(getActivity(), R.layout.dialog_publish_requestgoods, null);
        final EditText et_title = (EditText) view.findViewById(R.id.et_publish_request_title);
        final EditText et_content = (EditText) view.findViewById(R.id.et_publish_request_content);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gInfo.setrTitle(et_title.getText().toString().trim() + "");
                gInfo.setrContent(et_content.getText().toString().trim() + "");
                final Map<String, Object> params = ObjectUtils.objToMap(gInfo);
                params.put("username", getActivity().getSharedPreferences("login", Context.MODE_PRIVATE).getString("username", ""));
                params.put("publishType","new");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            HttpStreamUtils.sendHttpRequestByPost(handler, WHAT_PUBLISH_REQUEST_FINISHED, getResources().getString(R.string.url_publish_requestgoods), params);
                        } catch (IOException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(WHAT_CONNECTION_FAILED);
                        }
                    }
                }.start();
//                Log.e("mylog", gInfo.toString());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
}
