package com.threeblocks.android.threeblocks.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by seungwoo on 2017-08-04.
 */

public class QueryPreference {
    private static final String PREF_SEARCH_QUERY = "searchQuery";


    private static final int MAX_SIZE = 5;

    public QueryPreference() {
        super();
    }

    public static void setStringArrayPref(Context context,String pref_name, String key, ArrayList<String> values) {
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        editor = prefs.edit();

        Gson gson = new Gson();
        String jsonSearch = gson.toJson(values);

        /*
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        */
        editor.putString(key,jsonSearch);
        editor.apply();
    }

    public static ArrayList<String> getStringArrayPref(Context context,String pref_name,String key) {

        SharedPreferences prefs;
        prefs = context.getSharedPreferences(pref_name,context.MODE_PRIVATE);
        ArrayList<String> yourList;
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        if (prefs.contains(key)){
            String jsonSearch = prefs.getString(key,null);
            Gson gson = new Gson();
            String[] serachitems = gson.fromJson(jsonSearch,
                    String[].class);
            yourList = new ArrayList<String>(Arrays.asList(serachitems));
            //ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arr));
        } else
            return null;
        return yourList;
        /*
            ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
        */
    }
    public static void addList(Context context, String pref_name, String key,String country) {
        ArrayList<String> yoursearch = getStringArrayPref(context, pref_name, key);
        if (yoursearch == null)
            yoursearch = new ArrayList<>();

        if(yoursearch.size() > MAX_SIZE) {
            //Log.i(PREF_SEARCH_QUERY, String.valueOf(yoursearch.size()));
            yoursearch.clear();
            deleteList(context, pref_name);
        }

        if(yoursearch.contains(country)){

            yoursearch.remove(country);

        }
        yoursearch.add(country);

        setStringArrayPref(context, pref_name, key, yoursearch);

    }
    public static void deleteList(Context context, String pref_name){

        SharedPreferences myPrefs = context.getSharedPreferences(pref_name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.apply();
    }

}
