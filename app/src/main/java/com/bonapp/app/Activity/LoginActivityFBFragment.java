package com.bonapp.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bonapp.app.Application.MyApplication;
import com.bonapp.app.DataAccess.RequestQueueSingleton;
import com.bonapp.app.Model.User;
import com.bonapp.app.bonapp.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */

//Fragment that contains the logic behind facebook login & checks if it's the user first connection.
//In that case, the user is added to the database
public class LoginActivityFBFragment extends Fragment {

    private TextView mTextDetails;
    private RequestQueue requestQueue;

    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {


        //Connection logic
        private ProfileTracker mProfileTracker;
        @Override
        public void onSuccess(LoginResult loginResult) {

                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {

                        mProfileTracker.stopTracking();
                        Profile.setCurrentProfile(profile2);

                        MyApplication.setFbUserId(profile2.getId().substring(profile2.getId().length() / 2, profile2.getId().length()));

                        //checks if it's the user's first connection (= he's not in database)
                        checkUserInDatabase();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

                    }
                };
                mProfileTracker.startTracking();

        }

        @Override
        public void onCancel() {
            Log.v("facebook - onCancel", "cancelled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.v("facebook - onError", error.getMessage());
            Toast.makeText(getActivity(), getActivity().getString(R.string.noconnection), Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }


    public LoginActivityFBFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextDetails = (TextView) view.findViewById((R.id.text_details));

        final LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);

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


    //GET request to check if users is in database. 404 error means url is bad => user does not exists
    //if 404 error => add user to database
    public void checkUserInDatabase() {
        requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();

        StringRequest userRequest = new StringRequest(Request.Method.GET, MyApplication.getBonAppUsersUrl() + MyApplication.getFbUserId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("checkUserInDatabase", "user existe");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            addUserToDatabase();
                            break;

                        default:
                            if (error.getCause() instanceof java.net.UnknownHostException) {
                                Toast.makeText(getActivity(), R.string.noconnection, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), R.string.problemesurvenu, Toast.LENGTH_LONG).show();
                            }
                            break;
                    }
                }
            }
        });
        requestQueue.add(userRequest);
    }

    //add the user to the database and displays a welcome message when added
    public void addUserToDatabase() {
        requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();

        User newUser = new User(Integer.parseInt(MyApplication.getFbUserId()), Profile.getCurrentProfile().getName());
        Gson gson = new Gson();
        String newUserJson = gson.toJson(newUser);

        JsonObjectRequest newUserRequest = new JsonObjectRequest(Request.Method.POST, MyApplication.getBonAppUsersUrl(), newUserJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getActivity(), R.string.firstconnection + Profile.getCurrentProfile().getFirstName(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getCause() instanceof java.net.UnknownHostException) {
                    Toast.makeText(getActivity(), R.string.noconnection, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), R.string.problemesurvenu, Toast.LENGTH_LONG).show();
                }
            }
        });
        requestQueue.add(newUserRequest);
    }

}
