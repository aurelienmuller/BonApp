package com.bonapp.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bonapp.app.bonapp.R;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;


//container for the login fragment
public class LoginActivityFB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity_fb);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    //if user is not logged in, he is unable to press the back button
    @Override
    public void onBackPressed() {
        if(AccessToken.getCurrentAccessToken() == null) {
        } else {
            super.onBackPressed();
        }
    }
}
