package com.bonapp.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.bonapp.app.DataAccess.RequestQueueSingleton;
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
    private static final String f2fKey = "217401dcb0a4ad131cd118a528ce6cb4";
    private static final String food2forkUrl = "http://food2fork.com/api/search?key=" + f2fKey + "&q=";
    private static final String bonAppUrl = "http://bonapp2.azurewebsites.net/api/";
    private static final String bonAppUsersUrl = bonAppUrl + "users/";
    private static final String bonAppUserfavoritesUrl = bonAppUrl + "userfavorites/";
    private static final String bonAppRecipesUrl = bonAppUrl + "recipes/";


    private static MyApplication mInstance;

    private boolean isLoggedin;



    @Override
    public void onCreate(){
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());


        try { PackageInfo info = getPackageManager().getPackageInfo( this.getPackageName(), PackageManager.GET_SIGNATURES); for (Signature signature : info.signatures) { MessageDigest md = MessageDigest.getInstance("SHA"); md.update(signature.toByteArray()); Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT)); } } catch (PackageManager.NameNotFoundException e) {e.printStackTrace(); } catch (NoSuchAlgorithmException e) {e.printStackTrace(); }


        mInstance = this;
    }


    public static MyApplication getInstance(){
        return mInstance;
    }

    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }

    public static String getFood2forkUrl() {
        return food2forkUrl;
    }

    public static String getBonAppUsersUrl() {
        return bonAppUsersUrl;
    }

    public static String getBonAppUserfavoritesUrl() {
        return bonAppUserfavoritesUrl;
    }

    public static String getBonAppRecipesUrl() {
        return bonAppRecipesUrl;
    }


    public static void setFbUserId(String id) {
        fbUserId = id;
    }

    public static String getFbUserId() { return fbUserId; }
}
