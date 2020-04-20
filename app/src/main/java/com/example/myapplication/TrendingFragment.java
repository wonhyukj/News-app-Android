package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment implements View.OnKeyListener {
    EditText trendQuery;
    private List<Trend> trends;
    private LineChart mChart;
    private ArrayList<Trend> trendArrayList;


    public static TrendingFragment newInstance() {
        TrendingFragment fragment = new TrendingFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trends = new ArrayList<>();
        trendArrayList = new ArrayList<>();
    }
    public TrendingFragment() {
        // Required empty public constructor
    }

    public void createChart() {
        if (trends.size() > 0) {
            ArrayList<Entry> yValues = new ArrayList<>();
            for (int i = 0; i < trends.size(); i++) {
                yValues.add(new Entry(i, Float.parseFloat(trends.get(i).getValue())));
            }
            LineDataSet set = new LineDataSet(yValues, "Dataset");

            set.setFillAlpha(110);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);

            LineData data = new LineData(dataSets);
            mChart.setData(data);
            mChart.invalidate();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_trending, container, false);
        trendQuery = root.findViewById(R.id.trendQuery);
        mChart = root.findViewById(R.id.chart1);

        String query = trendQuery.getText().toString();
        extractTrend(query);


        trendQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String query = trendQuery.getText().toString();
                    extractTrend(query);
                    handled = true;

                }
                return handled;
            }
        });
        return root;
    }

    private Activity getMainActivity() {
        return Objects.requireNonNull(getActivity());
    }

    private Context getApplicationContext() {
        return getMainActivity().getApplicationContext();
    }

    private void extractTrend(String query) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String JSON_URL = "http://nodeserverandroid-env.eba-cxvrpe5n.us-west-2.elasticbeanstalk.com/Trend?keyword=" + query;
        Log.i("JSON+URL: ", JSON_URL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    trends.clear();
                    JSONObject temp = response.getJSONObject("temp");
                    JSONObject defaultVal = temp.getJSONObject("default");
                    JSONArray timelineData = defaultVal.getJSONArray("timelineData");
                    Log.i("TREND: ", timelineData.toString());
                    for(int i = 0; i < timelineData.length(); i ++) {
                        Trend trend = new Trend();
                        JSONObject instance = timelineData.getJSONObject(i);
                        trend.setTime(instance.getString("time").toString());
                        trend.setFormattedTime(instance.getString("formattedTime").toString());
                        trend.setFormattedAxisTime(instance.getString("formattedAxisTime").toString());

                        instance.getJSONArray("value").getString(0);
                        trend.setValue(instance.getJSONArray("value").getString(0));

                        instance.getJSONArray("formattedValue").getString(0);
                        trend.setFormtatedValue(instance.getJSONArray("formattedValue").getString(0));
                        trends.add(trend);
                    }
                    createChart();
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
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }
}
