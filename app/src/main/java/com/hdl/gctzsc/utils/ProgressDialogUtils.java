package com.hdl.gctzsc.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.pdf.PdfDocument;

/**
 * 进度条对话框
 * Created by HDL on 2016/4/18.
 */
public class ProgressDialogUtils {

    ProgressDialog pDialog;

    public ProgressDialogUtils(Context context, String title, String message) {
        pDialog = new ProgressDialog(context);
        pDialog.setTitle(title);
        pDialog.setMessage(message);
    }

    /**
     * 显示对话框
     */
    public void showProgressDialog() {
        pDialog.show();
    }

    /**
     * 关闭对话框
     */
    public void closeProgressDialog() {
        pDialog.dismiss();
    }
}
