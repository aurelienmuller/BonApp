package com.bonapp.app;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bonapp.app.bonapp.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SearchActivity extends AppCompatActivity {

    private Button searchRecipe;
    private Button searchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.bonapp.app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        searchRecipe = (Button) findViewById(R.id.buttonRechRec);
        searchUser = (Button) findViewById(R.id.buttonRechUser);

        searchRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, searchRecipeActivity.class));
            }
        });
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, searchUserActivity.class));
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
                startActivity(new Intent(SearchActivity.this, LoginActivityFB.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
