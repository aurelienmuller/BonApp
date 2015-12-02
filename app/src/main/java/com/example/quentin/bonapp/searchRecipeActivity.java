package com.example.quentin.bonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import DataAccess.RequestQueueSingleton;

/**
 * Created by etu25714 on 20/10/2015.
 */
public class searchRecipeActivity extends AppCompatActivity {
    private Button validateButton;
    private EditText searchEditText;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_recipe_activity);



        searchEditText = (EditText) findViewById(R.id.editTextSearchRecipe);
        validateButton = (Button) findViewById(R.id.buttonValiderRech);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchText = searchEditText.getText().toString();

                RequestQueue requestQueue = RequestQueueSingleton.getInstance().getRequestQueue();
                StringRequest request = new StringRequest(Request.Method.GET,"http://food2fork.com/api/search?key=217401dcb0a4ad131cd118a528ce6cb4&q=" + searchText, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(searchRecipeActivity.this,"RESPONSE " + response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(searchRecipeActivity.this, "ERROR " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                });
                requestQueue.add(request);

                //startActivity(new Intent(searchRecipeActivity.this, ListRecipeActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.itemRechercheID:
                startActivity(new Intent(searchRecipeActivity.this, SearchActivity.class));
                return true;
            case R.id.itemFavorisID:
                startActivity(new Intent(searchRecipeActivity.this, FavoriteActivity.class));
                return true;
            case R.id.itemParametresID:
                startActivity(new Intent(searchRecipeActivity.this, OptionsActivity.class));
                return true;
            case R.id.itemUserID:
                startActivity(new Intent(searchRecipeActivity.this, LoginActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
}
