package com.lemonxq_laplace.pregnantmonitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lemonxq_laplace.pregnantmonitor.R;
import com.lemonxq_laplace.pregnantmonitor.Util.CommonRequest;
import com.lemonxq_laplace.pregnantmonitor.Util.CommonResponse;
import com.lemonxq_laplace.pregnantmonitor.Util.Consts;
import com.lemonxq_laplace.pregnantmonitor.Util.HttpUtil;
import com.lemonxq_laplace.pregnantmonitor.Util.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


public class SignIn extends AppCompatActivity {

    private Button loginBtn;
    private Button registerBtn;
    private Button visitorBtn;
    private EditText accountText;
    private EditText passwordText;
    private String account = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        loginBtn = findViewById(R.id.login);
        registerBtn = findViewById(R.id.signup);
        visitorBtn = findViewById(R.id.visitor);
        accountText = findViewById(R.id.account);
        passwordText = findViewById(R.id.password);

        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        registerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        visitorBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, MainActivity.class);
                startActivity(intent);
            }
        });

        accountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                account = accountText.getText().toString();
                System.out.println("before:" + account);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                account = accountText.getText().toString();
                System.out.println("on:" + account);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                account = accountText.getText().toString();
                System.out.println("after:" + account);
            }
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = passwordText.getText().toString();
                System.out.println("before:" + password);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = passwordText.getText().toString();
                System.out.println("on:" + password);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = passwordText.getText().toString();
                System.out.println("after:" + password);
            }
        });
    }

    // GET方式Login
//    private void Login() {
//        // 构造GET参数列表
//        LinkedHashMap<String, String> params = new LinkedHashMap<>();
//        params.clear();
//        // 前端参数校验，防SQL注入
//        account = Util.StringHandle(account);
//        password = Util.StringHandle(password);
//        // 填充参数
//        params.put("account", account);
//        params.put("pwd", password);
//        System.out.println("account:" + account + " pwd:" + password);
//
//        // 带参数GET请求
//        HttpUtil.sendRequest(Consts.URL_Login, params, new okhttp3.Callback() {
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String resCode = response.body().string();
//                String resMsg = "";
//                System.out.println("resCode:" + resCode);
//
//                if (resCode.equals(Consts.ERRORCODE_NULL))
//                    resMsg = "账号或密码" + Consts.ERRORMSG_NULL;
//                else if (resCode.equals(Consts.ERRORCODE_ACCOUNTNOTEXIST))
//                    resMsg = Consts.ERRORMSG_ACCOUNTNOTEXIST;
//                else if (resCode.equals(Consts.ERRORCODE_PWD))
//                    resMsg = Consts.ERRORMSG_PWD;
//                else if (resCode.equals(Consts.SUCCESSCODE_LOGIN)) {
//                    resMsg = Consts.SUCCESSMSG_LOGIN;
//                    showResponse(resMsg);
//                    Intent intent = new Intent(SignIn.this, MainActivity.class);
//                    startActivity(intent);
//                }
//
//                showResponse(resMsg);
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }

    /**
     *  POST方式Login
     */
    private void Login() {
        // 创建请求体对象
        CommonRequest request = new CommonRequest();

        // 前端参数校验，防SQL注入
        account = Util.StringHandle(account);
        password = Util.StringHandle(password);

        System.out.println(account.equals(null));
        System.out.println(account.equals(""));
        Log.d("ACCOUNT",account);
        Log.d("PWD",password);

        // 填充参数
        request.addRequestParam("account",account);
        request.addRequestParam("pwd",password);

        // POST请求
        HttpUtil.sendPost(Consts.URL_Login, request.getJsonStr(), new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CommonResponse res = new CommonResponse(response.body().string());
                String resCode = res.getResCode();
                String resMsg = res.getResMsg();
                // 登录成功
                if (resCode.equals(Consts.SUCCESSCODE_LOGIN)) {
                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    startActivity(intent);
                }
                showResponse(resMsg);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     *  POST方式Register
     */
    private void Register() {
        // 创建请求体对象
        CommonRequest request = new CommonRequest();

        // 前端参数校验，防SQL注入
        account = Util.StringHandle(account);
        password = Util.StringHandle(password);

        // 填充参数
        request.addRequestParam("account",account);
        request.addRequestParam("pwd",password);

        // POST请求
        HttpUtil.sendPost(Consts.URL_Register, request.getJsonStr(), new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CommonResponse res = new CommonResponse(response.body().string());
                String resMsg = res.getResMsg();

                // 显示注册结果
                showResponse(resMsg);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

//    private void Register() {
//        // 构造POST参数列表
//        LinkedHashMap<String, String> params = new LinkedHashMap<>();
//        params.clear();
//        // 前端参数校验，防SQL注入
//        account = Util.StringHandle(account);
//        password = Util.StringHandle(password);
//        // 填充参数
//        params.put("account", account);
//        params.put("pwd", password);
//        System.out.println("account:" + account + " pwd:" + password);
//
//        HttpUtil.sendPost(Consts.URL_Register, params, new okhttp3.Callback() {
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String resCode = response.body().string();
//                String resMsg = "";
//                System.out.println("resCode:" + resCode);
//
//                if (resCode.equals(Consts.ERRORCODE_NULL))
//                    resMsg = Consts.ERRORMSG_NULL;
//                else if (resCode.equals(Consts.ERRORCODE_ACCOUNTEXIST))
//                    resMsg = Consts.ERRORMSG_ACCOUNTEXIST;
//                else if (resCode.equals(Consts.ERRORCODE_INSERT))
//                    resMsg = Consts.ERRORMSG_INSERT;
//                else if (resCode.equals(Consts.SUCCESSCODE_REGISTER))
//                    resMsg = Consts.SUCCESSMSG_REGISTER;
//
//                showResponse(resMsg);
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }

    private void showResponse(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SignIn.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
