package com.example.quentin.bonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Aurélien on 04/11/2015.
 */
public class searchUserActivity extends AppCompatActivity {
    private Button validateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user_activity);

        validateButton = (Button) findViewById(R.id.buttonValidate);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(searchUserActivity.this, ListRecipeActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemFavorisID:
                startActivity(new Intent(searchUserActivity.this, FavoriteActivity.class));
                return true;
            case R.id.itemUserID:
                startActivity(new Intent(searchUserActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
