package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


//import androidx.recyclerview.widget.RecyclerView;
//    implementation 'com.android.support:recyclerview-v7:28.0.0'

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<Weather> weathers;
    private static int TYPE_WEATHER = 1;
    private static int TYPE_NEWS = 2;

    public WeatherAdapter(Context ctx, List<Weather> weathers){
        this.inflater = LayoutInflater.from(ctx);
        this.weathers = weathers;
    }

//    @Override
//    public int getItemViewType(int position)
//    {
//        if(position==0)
//            return TYPE_WEATHER;
//        else
//            return TYPE_NEWS;
//    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
//        if(viewType==TYPE_WEATHER)
//        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_layout,parent,false);
            viewHolder = new WeatherViewHolder(view);
//        }
//        else
//        {
//        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_layout,parent,false);
//        viewHolder= new ViewHolder(view);
//        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if(holder.getItemViewType() == TYPE_WEATHER) {
            WeatherViewHolder weatherViewHolder = (WeatherViewHolder) holder;
            weatherViewHolder.city.setText(weathers.get(position).getCity());
            weatherViewHolder.state.setText(weathers.get(position).getState());
            weatherViewHolder.temperature.setText(weathers.get(position).getTemperature());
            weatherViewHolder.weather.setText(weathers.get(position).getWeather());
            weatherViewHolder.cardview.setPreventCornerOverlap(false);

            if(weathers.get(position).getWeather().compareTo("Clouds") == 0) {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.cloudy_weather);
            } else if(weathers.get(position).getWeather().compareTo("Clear") == 0) {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.clear_weather);
            } else if(weathers.get(position).getWeather().compareTo("Snow") == 0) {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.snowy_weather);
            } else if(weathers.get(position).getWeather().compareTo("Rain") == 0 || weathers.get(position).getWeather().compareTo("Drizzle") == 0) {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.rainy_weather);
            } else if(weathers.get(position).getWeather().compareTo("Thunderstorm") == 0) {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.thunder_weather);
            } else {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.sunny_weather);
            }
    }

    @Override
    public int getItemCount() {
        return this.weathers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView newsTitle,newsSource,newsDate;

        ImageView newsImage, newsBookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newsImage = itemView.findViewById(R.id.newsImage);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsSource = itemView.findViewById(R.id.newsSource);
            newsDate = itemView.findViewById(R.id.newsDate);
            newsBookmark = itemView.findViewById(R.id.bookmark);
            // handle onClick

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public  class WeatherViewHolder extends RecyclerView.ViewHolder{
        TextView city,state,weather,temperature;
        CardView cardview;


        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            city = itemView.findViewById(R.id.city);
            state = itemView.findViewById(R.id.state);
            weather = itemView.findViewById(R.id.weather);
            temperature = itemView.findViewById(R.id.temperature);
            // handle onClick
            cardview = (CardView)itemView.findViewById(R.id.cardView);
//            cardview.setCardBackgroundColor(Color.parseColor("#b70505"));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
