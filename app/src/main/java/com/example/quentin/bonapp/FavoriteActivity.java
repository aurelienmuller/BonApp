package com.example.quentin.bonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class FavoriteActivity extends AppCompatActivity {

    private ListView favList;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_activity);

        favList = (ListView) findViewById(R.id.favListView);

        List<String> favListLocal = new ArrayList<String>();
        favListLocal.add("Hamburger");
        favListLocal.add("Pates");
        favListLocal.add("Patates rôties");
        favListLocal.add("Poulet grillé");
        favListLocal.add("Sauce carbo");
        favListLocal.add("Milkshake banane");
        favListLocal.add("Croque Monsieur");
        favListLocal.add("Tomates farcies");
        favListLocal.add("Pizza");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                favListLocal);
        favList.setAdapter(arrayAdapter);

        //setContentView(favList);
        favList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent recipeIntent = new Intent(FavoriteActivity.this, RecipeActivity.class);
                FavoriteActivity.this.startActivity(recipeIntent);
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
            case R.id.itemFavorisID:
                startActivity(new Intent(FavoriteActivity.this, FavoriteActivity.class));
                return true;
            case R.id.itemUserID:
                startActivity(new Intent(FavoriteActivity.this, LoginActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
}
