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

public class AuthToken {

    private static final String TAG = "AuthToken";

    private static final String AUTH_USERNAME_PREF = "accounts_username";
    private static final String AUTH_PASSWORD_PREF = "accounts_password";
    private static final String ANDROID_ID_PREF = "accounts_android_id";

    private String username;
    private String password;
    private String androidId;

    public AuthToken(Context context) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        username = sharedPreferences.getString(AUTH_USERNAME_PREF, null);
        password = sharedPreferences.getString(AUTH_PASSWORD_PREF, null);
        androidId = sharedPreferences.getString(ANDROID_ID_PREF, null);

        if (username == null || password == null || androidId == null) {
            // TODO launcher login activity
            username = "jklinker1@gmail.com";
            password = "klinker1127";

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    GooglePlayAPI api = new GooglePlayAPI(username, password);
//
//                    try {
//                        api.checkin();
//                        androidId = api.getAndroidID();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
        }
    }

    public AuthToken(Context context, String username, String password, String androidId) {
        this.username = username;
        this.password = password;
        this.androidId = androidId;

        storeToPrefs(context);
    }

    private void storeToPrefs(Context context) {
        // update these in shared prefs
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(AUTH_USERNAME_PREF, username)
                .putString(AUTH_PASSWORD_PREF, password)
                .putString(ANDROID_ID_PREF, androidId)
                .commit();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAndroidId() {
        return androidId;
    }

    public interface  OnLoadFinishedListener {
        public void onLoadFinished();
    }
}
