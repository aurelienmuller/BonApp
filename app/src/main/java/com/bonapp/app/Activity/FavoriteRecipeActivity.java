package com.bonapp.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bonapp.app.Application.MyApplication;
import com.bonapp.app.bonapp.R;
import com.facebook.FacebookSdk;

import com.bonapp.app.DataAccess.RequestQueueSingleton;
import com.bonapp.app.Model.Recipe;

//Activity which contains a WebView of a favorite recipe
//We can set multiple WebView, the better suited will be chosen
public class FavoriteRecipeActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private Recipe recipeToFavorite;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        FacebookSdk.sdkInitialize(getApplicationContext());

        final WebView webView = (WebView) findViewById(R.id.webViewRecipe);
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
                //displays a dialog to confirm deletion of recipe
                alertDialog.show();

                return true;
            case R.id.itemUserID:
                startActivity(new Intent(FavoriteRecipeActivity.this, LoginActivityFB.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    //Creates the dialog to confirm the deletion of recipe
    public void createDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FavoriteRecipeActivity.this);
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

    //Volley request to delete the corresponding entry from the Userfavorite table
    //if successful, delete the corresponding recipe from the recipe table
    public void deleteUserfavorite() {
        String userIdString = MyApplication.getFbUserId();
        recipeToFavorite = (Recipe) (this.getIntent().getSerializableExtra(("recipe")));

        requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();

        StringRequest requestUserFav = new StringRequest(Request.Method.DELETE, MyApplication.getBonAppUserfavoritesUrl() + recipeToFavorite.getRecipe_id() + userIdString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                deleteRecipe();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;

                if(error.getCause() instanceof java.net.UnknownHostException) {
                    Toast.makeText(FavoriteRecipeActivity.this, R.string.noconnection, Toast.LENGTH_SHORT).show();

                } else {
                    switch(response.statusCode) {
                        case 404:
                            Toast.makeText(FavoriteRecipeActivity.this, R.string.recettedejasuppr, Toast.LENGTH_LONG).show();
                            break;

                        default:
                            Toast.makeText(FavoriteRecipeActivity.this, R.string.problemesurvenu, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        requestUserFav.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(requestUserFav);
    }

    //deletes the corresponding recipe from the Recipe table
    public void deleteRecipe() {

        StringRequest requestRecipe = new StringRequest(Request.Method.DELETE, MyApplication.getBonAppRecipesUrl() + recipeToFavorite.getRecipe_id(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(FavoriteRecipeActivity.this, R.string.recettesupprimee, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(error.getCause() instanceof java.net.UnknownHostException) {
                            Toast.makeText(FavoriteRecipeActivity.this, FavoriteRecipeActivity.this.getString(R.string.noconnection), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(FavoriteRecipeActivity.this, R.string.problemesurvenu, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                requestRecipe.setRetryPolicy(new DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(requestRecipe);
    }

}
