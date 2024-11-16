package com.example.littlenotes;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Little Notes");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        String profilePhotoUrl = sharedPreferences.getString("ProfilePhotoURL", null);

        if (isLoggedIn) {
            // Show NotesFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NotesFragment())
                    .commit();
            if (profilePhotoUrl != null) {
                displayProfilePhoto(profilePhotoUrl, toolbar);
            }
        } else {
            // Show LoginFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commit();
        }
    }

    private void displayProfilePhoto(String url, Toolbar toolbar) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new Toolbar.LayoutParams(50,50));
        Picasso.get().load(url).error(R.drawable.ic_launcher_foreground).into(imageView);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.END;
        imageView.setLayoutParams(params);
        toolbar.addView(imageView);

    }
}