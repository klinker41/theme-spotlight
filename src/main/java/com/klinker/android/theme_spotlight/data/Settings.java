package com.klinker.android.theme_spotlight.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.klinker.android.theme_spotlight.R;

/**
 * Singleton for settings, lots and lots of room for expansion if I want
 */
public class Settings {

    private static Settings settings;

    // lots of room for expansion here...
    public boolean freeOnly;

    public static Settings getInstance(Context context) {
        return getInstance(context, false);
    }

    public static Settings getInstance(Context context, boolean forceReload) {
        if (settings == null || forceReload) {
            settings = new Settings(context);
        }

        return settings;
    }

    private Settings(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        freeOnly = sharedPreferences.getBoolean(context.getString(R.string.free_only_key), false);
    }
}
