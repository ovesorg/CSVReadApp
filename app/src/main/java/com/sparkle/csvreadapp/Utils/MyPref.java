package com.sparkle.csvreadapp.Utils;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Admin on 01-05-2018.
 */

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

    public void setPref(String flagid, long fid) {
        editor.putLong(flagid, fid);
        editor.commit();
    }

    public Long getPref(String flagid, long fid){
        return pref.getLong(flagid,fid);
    }


    public void setPref(String key, List<String> val) {
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(val);
        editor.putString(key, json1);
        editor.commit();

    }

    public String getPref(String key, List<String> val) {

        return pref.getString(key, String.valueOf(val));
    }

//    public void setPref3(String key, List<AccountDetail> val) {
//        Gson gson1 = new Gson();
//        String json1 = gson1.toJson(val);
//        editor.putString(key, json1);
//        editor.commit();
//
//    }
//
//    public String getPref3(String key, List<AccountDetail> val) {
//
//        return pref.getString(key, String.valueOf(val));
//    }
    public void setPref1(String key, List<BluetoothDevice> val) {
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(val);
        editor.putString(key, json1);
        editor.commit();
    }

    public String getPref1(String key, List<BluetoothDevice> val) {

        return pref.getString(key, String.valueOf(val));
    }

    public void setPref2(String key, List<ScanResult> val) {
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(val);
        editor.putString(key, json1);
        editor.commit();

    }

    public String getPref2(String key, List<ScanResult> val) {

        return pref.getString(key, String.valueOf(val));
    }


    public void setPref(String key, String val) {
        editor.putString(key, val);
        editor.commit();
    }

    public String getPref(String key, String val) {
        return pref.getString(key, val);
    }

    public void setPref(String pref, boolean val) {
        editor.putBoolean(pref, val);
        editor.commit();
    }

    public int getUnreadCount() {
        String unread = getPref(Constant.UNREAD_COMMENTS, "");
        int count = 0;
        if (unread.isEmpty())
            return 0;

        try {
            JSONObject unreadJson = new JSONObject(unread);
            Iterator<String> iter = unreadJson.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                count += unreadJson.getInt(key);
            }
            return count;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addUnreadCount(String chatId) {
        String unread = getPref(Constant.UNREAD_COMMENTS, "");
        try {
            JSONObject unreadJson;
            if (unread.isEmpty())
                unreadJson = new JSONObject();
            else
                unreadJson = new JSONObject(unread);

            if (unreadJson.has(chatId)) {
                int current = unreadJson.getInt(chatId);
                unreadJson.put(chatId, current + 1);
            } else {
                unreadJson.put(chatId, 1);
            }
            setPref(Constant.UNREAD_COMMENTS, unreadJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeFromUnread(String chatId) {
        String unread = getPref(Constant.UNREAD_COMMENTS, "");
        try {
            if (!unread.isEmpty()) {
                JSONObject unreadJson = new JSONObject(unread);
                if (unreadJson.has(chatId)) {
                    unreadJson.remove(chatId);
                    setPref(Constant.UNREAD_COMMENTS, unreadJson.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean getPref(String key, boolean val) {
        return pref.getBoolean(key, val);
    }

    public void clearPref() {
        editor.clear();
        editor.commit();
    }
}
