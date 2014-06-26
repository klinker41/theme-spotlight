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
import android.app.FragmentManager;
import android.os.Bundle;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.fragment.ThemeFragment;

public class ThemeActivity extends Activity {

    private ThemeFragment mFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        // inflate the new fragment
        String packageName = getIntent().getStringExtra(ThemeFragment.ARG_PACKAGE_NAME);
        mFragment = ThemeFragment.newInstance(packageName);

        // replace the entire content view of activity with our fragment, that's
        // all that I want to show
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mFragment)
                .commit();
    }
}
