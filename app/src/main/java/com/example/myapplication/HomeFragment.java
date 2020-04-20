package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
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
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<Object> combines;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView progressBarTextView;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        combines = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(this.recyclerView.getAdapter()).notifyDataSetChanged();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getMainActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getApplicationContext()).setTitle("Title").setMessage("Location Permission")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getMainActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create().show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getMainActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private void addWeatherCard(double lat, double lng) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_layout, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);

        HomeAdapter homeAdapter = new HomeAdapter(getApplicationContext(), combines);
        recyclerView.setAdapter(homeAdapter);
        progressBar = root.findViewById(R.id.home_progress);
        progressBarTextView = root.findViewById(R.id.home_textview);

        swipeRefreshLayout = root.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        checkPermission();
        final double[] lat = {34.0266};
        final double[] lng = {-118.2828};
        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat[0] = location.getLatitude();
                lng[0] = location.getLongitude();
                addWeatherCard(lat[0], lng[0]);
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
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600, 50, locationListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        progressBar.setVisibility(View.VISIBLE);
        progressBarTextView.setVisibility(View.VISIBLE);
        extractNews();
        return root;
    }

    private Activity getMainActivity() {
        return Objects.requireNonNull(getActivity());
    }

    private Context getApplicationContext() {
        return getMainActivity().getApplicationContext();
    }

    private void extractNews() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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
                            newObj.setNewsImgURL(newsObject.getString("bigUrl"));
                            combines.add(newObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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

    private void extractWeather(final String cityName, final String stateName, StringBuilder cityNameURL) {
        String URL = "http://api.openweathermap.org/data/2.5/weather?q=" + cityNameURL + "&units=metric&appid=a2746082d6cfc8e4df2d8d352d2aa389";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("RESPONSE WEATHER: ", response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("weather");
                    String temperature = Integer.toString(response.getJSONObject("main").getInt("temp"));
                    String weatherStr = jsonArray.getJSONObject(0).getString("main");
                    Weather weather = new Weather(cityName, stateName, weatherStr, temperature);
                    boolean isContain = false;
                    if (combines.size() > 0) {
                        if (combines.get(0) instanceof Weather) {
                            isContain = true;
                            combines.remove(0);
                            combines.add(0, weather);
                        }
                    }
                    if (!isContain) {
                        combines.add(0, weather);
                    }
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Object weather = null;
                if (combines.size() > 0) {
                    weather = combines.get(0);
                }
                combines.clear();
                if (weather != null) {
                    combines.add(weather);
                }
                extractNews();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }
}
