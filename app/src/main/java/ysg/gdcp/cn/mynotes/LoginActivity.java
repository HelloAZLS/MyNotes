package ysg.gdcp.cn.mynotes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import ysg.gdcp.cn.mynotes.data.MyUser;


/**
 * Created by Administrator on 2017/5/12 18:22.
 *
 * @author ysg
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etCode;
    private  boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "c022a6dff9fe2252de5e21742f40533c");
        //保存登录信息
        SharedPreferences sp =getSharedPreferences("NoteConfig",Activity.MODE_PRIVATE);
        sp.edit().putBoolean("isLogin",true).commit();
        isLogin = sp.getBoolean("isLogin", false);
        //检验是否自动登录
        if (isLogin){
            Toast.makeText(this, "开始自动登录", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            return;
        }
        initViews();

    }

    private void initViews() {
        findViewById(R.id.btn_Code).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        etUsername = (EditText) findViewById(R.id.et_username);

        etCode = (EditText) findViewById(R.id.et_code);
    }

    @Override
    public void onClick(View v) {
        String phoneNum = null;
        switch (v.getId()) {
            case R.id.btn_Code:
                //获取验证码
               Toast.makeText(this, "获取验证码", Toast.LENGTH_SHORT).show();
                phoneNum = etUsername.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)){
                    Toast.makeText(this, "电话号码为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
                BmobSMS.requestSMSCode(phoneNum,"科技", new QueryListener<Integer>() {

                    @Override
                    public void done(Integer smsId,BmobException ex) {
                        if(ex==null){//验证码发送成功
                            Toast.makeText(LoginActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btn_login:
                //开始登录
                final String mCode = etCode.getText().toString().trim();
                phoneNum = etUsername.getText().toString().trim();
                if (TextUtils .isEmpty(mCode)&&TextUtils.isEmpty(phoneNum)){
                    Toast.makeText(this, "验证码或手机号错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobUser.signOrLoginByMobilePhone(phoneNum, mCode, new LogInListener<MyUser>() {

                    @Override
                    public void done(MyUser user, BmobException e) {
                        if(user!=null){
                            SharedPreferences sp =getSharedPreferences("NoteConfig",Activity.MODE_PRIVATE);
                            sp.edit().putBoolean("isLogin",true).commit();
                            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            Log.i("你好","验证码:"+mCode+",错误信息"+e.getMessage()+"");
                        }
                    }
                });

                break;
        }
    }
}
