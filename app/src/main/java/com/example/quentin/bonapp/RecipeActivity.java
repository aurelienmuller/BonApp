package com.example.quentin.bonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.gson.Gson;

import org.json.JSONObject;

import DataAccess.RequestQueueSingleton;
import Model.Recipe;
import Model.Userfavorite;


public class RecipeActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        webView = (WebView) findViewById(R.id.webViewRecipe);
        webView.setWebViewClient(new WebViewClient());
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
                Recipe recipeToFavorite = (Recipe) (this.getIntent().getSerializableExtra(("recipe")));
                Userfavorite newUserFav = new Userfavorite(1, recipeToFavorite.getRecipe_id());
                Gson gson = new Gson();
                String recipeJson = gson.toJson(recipeToFavorite);
                String userFavJson = gson.toJson(newUserFav);

                RequestQueue requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();

                JsonObjectRequest requestRecipe = new JsonObjectRequest(Request.Method.POST, "http://bonapp.azurewebsites.net/api/recipes/", recipeJson, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RecipeActivity.this, "Recette ajoutée aux favoris", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RecipeActivity.this, "Recipes : " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                requestRecipe.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(requestRecipe);

                JsonObjectRequest requestUserFav = new JsonObjectRequest(Request.Method.POST, "http://bonapp.azurewebsites.net/api/userfavorites/", userFavJson, new Response.Listener<JSONObject>() {
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
                requestQueue.add(requestUserFav);




                return true;
            case R.id.itemUserID:
                startActivity(new Intent(RecipeActivity.this, LoginActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
}
