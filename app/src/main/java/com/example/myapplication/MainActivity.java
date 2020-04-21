package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static String[] suggestion;
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        suggestion= new String[100];
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        extractSuggestion("Trump");
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
    private void extractSuggestion(String query) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String JSON_URL = "https://api.cognitive.microsoft.com/bing/v7.0/suggestions?q=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray suggestionGroups = response.getJSONArray("suggestionGroups");
//                    JSONObject searchSuggestions = suggestionGroups.
//                    JSONArray searchSuggestions = suggestionGroups.getJSONArray()
                    Log.i("SuggestionGroups: ", suggestionGroups.toString());
                    JSONObject dummy = suggestionGroups.getJSONObject(0);
                    JSONArray searchSuggestions = dummy.getJSONArray("searchSuggestions");
                    Log.i("searchSuggestions: ", searchSuggestions.toString());
                    for(int i = 0; i < searchSuggestions.length(); i++) {
                        String query = searchSuggestions.getJSONObject(i).getString("query");
                        suggestion[i] = query;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Ocp-Apim-Subscription-Key", "07aa6645f303473694af78aeb1297917");

                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        int autoCompleteId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        final AutoCompleteTextView searchAutoComplete = searchView.findViewById(autoCompleteId);
        searchAutoComplete.setTextColor(Color.BLACK);

        int imageViewId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        final ImageView searchClose = searchView.findViewById(imageViewId);

        searchAutoComplete.setTextColor(Color.BLACK);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(" ");
        builder.setSpan(new ImageSpan(getApplicationContext(), R.drawable.ic_search_grey_24dp),
                builder.length() - 1, builder.length(), 0);

        searchAutoComplete.setHint(builder);
        searchAutoComplete.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchAutoComplete, InputMethodManager.SHOW_IMPLICIT);
        adapter = new ArrayAdapter<String>(this, R.layout.auto_suggest, suggestion);
        searchAutoComplete.setAdapter(adapter);

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
                        searchClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                searchAutoComplete.setText("");
                                searchView.requestFocusFromTouch();
                                searchClose.setVisibility(View.INVISIBLE);
                            }
                        });
                        searchClose.setVisibility(View.VISIBLE);
                        if (newText.length() >= 3) {
                            extractSuggestion(newText);
                            Log.i("suggestion: ",suggestion.toString());
//                            searchAutoComplete.getAdapter().notify();

                        } else {
                            searchAutoComplete.setAdapter(null);
                        }
                        if (newText.length() == 0) {
                            searchClose.setVisibility(View.INVISIBLE);
                        }
                        return false;
                    }
                }
        );


        return true;

    }
}
