package com.example.quentin.bonapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import DataAccess.RequestQueueSingleton;
import Model.Recipe;
import Model.User;
import Model.Userfavorite;

public class MainActivity extends AppCompatActivity {
    //private RequestQueue requestQueue;
    private ArrayList<Recipe> listRecipes;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listRecipes = new ArrayList<>();

        user = "1";

        final ImageButton searchButton = (ImageButton) findViewById(R.id.imageButtonSearch);
        final ImageButton favoriteButton = (ImageButton) findViewById(R.id.imageButtonFav);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchRecipeIntent = new Intent(MainActivity.this, SearchActivity.class);
                MainActivity.this.startActivity(searchRecipeIntent);
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();

                StringRequest request = new StringRequest(Request.Method.GET,"http://bonapp.azurewebsites.net/api/users/1", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        parseJSONResponse(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.requesterror) + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                });

                request.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(request);
            }
        });

    }

    private void parseJSONResponse(String response) {
        
        if(response == null || response.length() == 0) {

            return;
        }

        try {

            JSONObject responseJSON = new JSONObject(response);

            if (responseJSON.has("userfavorites")) {

                JSONArray arrayFavorites = responseJSON.getJSONArray("userfavorites");

                for(int i = 0; i < arrayFavorites.length(); i++) {

                    JSONObject recipeFav = arrayFavorites.getJSONObject(i);
                    JSONObject currentRecipeFav = recipeFav.getJSONObject("recipe");


                    String recipe_id = currentRecipeFav.getString("recipe_id");
                    String image_url = currentRecipeFav.getString("image_url");
                    String source_url = currentRecipeFav.getString("source_url");
                    String f2f_url = currentRecipeFav.getString("f2f_url");
                    String title = currentRecipeFav.getString("title");
                    String publisher = currentRecipeFav.getString("publisher");
                    String publisher_url = currentRecipeFav.getString("publisher_url");
                    Double social_rank = currentRecipeFav.getDouble("social_rank");

                    Recipe recipe = new Recipe(publisher, f2f_url, title, source_url, recipe_id, image_url, social_rank, publisher_url);

                    listRecipes.add(recipe);


                }

                if(!listRecipes.isEmpty()) {
                    Intent i = new Intent(MainActivity.this, ListRecipeActivity.class);
                    i.putExtra("listRecipes", listRecipes);
                    startActivity(i);
                    listRecipes.clear();
                } else {
                    Toast.makeText(MainActivity.this, this.getString(R.string.listeVide), Toast.LENGTH_LONG).show();
                }

            }
        } catch (JSONException e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.itemFavorisID:
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                return true;
            case R.id.itemUserID:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
}
