package com.example.quentin.bonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import Model.Recipe;


public class ListRecipeActivity extends AppCompatActivity {

    private ArrayList<Recipe> ListRecipes = new ArrayList<>();
    //private ListView recipeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);

        ListView recipeListView = (ListView) findViewById(R.id.recipeListView);


        //ListRecipes = new ArrayList<>();
        ListRecipes = (ArrayList<Recipe>) (this.getIntent().getSerializableExtra("listRecipes"));

        ArrayAdapter<Recipe> arrayAdapter = new ArrayAdapter<>(
                ListRecipeActivity.this,
                android.R.layout.simple_list_item_1,
                ListRecipes);
        recipeListView.setAdapter(arrayAdapter);

        Toast.makeText(ListRecipeActivity.this, ListRecipes.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.itemRechercheID:
                startActivity(new Intent(ListRecipeActivity.this, SearchActivity.class));
                return true;
            case R.id.itemFavorisID:
                startActivity(new Intent(ListRecipeActivity.this, FavoriteActivity.class));
                return true;
            case R.id.itemParametresID:
                startActivity(new Intent(ListRecipeActivity.this, OptionsActivity.class));
                return true;
            case R.id.itemUserID:
                startActivity(new Intent(ListRecipeActivity.this, LoginActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
}
