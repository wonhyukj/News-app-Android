package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class ArticleActivity extends AppCompatActivity {

    ImageView img, twitter;
    private final String JSON_URL = "http://nodeserverandroid-env.eba-cxvrpe5n.us-west-2.elasticbeanstalk.com/article?id=";
    String queryURL;
    List<DetailArticle> detailArticles;
    BottomNavigationView bottomNavigation;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArticleAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        detailArticles = new ArrayList<>();
        final Bundle bundle = getIntent().getExtras();
        // Show Back button
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.article_actionbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        twitter = findViewById(R.id.article_twitter);
        recyclerView = findViewById(R.id.recycler_view2);
        layoutManager = new LinearLayoutManager(this);

        View view = getSupportActionBar().getCustomView();
        // On Click Listener for Twitter and bookmark

        if (bundle.getString("url") != null) {
            queryURL = JSON_URL + bundle.getString("url");
            Log.i("Link: ", queryURL);

            Toast.makeText(this, queryURL, Toast.LENGTH_SHORT).show();

            // Change Bookmark if exist
            img = findViewById(R.id.article_bookmark);

            if (SharedPreference.getSavedObjectFromPreference(this.getApplicationContext(), "storage", bundle.getString("url"), News.class) == null) {
                img.setImageResource(R.drawable.ic_bookmark_border_24px);
            } else {
                img.setImageResource(R.drawable.ic_bookmark_24px);
            }

            // Set Twitter Event
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check out this Link: &url="
                            + bundle.getString("url") + "&hashtags=CSCI571NewsSearch"));
                    view.getContext().startActivity(intent);
                }
            });

        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreference.getSavedObjectFromPreference(view.getContext(), "storage", bundle.getString("url"), News.class) == null) {
                    img.setImageResource(R.drawable.ic_bookmark_24px);
                    News news = new News();
                    news.setId(bundle.getString("url"));
                    news.setImg(bundle.getString("newsImage"));
                    news.setSection(bundle.getString("newsSource"));
                    news.setTime(bundle.getString("newsDate"));
                    news.setTitle(bundle.getString("newsTitle"));
                    news.setWebURL(bundle.getString("newsURL"));


                    SharedPreference.saveObjectToSharedPreference(view.getContext(), "storage", bundle.getString("url"), news);
                } else {
                    img.setImageResource(R.drawable.ic_bookmark_border_24px);
                    SharedPreference.removeSavedObjectFromPreference(view.getContext(), "storage", bundle.getString("url"));
                }
            }
        });
        extractNews();

    }

    // Enable Back button
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void extractNews() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, queryURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("RESPONSE: ", response.toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = response.getJSONObject("temp");
                    Log.i("jsonObject", jsonObject.toString());

                    DetailArticle detailArticle = new DetailArticle();
                    String sectionName = jsonObject.getString("sectionName");
                    String webPublicationDate = jsonObject.getString("webPublicationDate");
                    String webTitle = jsonObject.getString("webTitle");
                    String webUrl = jsonObject.getString("webUrl");

                    JSONArray elements = jsonObject.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements");
                    JSONObject element = elements.getJSONObject(0);
                    JSONArray assets = element.getJSONArray("assets");
                    int leng = assets.length();
                    JSONObject last = null;
                    String img = "";
                    if(leng != 0) {
                        last = assets.getJSONObject(leng - 1);
                        img = last.getString("file");
                    }

                    String long_desc = jsonObject.getString("long_desc");

                    detailArticle.setArticleTitle(webTitle);
                    detailArticle.setArticleSource(sectionName);
                    detailArticle.setArticleDate(webPublicationDate);
                    detailArticle.setArticleURL(webUrl);
                    detailArticle.setArticleImg(img);
                    detailArticle.setArticleContent(long_desc);

                    Log.i("sectionName: ", sectionName);
                    Log.i("webPublicationDate: ", webPublicationDate);
                    Log.i("webTitle: ", webTitle);
                    Log.i("webUrl: ", webUrl);
                    detailArticles.add(detailArticle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


                articleAdapter = new ArticleAdapter(getApplicationContext(), detailArticles);
                recyclerView.setAdapter(articleAdapter);
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