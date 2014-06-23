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

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.Spanned;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.adapter.ChangelogAdapter;
import com.klinker.android.theme_spotlight.util.XmlChangelogUtils;

public class SettingsActivity extends PreferenceActivity {

    private static final String TAG = "SettingsActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // add the preferences from xml
        addPreferencesFromResource(R.xml.preferences);

        try {
            Preference about = findPreference("preference_about");

            // set correct summary for version number
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            about.setSummary(about.getSummary().toString().replace("%s", versionName));

            // show changelog when item is clicked
            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Spanned[] changelogItems = XmlChangelogUtils.parse(SettingsActivity.this);
                    new AlertDialog.Builder(SettingsActivity.this)
                            .setTitle(R.string.changelog)
                            .setAdapter(new ChangelogAdapter(SettingsActivity.this, changelogItems), null)
                            .show();

                    return true;
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
