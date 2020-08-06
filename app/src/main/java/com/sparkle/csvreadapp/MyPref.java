package com.sparkle.csvreadapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;

import java.util.List;

public class MyPref {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context mContext;
    private static final String PREF_NAME = "sparkle.midlal.midlal.preference";

    public MyPref(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    public void setPref(String flagid, int fid) {
        editor.putInt(flagid, fid);
        editor.commit();
    }

    public Integer getPref(String flagid, int fid) {
        return pref.getInt(flagid, fid);
    }

    public void setPref(String key, String val) {
        editor.putString(key, val);
        editor.commit();
    }

    public String getPref(String key, String val) {
        return pref.getString(key, val);
    }

    public void setPref(String pref, float val) {
        editor.putFloat(pref, val);
        editor.commit();
    }
    public float getPref(String key, float val) {
        return pref.getFloat(key, val);
    }
    public void setPref(String pref, boolean val) {
        editor.putBoolean(pref, val);
        editor.commit();
    }
    public boolean getPref(String key, boolean val) {
        return pref.getBoolean(key, val);
    }




    public void setPref(String key, List<String[]> val) {
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(val);
        editor.putString(key, json1);
        editor.commit();

    }

    public void setPref(String key, Uri val) {
        editor.putString(key, String.valueOf(val));
        editor.commit();

    }

    public String getPref(String key, List<String[]> val) {

        return pref.getString(key, String.valueOf(val));
    }


    public void clearPref() {
        editor.clear();
        editor.commit();
    }
}
