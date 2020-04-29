package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        autoSuggestAdapter = new AutoSuggestAdapter(this, R.layout.auto_suggest);
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
                        break;
                    case R.id.nav_bookmarks:
                        selectedFragment = BookmarkFragment.newInstance();
                        break;
                    case R.id.nav_headline:
                        selectedFragment = HeadlineFragment.newInstance();
                        break;
                    case R.id.nav_trending:
                        selectedFragment = TrendingFragment.newInstance();
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
                    JSONObject dummy = suggestionGroups.getJSONObject(0);
                    JSONArray searchSuggestions = dummy.getJSONArray("searchSuggestions");
                    List<String> suggestions = new ArrayList<>();
                    for (int i = 0; i < searchSuggestions.length(); i++) {
                        if (suggestions.size() > 5) break;
                        String query = searchSuggestions.getJSONObject(i).getString("query");
                        suggestions.add(query);
                    }
                    autoSuggestAdapter.setData(suggestions);
                    autoSuggestAdapter.notifyDataSetChanged();
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
        searchAutoComplete.setAdapter(autoSuggestAdapter);
        searchAutoComplete.setThreshold(2);
        searchAutoComplete.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchAutoComplete.setText(autoSuggestAdapter.getObject(position));
            }
        });

        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchAutoComplete.getText()) && searchAutoComplete.getText().length() > 2) {
                        extractSuggestion(searchAutoComplete.getText().toString());
                    }
                }
                return false;
            }
        });


        int imageViewId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        final ImageView searchClose = searchView.findViewById(imageViewId);
        searchClose.setImageResource(R.drawable.ic_close_black_18dp);
        searchClose.setVisibility(View.GONE);
        searchAutoComplete.setTextColor(Color.BLACK);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(" ");
        builder.setSpan(new ImageSpan(getApplicationContext(), R.drawable.ic_search_grey_24dp),
                builder.length() - 1, builder.length(), 0);

        searchAutoComplete.setHint(builder);
        searchAutoComplete.setFocusableInTouchMode(true);
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
                        if (newText.length() > 0) {
                            searchClose.setVisibility(View.VISIBLE);
                        } else {
                            searchClose.setVisibility(View.GONE);
                        }
                        return false;
                    }
                }
        );
        return true;
    }
}
