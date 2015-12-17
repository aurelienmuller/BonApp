package com.bonapp.app.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bonapp.app.Application.MyApplication;
import com.bonapp.app.DataAccess.RequestQueueSingleton;
import com.bonapp.app.Model.Recipe;
import com.bonapp.app.bonapp.R;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


//Main activity. 1 button goes to the search page, the other makes a Volley request to get the current user's favorites
//checks if the user is logged in in onStart
public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        profile = Profile.getCurrentProfile();

        final ImageButton searchButton = (ImageButton) findViewById(R.id.imageButtonSearch);
        final ImageButton favoriteButton = (ImageButton) findViewById(R.id.imageButtonFav);

        makeProgressDialog();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchRecipeIntent = new Intent(MainActivity.this, SearchRecipeActivity.class);
                MainActivity.this.startActivity(searchRecipeIntent);
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFavorites();
            }
        });

    }

    //makes the progress dialog which shows up when loading favorites
    public void makeProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.chargement);
        progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.chargementmsg));
        progressDialog.setCancelable(false);
    }

    //Volley request on the current user to display his favorite recipes
    private void getFavorites() {
        progressDialog.show();

        String userIdString = MyApplication.getFbUserId();//profile.getId().substring(profile.getId().length() / 2, profile.getId().length());

        RequestQueue requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.GET, MyApplication.getBonAppUsersUrl() + userIdString, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                parseJSONResponse(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if(error.getCause() instanceof java.net.UnknownHostException) {
                    Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.noconnection), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.requesterror), Toast.LENGTH_SHORT).show();
                }

            }

        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }


    //parse the JSON response from volley to a list of recipes
    private void parseJSONResponse(String response) {

        if(response == null || response.length() == 0) {
            return;
        }

        try {
            ArrayList<Recipe> listRecipes = new ArrayList<>();
            Gson gson = new Gson();
            JSONObject responseJSON = new JSONObject(response);

            if (responseJSON.has("userfavorites")) {
                JSONArray arrayFavorites = responseJSON.getJSONArray("userfavorites");

                for(int i = 0; i < arrayFavorites.length(); i++) {
                    JSONObject recipeFav = arrayFavorites.getJSONObject(i);
                    JSONObject currentRecipeFav = recipeFav.getJSONObject("recipe");

                    Recipe recipe = gson.fromJson(currentRecipeFav.toString(), Recipe.class);

                    listRecipes.add(recipe);
                }

                if(!listRecipes.isEmpty()) {
                    Intent i = new Intent(MainActivity.this, ListRecipeActivity.class);
                    i.putExtra("listRecipes", listRecipes);
                    i.putExtra("parent", "MainActivity");

                    progressDialog.dismiss();

                    startActivity(i);
                    listRecipes.clear();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, this.getString(R.string.listeVide), Toast.LENGTH_LONG).show();
                }

            }
        } catch (JSONException e) {
            Log.v("parseJSONResponse", e.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.itemUserID:
                startActivity(new Intent(MainActivity.this, LoginActivityFB.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    //Checks if the user is logged in. If not, starts the login activity.
    //If yes, saves his id.
    @Override
    protected  void onStart() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        super.onStart();

            if (AccessToken.getCurrentAccessToken() == null) {
                startActivity(new Intent(MainActivity.this, LoginActivityFB.class));
                profile = Profile.getCurrentProfile();
            } else {
                profile = Profile.getCurrentProfile();
                MyApplication.setFbUserId(profile.getId().substring(profile.getId().length() / 2, profile.getId().length()));
            }


    }

}
