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

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.klinker.android.theme_spotlight.Themers;
import com.klinker.android.theme_spotlight.data.FeaturedThemer;
import com.klinker.android.theme_spotlight.fragment.FeaturedThemesFragment;
import com.klinker.android.theme_spotlight.fragment.ThemeListFragment;

public class FeaturedThemerActivity extends SpotlightActivity {

    public static final String EXTRA_THEMER_POSITION = "extra_themer_position";

    private FeaturedThemer mFeaturedThemer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lockDrawer(true);

        int themer = getIntent().getIntExtra(EXTRA_THEMER_POSITION, 0);
        mFeaturedThemer = Themers.FEATURED_THEMERS[themer];

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setUpFragment(int position) {
        if (mFeaturedThemer.isFromPlaystore()) {
            setCurrentFragment(ThemeListFragment.newInstance(getPublisherQuery(mFeaturedThemer.getPlayStoreDeveloperName())));
        } else {
            setCurrentFragment(FeaturedThemesFragment.newInstance(mFeaturedThemer));
        }
    }

    @Override
    public void setupActionbar(int position) {
        getActionBar().setTitle(mFeaturedThemer.getName());
        getActionBar().setIcon(new ColorDrawable(android.R.color.transparent));
    }

    public String getPublisherQuery(String publisher) {
        return "pub:" + publisher;
    }

    @Override
    public void setupDrawerToggle() {
    }
}
