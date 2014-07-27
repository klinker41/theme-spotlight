package com.klinker.android.theme_spotlight.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import com.klinker.android.theme_spotlight.R;

/**
 * Singleton for settings, lots and lots of room for expansion if I want
 */
public class Settings {

    private static final String TAG = "Settings";
    private static Settings settings;

    // lots of room for expansion here...
    public boolean freeOnly;
    public boolean isUpdate;
    public int currentVersion;

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
        currentVersion = sharedPreferences.getInt(context.getString(R.string.current_version_key), 0);

        checkIsUpdate(context);
    }

    private void checkIsUpdate(Context context) {
        try {
            PackageInfo spotlightInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int version = spotlightInfo.versionCode;

            if (version != currentVersion) {
                isUpdate = true;
                actOnUpdate(context);
                currentVersion = version;
                PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putInt(context.getString(R.string.current_version_key), version).commit();
            } else {
                isUpdate = false;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    private void actOnUpdate(Context context) {
        // here, current version is actually the old version since we haven't updated it yet to the new version.
        // so, what we are doing is saying, if the old version was this, then do that and so on
        switch (currentVersion) {
            case 0:
                // do something here when updating
        }
    }
}
