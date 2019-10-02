package com.jumadi.moviecatalogue.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class PrefManager {

    private static final String INITIALIZE = "initialize";

    private static SharedPreferences pref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor savePref(Context context) {
        SharedPreferences prefs = pref(context);
         return prefs.edit();
    }

    public static boolean getInitialize(Context context) {
        SharedPreferences prefs = pref(context);
        return prefs.getBoolean(INITIALIZE, false);
    }

    public static void setInitialize(Context context, Boolean set) {
        SharedPreferences.Editor editor = savePref(context);
        editor.putBoolean(INITIALIZE, set);
        editor.apply();
    }
}
