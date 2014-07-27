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

package com.klinker.android.theme_spotlight.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import com.klinker.android.theme_spotlight.data.AuthToken;
import com.klinker.android.theme_spotlight.data.Settings;

// abstract class for keeping all of my auth stuff. AuthTokens will
// be grabbed in on start vs onCreate so that we can use the callbacks for when
// the auth has finished and ensure that all boilerplate work is done in the
// implementing class
public abstract class AuthActivity extends Activity {

    private AuthToken authToken;
    private Settings settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = Settings.getInstance(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (authToken == null) {
            initAuthToken(this);
        }
    }

    public void initAuthToken(Activity activity) {
        authToken = new AuthToken(activity);

        if (authToken.getAuthToken() == null) {
            authToken.initAuthToken(activity, new AuthToken.OnLoadFinishedListener() {
                @Override
                public void onLoadFinished() {
                    onAuthFinished(authToken);
                }
            });
        } else {
            onAuthFinished(authToken);
        }
    }

    // helper to replace a current view with a new fragment
    public void attachFragment(int resourceId, Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();

        try {
            fragmentManager.beginTransaction()
                    .replace(resourceId, fragment)
                    .commit();
        } catch (IllegalStateException e) {
            // happens when the app is no longer open and fragment has been switched, ignore it
            e.printStackTrace();
        }
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public Settings getSettings() {
        return settings;
    }

    public abstract void onAuthFinished(AuthToken token);
}
