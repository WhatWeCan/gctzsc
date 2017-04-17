package com.hdl.gctzsc.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.customview.ToastUtils;
import com.hdl.gctzsc.modle.Users;
import com.hdl.gctzsc.utils.MD5Encoder;
import com.hdl.gctzsc.utils.NetUtils;
import com.hdl.gctzsc.utils.ProgressDialogUtils;
import com.hdl.gctzsc.utils.SysApplication;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 更改密码
 */
public class UpdatePwdActivity extends AppCompatActivity {

    private EditText et_userName;
    private Intent mIntent;
    private EditText et_pwd;
    private EditText et_repwd;
    private ProgressDialogUtils progressDialog;
    private String phone;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SysApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        mIntent = getIntent();
        mContext = this;
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        setContentView(R.layout.activity_update_pwd);
        progressDialog = new ProgressDialogUtils(this, "更改密码", "更改密码中......");
        et_userName = (EditText) findViewById(R.id.et_update_username);
        phone = mIntent.getStringExtra("phone");
        et_userName.setText(phone);

        et_pwd = (EditText) findViewById(R.id.et_update_pwd);
        et_repwd = (EditText) findViewById(R.id.et_update_repwd);
    }


    /**
     * 更改密码
     *
     * @param view
     */
    public void onUpdate(View view) {
        String pwd = et_pwd.getText().toString().trim();//密码
        String re_pwd = et_repwd.getText().toString().trim();//确认密码
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(re_pwd)) {
            ToastUtils.showInfo(this, "密码不能为空");
            et_pwd.requestFocus();
            et_pwd.setCursorVisible(true);
            return;
        } else if (pwd.length() < 6 || re_pwd.length() < 6) {
            et_pwd.requestFocus();
            et_pwd.setCursorVisible(true);
            ToastUtils.showInfo(this, "密码的长度必须大于等于6位,小于等于20位");
            return;
        } else if (!pwd.equals(re_pwd)) {
            et_repwd.requestFocus();
            et_repwd.setCursorVisible(true);
            ToastUtils.showInfo(this, "两次输入的密码不一致,请重新输入");
            return;
        }
        if (!NetUtils.isConnected(mContext)) {
            ToastUtils.showInfo(this, "没有网络哦");
            return;
        }
        final String md5_pwd = MD5Encoder.fiveMD5Encode(pwd);
        progressDialog.showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //1、查询到该用户
                Users currentUser = BmobUser.getCurrentUser(Users.class);//获取到当前的用户
                //2、修改当前用户的密码
                BmobUser newUser = new BmobUser();
                newUser.setPassword(md5_pwd);
                newUser.update(currentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(mContext, "密码修改成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdatePwdActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(mContext, "密码修改失败咯", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }

    /**
     * 更改密码
     *
     * @param view
     */
    public void onCancle(View view) {
        finish();
    }

    /**
     * 返回键
     *
     * @param view
     */
    public void onBack(View view) {
        finish();
    }
}
