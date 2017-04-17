package com.hdl.gctzsc.activity.mine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.customview.ToastUtils;
import com.hdl.gctzsc.modle.Users;
import com.hdl.gctzsc.utils.MD5Encoder;
import com.hdl.gctzsc.utils.NetUtils;
import com.hdl.gctzsc.utils.SysApplication;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册用户
 */
public class RegisterActivity extends AppCompatActivity {
    private String phone;
    private Intent mIntent;
    private EditText et_repwd;
    private EditText et_pwd;
    private EditText et_sNo;
    private RadioGroup rg_sex;
    private EditText et_nickName;
    private ProgressDialog pDialog;
    private Users users;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        mIntent = getIntent();
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_register);
        //昵称
        et_nickName = (EditText) findViewById(R.id.et_register_nickname);
        phone = mIntent.getStringExtra("phone");
        et_nickName.setText(phone);//号码回显

        et_sNo = (EditText) findViewById(R.id.et_register_sno);//学号

        et_repwd = (EditText) findViewById(R.id.et_register_repwd);//再次确认密码

        et_pwd = (EditText) findViewById(R.id.et_register_pwd);//密码
        rg_sex = (RadioGroup) findViewById(R.id.rg_register_sex);
        rg_sex.check(R.id.rb_register_man);//默认选中第一个

        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setTitle("用户注册");
        pDialog.setMessage("正在注册中......");

    }

    /**
     * 显示正在注册中
     */
    private void showRegistingDialog() {
        pDialog.show();
    }

    /**
     * 显示正在登陆中
     */
    private void closeRegistingDialog() {
        pDialog.dismiss();
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
     * 注册按钮
     *
     * @param view
     */
    public void onRegister(View view) {
        String nickName = et_nickName.getText().toString().trim();//昵称
        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.showInfo(this, "昵称不能为空");
            et_nickName.requestFocus();
            et_nickName.setCursorVisible(true);
            return;
        }

        String pwd = et_pwd.getText().toString().trim();//密码
        String re_pwd = et_repwd.getText().toString().trim();//错误密码
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

        String sNo = et_sNo.getText().toString().trim();//学号
        if (sNo.length() != 14) {
            et_sNo.requestFocus();
            et_sNo.setCursorVisible(true);
            ToastUtils.showInfo(this, "学号输入有误(请查看是否有14位)");
            return;
        }
        if (!NetUtils.isConnected(mContext)) {
            ToastUtils.showInfo(this, "没有网络哦");
            return;
        }
        if (!NetUtils.isConnected(mContext)) {
            ToastUtils.showInfo(this, "没有网络哦");
            return;
        }

        boolean isMan = true;
        if (R.id.rb_register_man != rg_sex.getCheckedRadioButtonId()) {
            isMan = false;
        }
        users = new Users();
        users.setuNo(sNo);
        users.setUsername(nickName);
        users.setPassword(MD5Encoder.fiveMD5Encode(pwd));
        users.setMan(isMan);
        users.setMobilePhoneNumber(phone);
        showRegistingDialog();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //注意：不能用save方法进行注册
                users.signUp(new SaveListener<Users>() {
                    @Override
                    public void done(Users user, BmobException e) {
                        if (e == null) {
                            Toast.makeText(mContext, "用户注册成功", Toast.LENGTH_SHORT).show();
                            //跳转到登录界面
                            ToastUtils.showInfo(RegisterActivity.this, "注册成功");
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(mContext, "用户已经存在", Toast.LENGTH_SHORT).show();
                        }
                        closeRegistingDialog();
                    }
                });
            }
        }).start();
    }


    /**
     * 立即登陆按钮
     *
     * @param view
     */
    public void onLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
