package com.hdl.gctzsc.activity.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.customview.ToastUtils;
import com.hdl.gctzsc.fragments.MineFragment;
import com.hdl.gctzsc.modle.Users;
import com.hdl.gctzsc.utils.DateUtils;
import com.hdl.gctzsc.utils.ProgressDialogUtils;
import com.hdl.gctzsc.utils.SysApplication;

import org.json.JSONException;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class UserInfoActivity extends Activity {

    private static final int WHAT_CONNECTION_FAILED = 101;
    private static final int WHAT_SEND_UPDATE_REQUEST_FINISHED = 102;
    private ProgressDialogUtils progressDialogUtils;
    private static final int WHAT_SEND_REQUEST_FINISHED = 100;
    private Users users;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressDialogUtils.closeProgressDialog();
            switch (msg.what) {
                case WHAT_SEND_REQUEST_FINISHED:
                    try {
                        users = HttpStreamUtils.getUserInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtils.showInfo(UserInfoActivity.this, "获取数据有误,请重试");
                    }
                    setViewData();
                    break;
                case WHAT_CONNECTION_FAILED:
                    ToastUtils.showInfo(UserInfoActivity.this, "访问服务器失败了,请检查网络");
                    break;
                case WHAT_SEND_UPDATE_REQUEST_FINISHED:
                    try {
                        boolean isSuccessful = HttpStreamUtils.isUpdateNickNameInfo();
                        if (isSuccessful) {
                            ToastUtils.showInfo(UserInfoActivity.this, "修改成功");
                            getSharedPreferences("login", MODE_PRIVATE).edit().putString("nickname", nickName_input).commit();
                            Handler handler = MineFragment.handler;
                            Message message = handler.obtainMessage();
                            message.obj = nickName_input;
                            message.what = 1000;
                            handler.sendMessage(message);
                            getUserInfo();//刷新一下当前页面
                        } else {
                            ToastUtils.showInfo(UserInfoActivity.this, "修改失败了");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtils.showInfo(UserInfoActivity.this, "获取数据有误,请重试");
                    }
                    break;
            }
        }
    };
    private SharedPreferences login_sp;
    private String username;
    private ImageView iv_sex;
    private TextView tv_phone;
    private TextView tv_sno;
    private TextView tv_nickName;
    private TextView tv_time;
    private String nickName_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUserInfo();
        login_sp = getSharedPreferences("login", MODE_PRIVATE);
        username = login_sp.getString("username", "");
        SysApplication.getInstance().addActivity(this);
        initView();
    }

    /**
     * 初始化数据
     */
    private void initView() {
        setContentView(R.layout.activity_userinfo);
        tv_nickName = (TextView) findViewById(R.id.tv_userinfo_nickname);
        tv_sno = (TextView) findViewById(R.id.tv_userinfo_sno);
        tv_phone = (TextView) findViewById(R.id.tv_userinfo_phone);
        iv_sex = (ImageView) findViewById(R.id.iv_userinfo_sex);
        tv_time = (TextView) findViewById(R.id.tv_userinfo_time);
    }

    /**
     * 设置视图的数据
     */
    private void setViewData() {
        tv_nickName.setText(users.getuNickName());
        tv_sno.setText(users.getuNo());
        tv_phone.setText(users.getuPhone());
        iv_sex.setImageResource(users.isMan() ? R.mipmap.man : R.mipmap.woman);
        try {
            tv_time.setText(DateUtils.getTimeAfterFormat(users.getuTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        progressDialogUtils = new ProgressDialogUtils(this, "获取用户信息", "获取中......");
        progressDialogUtils.showProgressDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    HttpStreamUtils.sendHttpRequest(handler, WHAT_SEND_REQUEST_FINISHED,
                            getResources().getString(R.string.url_get_userinfo) + "?type=get&username=" + username);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(WHAT_CONNECTION_FAILED);
                }
            }
        }.start();
    }

    /**
     * 注销了
     *
     * @param view
     */
    public void onLogout(View view) {
        getSharedPreferences("login", MODE_PRIVATE).edit().putString("pwd", "").commit();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * 修改密码
     *
     * @param view
     */
    public void onUpdatePwd(View view) {
        startSMSCheck();
    }

    /**
     * 编辑昵称
     *
     * @param view
     */
    public void onEditNickName(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改昵称");
        final EditText et_nickName = new EditText(this);
        et_nickName.setSingleLine();
        et_nickName.setHint("请输入新昵称");
        et_nickName.setBackgroundResource(R.drawable.shape_input);
        builder.setView(et_nickName);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                nickName_input = et_nickName.getText().toString();
                if (nickName_input.length() > 14) {
                    nickName_input = et_nickName.getText().toString().substring(0, 13);
                }
                final String newNickName = nickName_input;
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            String nNickName = URLEncoder.encode(newNickName, "UTF-8");
//                            Log.e("mylog", "-------------" + nNickName);
                            HttpStreamUtils.sendHttpRequest(handler, WHAT_SEND_UPDATE_REQUEST_FINISHED,
                                    getResources().getString(R.string.url_get_userinfo) + "?type=update&username=" + username + "&newnickname=" + nNickName);
                        } catch (IOException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(WHAT_CONNECTION_FAILED);
                        }
                    }
                }.start();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * 开启短信验证
     */
    private void startSMSCheck() {
        //打开注册页面
        RegisterPage registerPage = new RegisterPage();

        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //依然走短信验证
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    // 提交用户信息
                    registerUser(phone);
                }
            }
        });
        registerPage.show(this);
    }

    /**
     * 验证成功之后,跳转到修改密码页面
     *
     * @param phone
     */
    private void registerUser(String phone) {
        Intent it = new Intent(this, UpdatePwdActivity.class);
        it.putExtra("phone", phone);
        startActivity(it);
        finish();
    }
}
