package com.bonapp.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bonapp.app.bonapp.R;
import com.facebook.Profile;
import com.google.gson.Gson;

import org.json.JSONObject;

import DataAccess.RequestQueueSingleton;
import Model.Recipe;
import Model.Userfavorite;


public class RecipeActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    RequestQueue requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();
    String recipeJson;
    String userFavJson;
    Recipe recipeToFavorite;
    SharedPreferences sharedPreferences;
    Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        profile = Profile.getCurrentProfile();

        webView = (WebView) findViewById(R.id.webViewRecipe);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportMultipleWindows(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                getWindow().setTitle(title); //Set Activity tile to page title.
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        Recipe recipe = (Recipe) (this.getIntent().getSerializableExtra("recipe"));

        webView.loadUrl(recipe.getSource_url());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.recipemenu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.itemFavorisID:
                sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                String userIdString = MyApplication.getFbUserId();
                int userIdInt = Integer.parseInt(userIdString);
                recipeToFavorite = (Recipe) (this.getIntent().getSerializableExtra(("recipe")));
                Userfavorite newUserFav = new Userfavorite(recipeToFavorite.getRecipe_id() + userIdString, userIdInt, recipeToFavorite.getRecipe_id());
                Gson gson = new Gson();
                recipeJson = gson.toJson(recipeToFavorite);
                userFavJson = gson.toJson(newUserFav);

                addToRecipes();


                return true;
            case R.id.itemUserID:
                startActivity(new Intent(RecipeActivity.this, LoginActivityFB.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void addToRecipes() {
        JsonObjectRequest requestRecipe = new JsonObjectRequest(Request.Method.POST, "http://bonapp2.azurewebsites.net/api/recipes/", recipeJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(RecipeActivity.this, "Recette ajoutée aux favoris", Toast.LENGTH_LONG).show();
                addToUserFavorites();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 400:
                            Toast.makeText(RecipeActivity.this, response.data.toString(), Toast.LENGTH_LONG).show();
                            break;
                        case 409:
                            Toast.makeText(RecipeActivity.this, "Recette déjà dans les favoris", Toast.LENGTH_LONG).show();
                            break;
                    }
                    //Additional cases
                }
                //Toast.makeText(RecipeActivity.this, "Recipes : " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestRecipe.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(requestRecipe);
    }

    public void addToUserFavorites() {
        JsonObjectRequest requestUserFav = new JsonObjectRequest(Request.Method.POST, "http://bonapp2.azurewebsites.net/api/userfavorites/", userFavJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(RecipeActivity.this, "Recette ajoutée aux favoris", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                deleteFromRecipes();
                //Toast.makeText(RecipeActivity.this, "Userfavorites : " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestUserFav.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(requestUserFav);
    }

    public void deleteFromRecipes() {
        StringRequest requestRecipe = new StringRequest(Request.Method.DELETE, "http://bonapp2.azurewebsites.net/api/recipes/" + recipeToFavorite.getRecipe_id(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RecipeActivity.this, "Recette supprimée des favoris", Toast.LENGTH_LONG).show();
                //addToUserFavorites();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecipeActivity.this, "Recipes : " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestRecipe.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(requestRecipe);
    }
}
