package com.example.myapplication;

import android.util.Log;

public class Trend {

    private String time;
    private String formattedTime;
    private String formattedAxisTime;
    private String value;
    private String formtatedValue;

    public Trend(String time, String formattedTime, String formattedAxisTime, String value, String formtatedValue) {
        this.time = time;
        this.formattedTime = formattedTime;
        this.formattedAxisTime = formattedAxisTime;
        this.value = value;
        this.formtatedValue = formtatedValue;
    }

    public Trend() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getFormattedAxisTime() {
        return formattedAxisTime;
    }

    public void setFormattedAxisTime(String formattedAxisTime) {
        this.formattedAxisTime = formattedAxisTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFormtatedValue() {
        return formtatedValue;
    }

    public void setFormtatedValue(String formtatedValue) {
        this.formtatedValue = formtatedValue;
    }
}

