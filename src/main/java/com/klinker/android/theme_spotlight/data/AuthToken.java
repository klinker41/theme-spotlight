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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.klinker.android.theme_spotlight.util.AuthUtils;

public class AuthToken {

    private static final String TAG = "AuthToken";

    private static final String AUTH_TOKEN_PREF = "accounts_username";
    private static final String ANDROID_ID_PREF = "accounts_android_id";

    private String token;
    private String androidId;

    public AuthToken(final Context context, final OnLoadFinishedListener listener) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        token = sharedPreferences.getString(AUTH_TOKEN_PREF, null);
        androidId = sharedPreferences.getString(ANDROID_ID_PREF, null);

        // if this is the first time running and they aren't yet stored, create and store them
        if (token == null || androidId == null) {
            Log.v(TAG, "credentials null, getting new credentials");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        token = AuthUtils.getAuthToken(context);
                        androidId = AuthUtils.getAndroidID(context);

                        // save values to shared preferences
                        sharedPreferences.edit()
                                .putString(AUTH_TOKEN_PREF, token)
                                .putString(ANDROID_ID_PREF, androidId)
                                .commit();

                        Log.v(TAG, "token: " + token + ", androidId: " + androidId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    listener.onLoadFinished();
                }
            }).start();
        } else {
            listener.onLoadFinished();
        }
    }

    public String getToken() {
        return token;
    }

    public String getAndroidId() {
        return androidId;
    }

    public interface  OnLoadFinishedListener {
        public void onLoadFinished();
    }
}
