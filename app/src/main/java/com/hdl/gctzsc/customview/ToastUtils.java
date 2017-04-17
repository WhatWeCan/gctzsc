package com.hdl.gctzsc.customview;

import android.content.Context;
import android.widget.Toast;

/**
 * 弹出土司工具类
 * Created by HDL on 2016/4/12.
 */
public class ToastUtils {
    public static void showInfo(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
