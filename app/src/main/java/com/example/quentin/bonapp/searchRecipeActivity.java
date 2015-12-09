package com.example.quentin.bonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import DataAccess.RequestQueueSingleton;
import Model.Recipe;

/**
 * Created by etu25714 on 20/10/2015.
 */
public class searchRecipeActivity extends AppCompatActivity {
    private Button validateButton;
    private EditText searchEditText;
    private String searchText;
    private ArrayList<Recipe> ListRecipes;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_recipe_activity);

        gson = new Gson();

        ListRecipes = new ArrayList<>();


        searchEditText = (EditText) findViewById(R.id.editTextSearchRecipe);
        validateButton = (Button) findViewById(R.id.buttonValiderRech);


        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchText = searchEditText.getText().toString();

                searchText = stringFormatter(searchText);

                RequestQueue requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,"http://food2fork.com/api/search?key=217401dcb0a4ad131cd118a528ce6cb4&q=" + searchText, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        parseJSONResponse(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(searchRecipeActivity.this, searchRecipeActivity.this.getString(R.string.requesterror) + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                });

                requestQueue.add(request);

            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView searchEditText, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    validateButton.performClick();
                }
                return true;
            }
        });
    }

    private String stringFormatter(String text) {
        text = text.replace(" ","%20");
        text = text.replace("\n","%20");
        text = text.replace(".",",");

        return text;
    }

    private void parseJSONResponse(JSONObject response) {
        if(response == null || response.length() == 0) {
            return;
        }

        try {
            if(response.has("recipes")) {

                JSONArray arrayRecipes = response.getJSONArray("recipes");
                Type listType = new TypeToken<List<Recipe>>(){}.getType();
                ListRecipes = gson.fromJson(arrayRecipes.toString(), listType);

                /*for(int i = 0; i < arrayRecipes.length(); i++) {
                    JSONObject currentRecipe = arrayRecipes.getJSONObject(i);


                    String publisher = currentRecipe.getString("publisher");

                    String f2f_url = currentRecipe.getString("f2f_url");

                    String recipe_id = currentRecipe.getString("recipe_id");

                    String title = currentRecipe.getString("title");

                    String image_url = currentRecipe.getString("image_url");

                    String source_url = currentRecipe.getString("source_url");

                    double social_rank = currentRecipe.getDouble("social_rank");

                    String publisher_url = currentRecipe.getString("publisher_url");


                    Recipe recipe = new Recipe(publisher, f2f_url, title, source_url, recipe_id, image_url, social_rank, publisher_url);
                    ListRecipes.add(recipe);
                }*/

                if (!ListRecipes.isEmpty()) {
                    Intent i = new Intent(searchRecipeActivity.this, ListRecipeActivity.class);
                    i.putExtra("listRecipes", ListRecipes);
                    startActivity(i);
                    ListRecipes.clear();
                } else {
                    Toast.makeText(searchRecipeActivity.this, this.getString(R.string.listeVide), Toast.LENGTH_LONG).show();
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
                startActivity(new Intent(searchRecipeActivity.this, FavoriteActivity.class));
                return true;
            case R.id.itemUserID:
                startActivity(new Intent(searchRecipeActivity.this, LoginActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
}
