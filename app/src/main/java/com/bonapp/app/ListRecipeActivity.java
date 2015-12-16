package com.bonapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bonapp.app.bonapp.R;

import java.util.ArrayList;

import com.bonapp.app.Model.Recipe;


public class ListRecipeActivity extends AppCompatActivity {

    private ArrayList<Recipe> ListRecipes = new ArrayList<>();
    String previousActivity;
    //private ListView recipeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);

        previousActivity = getIntent().getStringExtra("parent");

        ListView recipeListView = (ListView) findViewById(R.id.recipeListView);


        //ListRecipes = new ArrayList<>();
        ListRecipes = (ArrayList<Recipe>) (this.getIntent().getSerializableExtra("listRecipes"));

        ArrayAdapter<Recipe> arrayAdapter = new ArrayAdapter<>(
                ListRecipeActivity.this,
                android.R.layout.simple_list_item_1,
                ListRecipes);
        recipeListView.setAdapter(arrayAdapter);

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent recipeIntent;
                if(previousActivity.equals("MainActivity")) {
                    //Toast.makeText(ListRecipeActivity.this, previousActivity, Toast.LENGTH_LONG).show();
                    recipeIntent = new Intent(ListRecipeActivity.this, FavoriteRecipeActivity.class);
                } else {
                    recipeIntent = new Intent(ListRecipeActivity.this, RecipeActivity.class);
                }
                recipeIntent.putExtra("recipe", ListRecipes.get(position));

                startActivity(recipeIntent);
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
            case R.id.itemUserID:
                startActivity(new Intent(ListRecipeActivity.this, LoginActivityFB.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
}
