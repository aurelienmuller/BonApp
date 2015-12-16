package com.bonapp.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bonapp.app.bonapp.R;
import com.facebook.Profile;
import com.google.gson.Gson;

import org.json.JSONObject;

import com.bonapp.app.DataAccess.RequestQueueSingleton;
import com.bonapp.app.Model.Recipe;
import com.bonapp.app.Model.Userfavorite;


public class RecipeActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    String recipeJson;
    String userFavJson;
    Recipe recipeToFavorite;
    Profile profile;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private RequestQueue requestQueue;


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

        createDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.recipemenu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.itemFavorisID:

                alertDialog.show();

                return true;
            case R.id.itemUserID:
                startActivity(new Intent(RecipeActivity.this, LoginActivityFB.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void addToRecipes() {
        String userIdString = MyApplication.getFbUserId();
        int userIdInt = Integer.parseInt(userIdString);
        recipeToFavorite = (Recipe) (this.getIntent().getSerializableExtra(("recipe")));
        Userfavorite newUserFav = new Userfavorite(recipeToFavorite.getRecipe_id() + userIdString, userIdInt, recipeToFavorite.getRecipe_id());
        Gson gson = new Gson();
        recipeJson = gson.toJson(recipeToFavorite);
        userFavJson = gson.toJson(newUserFav);

        requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();

        JsonObjectRequest requestRecipe = new JsonObjectRequest(Request.Method.POST, MyApplication.getBonAppRecipesUrl(), recipeJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                addToUserFavorites();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 409:
                            Toast.makeText(RecipeActivity.this, R.string.recetteexiste, Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(RecipeActivity.this, R.string.erreurserveur, Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(RecipeActivity.this, R.string.problemesurvenu, Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        });
        requestRecipe.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(requestRecipe);
    }

    public void createDialog() {
        alertDialogBuilder = new AlertDialog.Builder(RecipeActivity.this);
        alertDialogBuilder.setTitle(R.string.favoris)
                .setMessage(R.string.ajouterfav)
                .setCancelable(true)
                .setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addToRecipes();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = alertDialogBuilder.create();
    }

    public void addToUserFavorites() {
        JsonObjectRequest requestUserFav = new JsonObjectRequest(Request.Method.POST, MyApplication.getBonAppUserfavoritesUrl(), userFavJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(RecipeActivity.this, R.string.recetteajoutee, Toast.LENGTH_LONG).show();
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
        StringRequest requestRecipe = new StringRequest(Request.Method.DELETE, MyApplication.getBonAppRecipesUrl() + recipeToFavorite.getRecipe_id(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RecipeActivity.this, R.string.recettesupprimee, Toast.LENGTH_LONG).show();
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
