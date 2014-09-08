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
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

public class AuthToken {

    private static final String TAG = "AuthToken";

    private String authToken;
    private String androidId;

    public void initAuthToken(final Activity context, final OnLoadFinishedListener listener) {
        // if tokens are not available (ie this is first run), then we need to fetch
        // them. We authenticate with Google Play Services and get the correct account
        // token and then store that and the androidId
        if (getAuthToken() == null || getAndroidId() == null) {
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setAuthToken(fetchAuthToken(false, context));
                    setAndroidId(fetchAuthorizedAndroidId(context));

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

    public String getAuthToken() {
        return authToken;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public interface OnLoadFinishedListener {
        public void onLoadFinished();
    }

    // get the android id from the device
    private String fetchAuthorizedAndroidId(Context context) {
        String[] query = new String[]{"android_id"};
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.google.android.gsf.gservices"), null, null, query, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getColumnCount() >= 2) {
            return Long.toHexString(Long.parseLong(cursor.getString(1))).toUpperCase();
        }

        return null;
    }

    // get out auth token
    private String fetchAuthToken(boolean invalidateToken, Activity activity) {
        String authToken = "null";
        try {
            AccountManager am = AccountManager.get(activity);
            Account[] accounts = am.getAccountsByType("com.google");
            AccountManagerFuture<Bundle> accountManagerFuture;
            if (activity == null) {
                accountManagerFuture = am.getAuthToken(accounts[0], "android", true, null, null);
            } else {
                accountManagerFuture = am.getAuthToken(accounts[0], "android", null, activity, null, null);
            }
            Bundle authTokenBundle = accountManagerFuture.getResult();
            authToken = authTokenBundle.getString(AccountManager.KEY_AUTHTOKEN);
            if (invalidateToken) {
                am.invalidateAuthToken("com.google", authToken);
                authToken = fetchAuthToken(false, activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authToken;
    }
}
