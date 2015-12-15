package com.bonapp.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bonapp.app.bonapp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFBFragment extends Fragment {

    //String id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private TextView mTextDetails;

    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {



        private ProfileTracker mProfileTracker;
        @Override
        public void onSuccess(LoginResult loginResult) {

            //if(Profile.getCurrentProfile() == null) {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        //Log.v("facebook - profile1111", profile2.getName());
                        //editor.putString("id", profile2.getId().substring(profile2.getId().length()/2, profile2.getId().length()));
                        //editor.putString("username", profile2.getName());
                        //editor.commit();
                        //sharedPreferences.edit().putString("id", profile2.getId());
                        //sharedPreferences.edit().commit();
                        //MyApplication.setUserId(profile2.getId());
                        //Toast.makeText(getActivity(), /*MyApplication.fbUserId*/ sharedPreferences.getString("id", "") +  sharedPreferences.getString("username", ""), Toast.LENGTH_LONG).show();
                        Log.v("onCurrentProfileChanged", "hello");
                        mProfileTracker.stopTracking();
                        Profile.setCurrentProfile(profile2);

                        MyApplication.setFbUserId(profile2.getId().substring(profile2.getId().length() / 2, profile2.getId().length()));
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

                    }
                };
                mProfileTracker.startTracking();

            /*} else {
                Profile profile = Profile.getCurrentProfile();
                Log.v("facebook - profile22222", profile.getName());
                editor.putString("id", profile.getId().substring(profile.getId().length()/2, profile.getId().length()));
                editor.putString("username", profile.getName());

                editor.commit();
                //sharedPreferences.edit().putString("id", profile.getId());
                //sharedPreferences.edit().commit();
                //MyApplication.setUserId(profile.getId());
                Toast.makeText(getActivity(), /*MyApplication.fbUserId sharedPreferences.getString("id", ""), Toast.LENGTH_LONG).show();
            //}
            /*Profile profile = Profile.getCurrentProfile();

            MyApplication.setUserId(profile.getId());

            displayWelcomeMessage(profile);*/

            //getActivity().finish();
        }

        @Override
        public void onCancel() {
            Log.v("facebook - onCancel", "cancelled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.v("facebook - onError", error.getMessage());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
    }

    public void displayWelcomeMessage(Profile profile) {
        if (profile != null) {
            mTextDetails.setText("Welcome " + profile.getName());


        }
    }

    public LoginActivityFBFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mCallbackManager = CallbackManager.Factory.create();

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        /*if(loggedIn) {
            Log.v("onCreate", "hello");

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextDetails = (TextView) view.findViewById((R.id.text_details));

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile, email"));
        loginButton.setFragment(this);

        loginButton.registerCallback(mCallbackManager, mCallback);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
