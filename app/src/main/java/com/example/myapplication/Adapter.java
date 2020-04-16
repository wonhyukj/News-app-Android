package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
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
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    LayoutInflater inflater;
    List<News> news;
    List<Weather> weathers;
    List<Object> combine;
    private static int TYPE_WEATHER = 1;
    private static int TYPE_NEWS = 2;

    public Adapter(Context ctx, List<Object> combine){
        this.inflater = LayoutInflater.from(ctx);
        this.combine = combine;
    }

    @Override
    public int getItemViewType(int position)
    {
        if(position==0)
            return TYPE_WEATHER;
        else
            return TYPE_NEWS;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType==TYPE_WEATHER)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_layout,parent,false);
            viewHolder = new WeatherViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_layout,parent,false);
            viewHolder= new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == TYPE_WEATHER) {
            WeatherViewHolder weatherViewHolder = (WeatherViewHolder) holder;
            Weather weather = (Weather)combine.get(position);

            weatherViewHolder.city.setText(weather.getCity());
            weatherViewHolder.state.setText(weather.getState());
            weatherViewHolder.temperature.setText(weather.getTemperature());
            weatherViewHolder.weather.setText(weather.getWeather());

            weatherViewHolder.cardview.setPreventCornerOverlap(false);

            if(weather.getWeather().compareTo("Clouds") == 0) {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.cloudy_weather);
            } else if(weather.getWeather().compareTo("Clear") == 0) {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.clear_weather);
            } else if(weather.getWeather().compareTo("Snow") == 0) {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.snowy_weather);
            } else if(weather.getWeather().compareTo("Rain") == 0 || weather.getWeather().compareTo("Drizzle") == 0) {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.rainy_weather);
            } else if(weather.getWeather().compareTo("Thunderstorm") == 0) {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.thunder_weather);
            } else {
                weatherViewHolder.cardview.setBackgroundResource(R.drawable.sunny_weather);
            }
        }


        else {
            ViewHolder holders = (ViewHolder) holder;

            News news = (News) combine.get(position);

            ZonedDateTime timeNow = ZonedDateTime.now( ZoneId.of( "America/Montreal" ) );
            ZonedDateTime timeGenerated = ZonedDateTime.parse(news.getTime());
            Duration d = Duration.between( timeNow , timeGenerated );

            Log.i("WHAT NOW: " ,timeNow.toString());
            Log.i("WHAT Generated: " ,timeGenerated.toString());
            Log.i("WHAT DIFFERENCE: ", d.toString());

            String[] d_arr = d.toString().split("-");
            String timeReturn = "";

            for (String a : d_arr) {
                System.out.println(a.substring(a.length() - 1, a.length()));
                if(a.substring(a.length() - 1, a.length()).compareTo("H") == 0) {
                    timeReturn = a.substring(0, a.length() - 1) + "h ago | ";
                    System.out.println(a);

                    break;
                }

                else if(a.substring(a.length() - 1, a.length()).compareTo("M") == 0) {
                    timeReturn = a.substring(0, a.length() - 1) + "m ago | ";
                    System.out.println(a);

                    break;
                }
                else if(a.substring(a.length() - 1, a.length()).compareTo("S") == 0) {
                    timeReturn = a.substring(0, a.length() - 1) + "s ago | ";
                    System.out.println(a);

                    break;
                }
            }

            Log.i("TIMERETURN: ", timeReturn);
            holders.newsTitle.setText(news.getTitle());
            holders.newsSource.setText(news.getSection());
            holders.newsDate.setText(timeReturn);
            Picasso.get().load(news.getImg()).fit().into(holders.newsImage);
            File f = new File("/Users/wonhyukjang/Desktop/AndroidStudio Project/MyApplication/app/src/main/res/drawable/bookmark.png");
            Picasso.get().load(news.getImg()).fit().into(holders.newsBookmark);


//            holder.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    return false;
//                }
//            });

        }
    }

    @Override
    public int getItemCount() {
        return this.combine.size();
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
// Create custom dialog object
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final Dialog dialog = new Dialog(view.getContext());
                    // Include dialog.xml file
                    dialog.setContentView(R.layout.dialog);
                    // Set dialog title
                    dialog.setTitle("Custom Dialog");

                    // set values for custom dialog components - text, image and button
                    ImageView imageView = (ImageView) dialog.findViewById(R.id.imageDialog);
                    TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                    text.setText(newsTitle.getText());
                    ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);

                    Bitmap bitmap = ((BitmapDrawable)newsImage.getDrawable()).getBitmap();

                    image.setImageBitmap(bitmap);

                    dialog.show();

                    return false;
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
            cardview = (CardView)itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
