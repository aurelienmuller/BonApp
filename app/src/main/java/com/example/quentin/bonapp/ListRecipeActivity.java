package com.example.quentin.bonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class ListRecipeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);
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
