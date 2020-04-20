package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setElevation(0);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = HomeFragment.newInstance();
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_bookmarks:
                        selectedFragment = BookmarkFragment.newInstance();
                        Toast.makeText(MainActivity.this, "Bookmarks", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_headline:
                        selectedFragment = HeadlineFragment.newInstance();
                        break;
                    case R.id.nav_trending:
                        selectedFragment = TrendingFragment.newInstance();
                        Toast.makeText(MainActivity.this, "Trending", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setBackgroundColor(Color.GREEN);

        int btnId = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView btn = searchView.findViewById(btnId);
        btn.setImageResource(R.drawable.ic_search_grey_24dp);

        int autoCompleteId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchAutoComplete = searchView.findViewById(autoCompleteId);
        searchAutoComplete.setTextColor(Color.BLACK);

        int imageViewId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        final ImageView searchClose = searchView.findViewById(imageViewId);
        final Context ctx = getApplicationContext();
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // Start new activity
                        Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                        intent.putExtra("query", query);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // Make Collapse
                        searchClose.setImageResource(R.drawable.ic_close_black_18dp);
                        if (newText.isEmpty()) {
                            searchClose.setVisibility(View.GONE);
                        } else {
                            searchClose.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }
                }
        );
        return true;
    }

}
