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

import android.os.Bundle;
import android.view.MenuItem;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.data.AuthToken;
import com.klinker.android.theme_spotlight.fragment.ThemeFragment;

public class ThemeActivity extends AuthActivity {

    private ThemeFragment mFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        String packageName = getIntent().getStringExtra(ThemeFragment.ARG_PACKAGE_NAME);
        mFragment = ThemeFragment.newInstance(packageName);
        attachFragment(R.id.content_frame, mFragment);

        // show back arrow on actionbar
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onAuthFinished(AuthToken token) {
        // do nothing here, auth will finish instantly since this is coming from another
        // auth activity
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}
