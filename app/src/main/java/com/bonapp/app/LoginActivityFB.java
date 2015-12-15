package com.bonapp.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bonapp.app.bonapp.R;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

public class LoginActivityFB extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity_fb);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


    }

    @Override
    public void onBackPressed() {
        if(AccessToken.getCurrentAccessToken() == null) {
        } else {
            super.onBackPressed();
        }

    }

    /* @Override
    public void onStop() {
         super.onStop();
         editor.putString("id", "");
         editor.putString("username", "");
         editor.commit();

     }*/

}
