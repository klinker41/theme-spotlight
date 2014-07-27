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
import android.view.MenuItem;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.adapter.ChangelogAdapter;
import com.klinker.android.theme_spotlight.data.Settings;
import com.klinker.android.theme_spotlight.util.XmlChangelogUtils;

public class SettingsActivity extends PreferenceActivity {

    private static final String TAG = "SettingsActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        superOnCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        try {
            initAboutPreference();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // used for testing
    public void superOnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Preference getAboutPreference() {
        return findPreference("preference_about");
    }

    public void initAboutPreference() throws PackageManager.NameNotFoundException {
        Preference about = getAboutPreference();

        // set correct summary for version number
        String versionName = getVersionNumber();
        String current = getString(R.string.version_number);
        about.setSummary(current.replace("%s", versionName));

        about.setOnPreferenceClickListener(aboutClick);
    }

    public String getVersionNumber() throws PackageManager.NameNotFoundException {
        return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    }

    private Preference.OnPreferenceClickListener aboutClick = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            Spanned[] changelogItems = XmlChangelogUtils.parse(SettingsActivity.this);
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle(R.string.changelog)
                    .setAdapter(new ChangelogAdapter(SettingsActivity.this, changelogItems), null)
                    .show();

            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onStop() {
        Settings.getInstance(this, true);
        super.onStop();
    }
}
