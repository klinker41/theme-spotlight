package com.klinker.android.theme_spotlight.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.activity.AboutAuthActivity;

/**
 * Singleton for settings
 */
public class Settings {

    private static final String TAG = "Settings";
    private static Settings settings;

    public SharedPreferences sharedPreferences;
    public boolean freeOnly;
    public boolean isUpdate;
    public int currentVersion;
    public String authToken;
    public String androidId;

    public static Settings getInstance(Context context) {
        return getInstance(context, false);
    }

    public static Settings getInstance(Context context, boolean forceReload) {
        if (context != null && (settings == null || forceReload)) {
            settings = new Settings(context);
        }

        return settings;
    }

    private Settings(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        freeOnly = sharedPreferences.getBoolean(context.getString(R.string.free_only_key), false);
        currentVersion = sharedPreferences.getInt(context.getString(R.string.current_version_key), 0);
        authToken = sharedPreferences.getString(context.getString(R.string.auth_token_key), null);
        androidId = sharedPreferences.getString(context.getString(R.string.android_id_key), null);

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
                Intent intent = new Intent(context, AboutAuthActivity.class);
                context.startActivity(intent);

                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }

                break;
        }
    }

    public void commitAuthInfo(Context context) {
        sharedPreferences.edit()
                .putString(context.getString(R.string.auth_token_key), authToken)
                .putString(context.getString(R.string.android_id_key), androidId)
                .commit();
    }
}
