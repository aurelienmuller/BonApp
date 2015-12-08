package com.example.quentin.bonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import Model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button bLogin, bLogout;
    EditText etUserName, etPassword, etMail;
    TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etMail = (EditText) findViewById(R.id.input_email);
        etUserName = (EditText) findViewById(R.id.input_username);
        etPassword = (EditText) findViewById(R.id.input_password);
        bLogin = (Button) findViewById(R.id.btn_login);
        bLogout = (Button) findViewById(R.id.btn_logout);
        tvRegisterLink = (TextView) findViewById(R.id.link_register);

        bLogin.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_login:
                User user = new User();
                //sessionManager.storeUserData(user);
                //sessionManager.setLoggedInUser(true);


                break;

            case R.id.btn_logout:
                //sessionManager.clearUserData();
                //sessionManager.setLoggedInUser(false);

                break;

            case R.id.link_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
