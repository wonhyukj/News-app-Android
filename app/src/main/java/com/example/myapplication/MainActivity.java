package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private RecyclerView recyclerView;
    private LocationManager locationManager;
    private double previousLat = 0.0;
    private double previousLng = 0.0;
    private String provider;
    private List<Object> combines;
    private HomeAdapter homeAdapter;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this).setTitle("Title").setMessage("Location Permission")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create().show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(provider, 400, 1, this);
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        recyclerView = findViewById(R.id.recycler_view);
        combines = new ArrayList<>();
        extractNews();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        checkLocationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), SearchResultsActivity.class);
        Log.i("SearchView", String.valueOf(searchView));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        return true;
    }


    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        if (getPreviousLat() != lat || getPreviousLng() != lng) {
            setPreviousLat(lat);
            setPreviousLng(lng);
            try {
                Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                Log.i("Addresses", addresses.toString());
                String cityName = addresses.get(0).getLocality();
                String stateName = addresses.get(0).getAdminArea();
                String[] cityArr = cityName.split(" ");
                StringBuilder cityNameURL = new StringBuilder();
                for (int i = 0; i < cityArr.length; i++) {
                    if (i != cityArr.length - 1) {
                        cityNameURL.append(cityArr[i]);
                        cityNameURL.append("%20");
                    } else {
                        cityNameURL.append(cityArr[i]);
                    }
                }
                extractWeather(cityName, stateName, cityNameURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void extractWeather(final String cityName, final String stateName, StringBuilder cityNameURL) {
        String URL = "http://api.openweathermap.org/data/2.5/weather?q=" + cityNameURL + "&units=metric&appid=a2746082d6cfc8e4df2d8d352d2aa389";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("RESPONSE WEATHER: ", response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("weather");
                    String temperature = Integer.toString(response.getJSONObject("main").getInt("temp"));
                    String weatherStr = jsonArray.getJSONObject(0).getString("main");
                    Weather weather = new Weather(cityName, stateName, weatherStr, temperature);
                    combines.add(weather);
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

    private void extractNews() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String JSON_URL = "http://nodeserverandroid-env.eba-cxvrpe5n.us-west-2.elasticbeanstalk.com/Guardian_Home";
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
                            newObj.setTitle(newsObject.getString("webTitle"));
                            newObj.setTime(newsObject.getString("webPublicationDate"));
                            newObj.setSection(newsObject.getString("section"));
                            newObj.setWebURL(newsObject.getString("webUrl"));
                            newObj.setImg(newsObject.getString("bigUrl"));
                            combines.add(newObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LinearLayoutManager linearManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearManager);
                homeAdapter = new HomeAdapter(getApplicationContext(), combines);
                recyclerView.setAdapter(homeAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonObjectRequest);

    }

    public double getPreviousLat() {
        return previousLat;
    }

    public void setPreviousLat(double previousLat) {
        this.previousLat = previousLat;
    }

    public double getPreviousLng() {
        return previousLng;
    }

    public void setPreviousLng(double previousLng) {
        this.previousLng = previousLng;
    }
}
