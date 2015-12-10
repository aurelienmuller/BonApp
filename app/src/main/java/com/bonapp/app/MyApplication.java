package com.bonapp.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by aurelienmuller on 2/12/15.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    private boolean isLoggedin;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }

    public static MyApplication getInstance(){
        return mInstance;
    }

    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }

    public void setIsLoggedIn(boolean value) {
        this.isLoggedin = value;
    }

    public boolean getIsLoggedIn() {
        return this.isLoggedin;
    }
}
