package com.example.quentin.bonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import Model.Recipe;


public class RecipeActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        webView = (WebView) findViewById(R.id.webViewRecipe);
        webView.setWebViewClient(new WebViewClient());
        Recipe recipe = (Recipe) (this.getIntent().getSerializableExtra("recipe"));

        webView.loadUrl(recipe.getSource_url());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.recipemenu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.itemFavorisID:
                startActivity(new Intent(RecipeActivity.this, FavoriteActivity.class));
                return true;
            case R.id.itemUserID:
                startActivity(new Intent(RecipeActivity.this, LoginActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
}
