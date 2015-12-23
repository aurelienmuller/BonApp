package com.bonapp.app.Application;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;


//Class which contains application scoped constants and variables
//such as api urls, keys, userId
public class MyApplication extends Application {

    private static String fbUserId;
    private static final String f2fKey = "*";
    private static final String food2forkUrl = "http://food2fork.com/api/search?key=" + f2fKey + "&q=";
    private static final String bonAppUrl = "*";
    private static final String bonAppUsersUrl = bonAppUrl + "users/";
    private static final String bonAppUserfavoritesUrl = bonAppUrl + "userfavorites/";
    private static final String bonAppRecipesUrl = bonAppUrl + "recipes/";

    private static MyApplication mInstance;



    @Override
    public void onCreate(){
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());

        //facebook key hash
        //try { PackageInfo info = getPackageManager().getPackageInfo( this.getPackageName(), PackageManager.GET_SIGNATURES); for (Signature signature : info.signatures) { MessageDigest md = MessageDigest.getInstance("SHA"); md.update(signature.toByteArray()); Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT)); } } catch (PackageManager.NameNotFoundException e) {e.printStackTrace(); } catch (NoSuchAlgorithmException e) {e.printStackTrace(); }

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
