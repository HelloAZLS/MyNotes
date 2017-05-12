package ysg.gdcp.cn.mynotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/12 18:22.
 *
 * @author ysg
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        switch (v.getId()) {
            case R.id.btn_Code:
//                Toast.makeText(this, "验证码", Toast.LENGTH_SHORT).show();
                String phoneNum = etUsername.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)){
                    Toast.makeText(this, "电话号码为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
//                BmobSMS.requestSMSCode(phoneNum);
                break;
            case R.id.btn_login:
                Toast.makeText(this, "登录", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
