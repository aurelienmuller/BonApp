package com.bonapp.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.AlertDialog;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bonapp.app.bonapp.R;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.gson.Gson;

import com.bonapp.app.DataAccess.RequestQueueSingleton;
import com.bonapp.app.Model.Recipe;

public class FavoriteRecipeActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;
    private String recipeJson;
    private String userIdString;
    private Recipe recipeToFavorite;
    private Profile profile;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        FacebookSdk.sdkInitialize(getApplicationContext());
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
        getMenuInflater().inflate(R.menu.favoritemenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.itemFavorisID:


                alertDialog.show();


                return true;
            case R.id.itemUserID:
                startActivity(new Intent(FavoriteRecipeActivity.this, LoginActivityFB.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void createDialog() {
        alertDialogBuilder = new AlertDialog.Builder(FavoriteRecipeActivity.this);
        alertDialogBuilder.setTitle(R.string.favoris)
                .setMessage(R.string.supprimerfav)
                .setCancelable(true)
                .setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteUserfavorite();
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

    public void deleteUserfavorite() {
        userIdString = MyApplication.getFbUserId();
        recipeToFavorite = (Recipe) (this.getIntent().getSerializableExtra(("recipe")));
        gson = new Gson();
        recipeJson = gson.toJson(recipeToFavorite);

        requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();

        StringRequest requestUserFav = new StringRequest(Request.Method.DELETE, MyApplication.getBonAppUserfavoritesUrl() + recipeToFavorite.getRecipe_id() + userIdString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                deleteRecipe();
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
    }

    public void deleteRecipe() {

        StringRequest requestRecipe = new StringRequest(Request.Method.DELETE, MyApplication.getBonAppRecipesUrl() + recipeToFavorite.getRecipe_id(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(FavoriteRecipeActivity.this, R.string.recettesupprimee, Toast.LENGTH_LONG).show();
                        //addToUserFavorites();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FavoriteRecipeActivity.this, R.string.problemesurvenu, Toast.LENGTH_LONG).show();
                    }
                });
                requestRecipe.setRetryPolicy(new DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(requestRecipe);
    }

}
