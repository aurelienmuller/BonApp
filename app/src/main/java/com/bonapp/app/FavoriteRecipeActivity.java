package com.bonapp.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bonapp.app.bonapp.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import DataAccess.RequestQueueSingleton;
import Model.Recipe;
import Model.Userfavorite;

public class FavoriteRecipeActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    RequestQueue requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();
    String recipeJson;
    String userFavJson;
    Recipe recipeToFavorite;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

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
        getMenuInflater().inflate(R.menu.favoritemenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.itemFavorisID:
                sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                String userIdString = sharedPreferences.getString("id", "");
                recipeToFavorite = (Recipe) (this.getIntent().getSerializableExtra(("recipe")));
                Userfavorite newUserFav = new Userfavorite(recipeToFavorite.getRecipe_id() + Integer.toString(666), 1, recipeToFavorite.getRecipe_id());
                Gson gson = new Gson();
                recipeJson = gson.toJson(recipeToFavorite);
                userFavJson = gson.toJson(newUserFav);



                /*StringRequest requestRecipe = new StringRequest(Request.Method.DELETE, "http://bonapp2.azurewebsites.net/api/recipes/" + recipeToFavorite.getRecipe_id(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(FavoriteRecipeActivity.this, "Recette supprimée aux favoris", Toast.LENGTH_LONG).show();
                        //addToUserFavorites();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FavoriteRecipeActivity.this, "Recipes : " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                requestRecipe.setRetryPolicy(new DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(requestRecipe);*/

                StringRequest requestUserFav = new StringRequest(Request.Method.DELETE, "http://bonapp2.azurewebsites.net/api/userfavorites/" + recipeToFavorite.getRecipe_id() + userIdString, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(FavoriteRecipeActivity.this, "Recette suppimée des favoris", Toast.LENGTH_LONG).show();
                        deleteUserFavorite();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FavoriteRecipeActivity.this, "Userfavorites : " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                requestUserFav.setRetryPolicy(new DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(requestUserFav);





                /*JsonObjectRequest requestUserFav = new JsonObjectRequest(Request.Method.POST, "http://bonapp.azurewebsites.net/api/userfavorites/", userFavJson, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RecipeActivity.this, "Recette ajoutée aux favoris", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RecipeActivity.this, "Userfavorites : " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                requestUserFav.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(requestUserFav);*/




                return true;
            case R.id.itemUserID:
                startActivity(new Intent(FavoriteRecipeActivity.this, LoginActivityFB.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void deleteUserFavorite() {
        /*JsonObjectRequest requestUserFav = new JsonObjectRequest(Request.Method.DELETE, "http://bonapp2.azurewebsites.net/api/userfavorites/" + recipeToFavorite.getRecipe_id() + Integer.toString(666), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(FavoriteRecipeActivity.this, "Recette suppimée des favoris", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FavoriteRecipeActivity.this, "Userfavorites : " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestUserFav.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(requestUserFav);*/

        StringRequest requestRecipe = new StringRequest(Request.Method.DELETE, "http://bonapp2.azurewebsites.net/api/recipes/" + recipeToFavorite.getRecipe_id(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(FavoriteRecipeActivity.this, "Recette supprimée des favoris", Toast.LENGTH_LONG).show();
                        //addToUserFavorites();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FavoriteRecipeActivity.this, "Recipes : " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                requestRecipe.setRetryPolicy(new DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(requestRecipe);
    }

}
