package com.example.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchResultsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String query;
    TextView searchTerm;
    private List<News> news;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView progressBarTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        news = new ArrayList<>();

        query = getIntent().getStringExtra(SearchManager.QUERY);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.search_actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchTerm = findViewById(R.id.searchKeyword);
        searchTerm.setText(query);

        progressBar = findViewById(R.id.search_progress);
        progressBarTextView = findViewById(R.id.search_progress_text);

        swipeRefreshLayout = findViewById(R.id.search_swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar.setVisibility(View.VISIBLE);
        progressBarTextView.setVisibility(View.VISIBLE);
        extractNews(query);
        recyclerView = findViewById(R.id.recycler_search);
        SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(), news);
        recyclerView.setAdapter(searchAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(this.recyclerView.getAdapter()).notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);

    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                extractNews(query);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void extractNews(String query) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String JSON_URL = "http://nodeserverandroid-env.eba-cxvrpe5n.us-west-2.elasticbeanstalk.com/Search?id=" + query;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("temp");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject newsObject = jsonArray.getJSONObject(i);
                            Log.i("JSON OBJ ", newsObject.toString());
                            News newObj = new News();
                            newObj.setId(newsObject.getString("id"));
                            newObj.setTitle(newsObject.getString("titles"));
                            newObj.setTime(newsObject.getString("date"));
                            newObj.setSection(newsObject.getString("section"));
                            newObj.setWebURL(newsObject.getString("direct"));
                            newObj.setImg(newsObject.getString("img"));
                            newObj.setNewsImgURL(newsObject.getString("img"));
                            news.add(newObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    progressBarTextView.setVisibility(View.GONE);
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

}