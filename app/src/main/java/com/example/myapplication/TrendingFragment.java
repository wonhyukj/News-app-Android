package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
    private EditText trendQuery;
    private List<Trend> trends;
    private LineChart mChart;

    public static TrendingFragment newInstance() {
        TrendingFragment fragment = new TrendingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trends = new ArrayList<>();
    }

    public TrendingFragment() {
        // Required empty public constructor
    }

    public void createChart(String query) {
        if (trends.size() > 0) {
            ArrayList<Entry> yValues = new ArrayList<>();
            for (int i = 0; i < trends.size(); i++) {
                yValues.add(new Entry(i, Float.parseFloat(trends.get(i).getValue())));
            }
            LineDataSet set = new LineDataSet(yValues, "Trending Chart for " + query);
            set.setColor(0xFF8A1FCC);
            set.setCircleColor(0xFF8A1FCC);
            set.setDrawCircleHole(false);

            LineData data = new LineData(set);
            mChart.setData(data);
            mChart.invalidate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_trending, container, false);
        trendQuery = root.findViewById(R.id.trendQuery);
        mChart = root.findViewById(R.id.chart1);

        YAxis left = mChart.getAxisLeft();
        left.setDrawLabels(true);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(false);

        YAxis right = mChart.getAxisRight();
        right.setDrawLabels(true); // no axis labels
        right.setDrawAxisLine(true); // no axis line
        right.setDrawGridLines(false); // no grid lines
        right.setDrawZeroLine(false); // draw a zero line

        XAxis a = mChart.getXAxis();
        a.setDrawLabels(true); // no axis labels
        a.setDrawAxisLine(true); // no axis line
        a.setDrawGridLines(false); // no grid lines

        Legend l = mChart.getLegend();
        l.setTextSize(16f);
        l.setTextColor(Color.BLACK);

        String query = "CoronaVirus";
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

    private void extractTrend(final String query) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String JSON_URL = "http://nodeserverandroid-env.eba-cxvrpe5n.us-west-2.elasticbeanstalk.com/Trend?keyword=" + query;
        Log.i("JSON+URL: ", JSON_URL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    trends.clear();
                    JSONObject temp = response.getJSONObject("temp");
                    JSONObject defaultVal = temp.getJSONObject("default");
                    JSONArray timelineData = defaultVal.getJSONArray("timelineData");
                    Log.i("TREND: ", timelineData.toString());
                    for (int i = 0; i < timelineData.length(); i++) {
                        Trend trend = new Trend();
                        JSONObject instance = timelineData.getJSONObject(i);
                        trend.setTime(instance.getString("time"));
                        trend.setFormattedTime(instance.getString("formattedTime"));
                        trend.setFormattedAxisTime(instance.getString("formattedAxisTime"));

                        instance.getJSONArray("value").getString(0);
                        trend.setValue(instance.getJSONArray("value").getString(0));

                        instance.getJSONArray("formattedValue").getString(0);
                        trend.setFormtatedValue(instance.getJSONArray("formattedValue").getString(0));
                        trends.add(trend);
                    }
                    createChart(query);
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
