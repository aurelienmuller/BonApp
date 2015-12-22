package com.bonapp.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bonapp.app.Application.MyApplication;
import com.bonapp.app.bonapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.bonapp.app.DataAccess.RequestQueueSingleton;
import com.bonapp.app.Model.Recipe;


//The user enters his search parameters (recipe title, ingredients, ...) and a request is made on the Food2Fork API
public class SearchRecipeActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_recipe_activity);

        searchEditText = (EditText) findViewById(R.id.editTextSearchRecipe);
        final Button validateButton = (Button) findViewById(R.id.buttonValiderRech);

        makeProgressDialog();

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRecipe();
            }
        });

    }

    //makes the progress dialog which shows up when loading recipes
    public void makeProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.chargement);
        progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.chargementmsg));
        progressDialog.setCancelable(false);
    }

    //Takes the EditText text, formats it (not necessary but better be safe)
    // and uses it as a search parameter for the Volley GET request
    //if the EditText is empty, the API returns the 30 best rated recipes
    public void searchRecipe() {
        progressDialog.show();
        String searchText = searchEditText.getText().toString();

        searchText = stringFormatter(searchText);

        RequestQueue requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MyApplication.getFood2forkUrl() + searchText, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                parseJSONResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error.getCause() instanceof java.net.UnknownHostException) {
                    Toast.makeText(SearchRecipeActivity.this, SearchRecipeActivity.this.getString(R.string.noconnection), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchRecipeActivity.this, SearchRecipeActivity.this.getString(R.string.requesterror), Toast.LENGTH_SHORT).show();

                }
            }

        });

        requestQueue.add(request);
    }

    //Formats the user's input. This is in case the user puts a character which "stops" the url by mistake
    //So he gets some results anyway
    private String stringFormatter(String text) {
        text = text.replace(" ","%20");
        text = text.replace("\n","%20");
        text = text.replace(".",",");
        text = text.replace("/", ",");
        text = text.replace("\\", ",");

        return text;
    }


    //Takes the JSONObject response from the Volley request
    //Checks if it's null (never happens with food2fork API) then checks if it contains recipes
    //if there are recipes, put them in an ArrayList for the next activity (ListRecipeActivity)
    //if not, displays an error message
    private void parseJSONResponse(JSONObject response) {
        if(response == null || response.length() == 0) {
            return;
        }

        try {
            if(response.has("recipes")) {

                Gson gson = new Gson();
                ArrayList<Recipe> ListRecipes = new ArrayList<>();
                JSONArray arrayRecipes = response.getJSONArray("recipes");
                Type listType = new TypeToken<List<Recipe>>(){}.getType();
                ListRecipes = gson.fromJson(arrayRecipes.toString(), listType);

                if (!ListRecipes.isEmpty()) {
                    Intent i = new Intent(SearchRecipeActivity.this, ListRecipeActivity.class);
                    i.putExtra("listRecipes", ListRecipes);
                    i.putExtra("parent", "SearchRecipeActivity");
                    progressDialog.dismiss();
                    startActivity(i);
                    ListRecipes.clear();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SearchRecipeActivity.this, this.getString(R.string.listeVide), Toast.LENGTH_LONG).show();
                }

            }
        } catch (JSONException e) {
            Log.v("JSONException", e.getMessage());
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
                startActivity(new Intent(SearchRecipeActivity.this, LoginActivityFB.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
}
