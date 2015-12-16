package com.bonapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

/**
 * Created by etu25714 on 20/10/2015.
 */
public class SearchRecipeActivity extends AppCompatActivity {
    private Button validateButton;
    private EditText searchEditText;
    private ArrayList<Recipe> ListRecipes;
    private Gson gson;
    private String searchText;
    private RequestQueue requestQueue;

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

                searchRecipe();

            }
        });


    }

    public void searchRecipe() {
        searchText = searchEditText.getText().toString();

        searchText = stringFormatter(searchText);

        requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MyApplication.getFood2forkUrl() + searchText, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                parseJSONResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SearchRecipeActivity.this, SearchRecipeActivity.this.getString(R.string.erreurserveur), Toast.LENGTH_SHORT).show();

            }

        });

        requestQueue.add(request);
    }


    private String stringFormatter(String text) {
        text = text.replace(" ","%20");
        text = text.replace("\n","%20");
        text = text.replace(".",",");
        text = text.replace("/", ",");
        text = text.replace("\\", ",");

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

                if (!ListRecipes.isEmpty()) {
                    Intent i = new Intent(SearchRecipeActivity.this, ListRecipeActivity.class);
                    i.putExtra("listRecipes", ListRecipes);
                    i.putExtra("parent", "SearchRecipeActivity");
                    startActivity(i);
                    ListRecipes.clear();
                } else {
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
