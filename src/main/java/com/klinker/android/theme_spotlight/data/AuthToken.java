/*
 * Copyright (C) 2014 Klinker Apps, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klinker.android.theme_spotlight.data;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.klinker.android.theme_spotlight.R;

public class AuthToken {

    private static final String TAG = "AuthToken";

    private static final String AUTH_TOKEN_PREF = "accounts_auth_token";
    private static final String ANDROID_ID_PREF = "accounts_android_id";

    private String authToken;
    private String androidId;

    public void initAuthToken(final Activity context, final OnLoadFinishedListener listener) {
        // if tokens are not available (ie this is first run, then we need to fetch
        // them. We authenticate with Google Play Services and get the correct account
        // token and then store that and the androidId in shared prefs for next time
        if (authToken == null || androidId == null) {
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    authToken = updateToken(false, context);
                    androidId = getAndroidID(context);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onLoadFinished();
                        }
                    });
                }
            }).start();
        } else {
            listener.onLoadFinished();
        }
    }

    public AuthToken(Context context) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        authToken = sharedPreferences.getString(AUTH_TOKEN_PREF, null);
        androidId = sharedPreferences.getString(ANDROID_ID_PREF, null);
    }

    private void storeToPrefs(Context context) {
        // update these in shared prefs
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(AUTH_TOKEN_PREF, authToken)
                .putString(ANDROID_ID_PREF, androidId)
                .commit();
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getAndroidId() {
        return androidId;
    }

    public interface OnLoadFinishedListener {
        public void onLoadFinished();
    }

    // get the android id from the device
    private String getAndroidID(Context context) {
        String[] query = new String[] {"android_id"};
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.google.android.gsf.gservices"), null, null, query, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getColumnCount() >= 2) {
            return Long.toHexString(Long.parseLong(cursor.getString(1))).toUpperCase();
        }

        return null;
    }

    // one method for getting the google auth token
    private String getAuthToken(Activity context, AccountManagerCallback<Bundle> callback) {
        String authToken = "null";

        try {
            AccountManager am = AccountManager.get(context);
            Bundle options = new Bundle();

            // access google play services to get this token
            if (am.getAccounts().length > 0) {
                am.getAuthToken(
                        am.getAccounts()[0],
                        "android",
                        options,
                        context,
                        callback,
                        null);
            } else {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.error_no_accounts)
                        .setMessage(R.string.error_no_accounts_summary)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authToken;
    }

    // another method for getting the auth token, I like this more because of the invalidation if I ever needed to
    private String updateToken(boolean invalidateToken, Activity activity) {
        String authToken = "null";
        try {
            AccountManager am = AccountManager.get(activity);
            Account[] accounts = am.getAccountsByType("com.google");
            AccountManagerFuture<Bundle> accountManagerFuture;
            if (activity == null) {
                accountManagerFuture = am.getAuthToken(accounts[0], "android", false, null, null);
            } else {
                accountManagerFuture = am.getAuthToken(accounts[0], "android", null, activity, null, null);
            }
            Bundle authTokenBundle = accountManagerFuture.getResult();
            authToken = authTokenBundle.getString(AccountManager.KEY_AUTHTOKEN).toString();
            if (invalidateToken) {
                am.invalidateAuthToken("com.google", authToken);
                authToken = updateToken(false, activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authToken;
    }
}
