package com.hdl.gctzsc.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hdl.gctzsc.R;

/**
 * 自定义的设置页面的子项
 * Created by HDL on 2016/4/4.
 */
public class SettingItem extends RelativeLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";//命名空间
    private TextView tv_title;//标题
    private TextView tv_desc;//内容描述
    private CheckBox cb_checked;//选中
    private String title;//通过属性【用户设置的】获取的标题
    private String desc_off;//通过属性【用户设置的】获取的关时的描述
    private String desc_on;//通过属性【用户设置的】获取的开时的描述

    public SettingItem(Context context) {
        this(context, null, 0);

    }

    public SettingItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        title = attrs.getAttributeValue(NAMESPACE, "Title");
        desc_off = attrs.getAttributeValue(NAMESPACE, "desc_off");
        desc_on = attrs.getAttributeValue(NAMESPACE, "desc_on");
        initView();
        //系统自已来设置标题和描述
        setTitle(title);
        if (cb_checked.isChecked()) {
            setDesc(desc_on);
        } else {
            setDesc(desc_off);
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {
        View.inflate(getContext(), R.layout.setting_item, this);//将布局文件setting_item填充给settingitem
        tv_title = (TextView) findViewById(R.id.tv_setting_title);
        tv_desc = (TextView) findViewById(R.id.tv_setting_desc);
        cb_checked = (CheckBox) findViewById(R.id.cb_setting_checked);
    }

    /**
     * 设置状态描述信息
     *
     * @param desc
     */
    public void setDesc(String desc) {
        tv_desc.setText(desc);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 设置是否选中
     *
     * @param isChecked
     */
    public void setChecked(boolean isChecked) {
        cb_checked.setChecked(isChecked);
        if (isChecked) {
            setDesc(desc_on);
        } else {
            setDesc(desc_off);
        }
    }

    public boolean getChecked() {
        return cb_checked.isChecked();
    }
}
