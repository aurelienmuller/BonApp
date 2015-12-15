package com.bonapp.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.Profile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by aurelienmuller on 2/12/15.
 */
public class MyApplication extends Application {

    SharedPreferences sharedPreferences;
    private static String fbUserId;
    Profile profile;


    private static MyApplication mInstance;

    private boolean isLoggedin;

    @Override
    public void onCreate(){
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        //profile = Profile.getCurrentProfile();
        //fbUserId = profile.getId().substring(profile.getId().length()/ 2, profile.getId().length());
        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


        try { PackageInfo info = getPackageManager().getPackageInfo( this.getPackageName(), PackageManager.GET_SIGNATURES); for (Signature signature : info.signatures) { MessageDigest md = MessageDigest.getInstance("SHA"); md.update(signature.toByteArray()); Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT)); } } catch (PackageManager.NameNotFoundException e) {e.printStackTrace(); } catch (NoSuchAlgorithmException e) {e.printStackTrace(); }

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

    public static void setFbUserId(String id) {
        fbUserId = id;
    }

    public static String getFbUserId() { return fbUserId; }
}
