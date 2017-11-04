package com.lemonxq_laplace.pregnantmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Button loginButton = (Button)findViewById(R.id.login);
        Button signupButton = (Button)findViewById(R.id.signup);
        Button visitorButton = (Button)findViewById(R.id.visitor);

        loginButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SignIn.this,MainActivity.class);
                startActivity(intent);
            }
        });

        visitorButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SignIn.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
