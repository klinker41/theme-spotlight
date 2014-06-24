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

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.klinker.android.theme_spotlight.util.AuthUtils;

public class AuthToken {

    private static final String TAG = "AuthToken";

    private static final String AUTH_TOKEN_PREF = "accounts_auth_token";
    private static final String ANDROID_ID_PREF = "accounts_android_id";

    private static String authToken;
    private static String androidId;

    public static void initAuthToken(final Activity context, final OnLoadFinishedListener listener) {
        // attempt to get stored information in available
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        authToken = sharedPreferences.getString(AUTH_TOKEN_PREF, null);
        androidId = sharedPreferences.getString(ANDROID_ID_PREF, null);

        // if tokens are not available (ie this is first run, then we need to fetch
        // them. We authenticate with Google Play Services and get the correct account
        // token and then store that and the androidId in shared prefs for next time
        if (authToken == null || androidId == null) {
            final Handler handler = new Handler();
            AuthUtils.getAuthToken(context, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> result) {
                    try {
                        Bundle bundle = result.getResult();
                        authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                        storeToPrefs(context);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onLoadFinished();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            androidId = AuthUtils.getAndroidID(context);
        } else {
            listener.onLoadFinished();
        }
    }

    // can't be initialized
    private AuthToken() { }

    private static void storeToPrefs(Context context) {
        // update these in shared prefs
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(AUTH_TOKEN_PREF, authToken)
                .putString(ANDROID_ID_PREF, androidId)
                .commit();
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static String getAndroidId() {
        return androidId;
    }

    public interface OnLoadFinishedListener {
        public void onLoadFinished();
    }
}
