package com.example.quentin.bonapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton searchButton = (ImageButton) findViewById(R.id.imageButtonSearch);
        final ImageButton favoriteButton = (ImageButton) findViewById(R.id.imageButtonFav);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchRecipeIntent = new Intent(MainActivity.this, SearchActivity.class);
                MainActivity.this.startActivity(searchRecipeIntent);
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent favoriteIntent = new Intent(MainActivity.this, FavoriteActivity.class);
                MainActivity.this.startActivity(favoriteIntent);
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
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            case R.id.itemFavorisID:
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                return true;
            case R.id.itemParametresID:
                startActivity(new Intent(MainActivity.this, OptionsActivity.class));
                return true;
            case R.id.itemUserID:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
}
