package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class SharedPreference {

    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);

        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    public static int getCount(Context context, String preferenceFileName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        return sharedPreferences.getAll().size();
    }

    public static ArrayList<News> getAllSavedObjectFromPreference(Context context, String preferenceFileName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        ArrayList<News> news = new ArrayList<News>();

        int size = getCount(context, preferenceFileName);
        if (size == 0) {
            Log.i("No Result", "");
            return null;
        } else {

            Map<String, ?> keys = sharedPreferences.getAll();

            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                final Gson gson = new Gson();
                news.add(gson.fromJson(sharedPreferences.getString(entry.getKey(), ""), News.class));
            }
            Log.i("ALL SHARED: ", news.toString());

            return news;
        }
    }

    public static void removeSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            sharedPreferences.edit().remove(preferenceKey).commit();
        }
    }

}