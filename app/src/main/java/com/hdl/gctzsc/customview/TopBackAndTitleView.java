package com.hdl.gctzsc.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hdl.gctzsc.R;

/**
 * 自定义顶部带返回键+标题栏的视图
 * Created by HDL on 2016/4/14.
 */
public class TopBackAndTitleView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";//命名空间
    private ImageView iv_back;
    private String title;
    private TextView tv_title;

    public TopBackAndTitleView(Context context) {
        this(context, null, 0);
    }

    public TopBackAndTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBackAndTitleView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        title = attrs.getAttributeValue(NAMESPACE, "text");
        initView();
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).finish();
            }
        });
    }

    /**
     * 初始化布局
     */
    private void initView() {
        View.inflate(getContext(), R.layout.custom_topbar_titleandback, this);
        iv_back = (ImageView) findViewById(R.id.iv_topbar_back);
        tv_title = (TextView) findViewById(R.id.tv_topbar_title);
    }

    /**
     * 设置标题
     *
     * @param text
     */
    public void setText(String text) {
        tv_title.setText(text);
    }
}
