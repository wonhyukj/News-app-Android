package com.example.myapplication;

import android.util.Log;

public class Weather {

    private String state;
    private String city;
    private String weather;
    private String temperature;

    public  Weather(){

    }

    public Weather(String state, String city, String weather , String temperature){
        this.state = state;
        this.city = city;
        this.weather = weather;
        this.temperature = temperature;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature + "\u2103";
        Log.i("TEMPERATURE: ", temperature);

    }

}

