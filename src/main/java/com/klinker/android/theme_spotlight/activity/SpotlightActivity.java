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

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.data.AuthToken;
import com.klinker.android.theme_spotlight.fragment.FeaturedThemeListFragment;
import com.klinker.android.theme_spotlight.fragment.ThemeListFragment;

public class SpotlightActivity extends AuthActivity {

    private static final String TAG = "SpotlightActivity";

    // fragment positions in the drawer
    private static final int EVOLVE_FRAGMENT = 0;
    private static final int TALON_FRAGMENT = 1;
    private static final int FEATURED_FRAGMENT = 2;

    // typefaces to use in the drawer
    private static final Typeface LIGHT_TEXT = Typeface.create("sans-serif-light", Typeface.NORMAL);
    private static final Typeface BOLD_TEXT = Typeface.create("sans-serif", Typeface.BOLD);

    // base searches
    private static final String EVOLVE_SMS = "EvolveSMS";
    private static final String TALON = "Talon theme";

    private Context mContext;
    private Handler mHandler;

    // stuff to manage the drawer
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;
    private int mIcon;
    private View[] drawerButtons;

    // current fragment being shown
    private Fragment mFragment;
    private int currentPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotlight);

        mContext = this;
        mHandler = new Handler();

        // initialize the drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
                R.drawable.ic_drawer, R.string.app_name, R.string.evolve_sms_themes) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                getActionBar().setIcon(mIcon);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.app_name);
                getActionBar().setIcon(R.drawable.spotlight_logo);
                invalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawer.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        setupDrawerButtons();
    }

    @Override
    public void onAuthFinished(AuthToken token) {
        switchFragments(currentPosition);
    }

    // perform transaction and switch the old fragment for the new one
    public void switchFragments(int position) {
        currentPosition = position;

        // Create a new fragment
        switch (position) {
            case EVOLVE_FRAGMENT:
                mFragment = ThemeListFragment.newInstance(EVOLVE_SMS);
                break;
            case TALON_FRAGMENT:
                mFragment = ThemeListFragment.newInstance(TALON);
                break;
            case FEATURED_FRAGMENT:
            default:
                mFragment = FeaturedThemeListFragment.newInstance();
                break;
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mFragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        boldDrawerItem(position);
        setupActionbar(position);
        mDrawer.closeDrawer(Gravity.START);
    }

    // set title and icon in the actionbar
    public void setupActionbar(int position) {
        switch (position) {
            case EVOLVE_FRAGMENT:
                setTitle(R.string.evolve_sms_themes);
                mIcon = R.drawable.evolve_logo;
                break;
            case TALON_FRAGMENT:
                setTitle(R.string.talon_themes);
                mIcon = R.drawable.talon_logo;
                break;
            case FEATURED_FRAGMENT:
                setTitle(R.string.featured_themers);
                mIcon = R.drawable.featured_logo;
                break;
            default:
                setTitle(R.string.app_name);
                mIcon = R.drawable.spotlight_logo;
        }

        mTitle = getTitle().toString();
        getActionBar().setIcon(mIcon);
    }

    // initialize buttons in drawer and handle clicking on them
    private void setupDrawerButtons() {
        drawerButtons = new View[3];
        drawerButtons[0] = findViewById(R.id.evolve_button);
        drawerButtons[1] = findViewById(R.id.talon_button);
        drawerButtons[2] = findViewById(R.id.featured_button);

        drawerButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragments(0);
            }
        });
        drawerButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragments(1);
            }
        });
        drawerButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragments(2);
            }
        });
    }

    private void boldDrawerItem(int position) {
        // Loop through and bold the correct items
        for (int i = 0; i < drawerButtons.length; i++) {
            if (i == position) {
                ((TextView) drawerButtons[i]).setTypeface(BOLD_TEXT);
            } else {
                ((TextView) drawerButtons[i]).setTypeface(LIGHT_TEXT);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawer.isDrawerOpen(Gravity.START);
        // TODO hide search icon when opened
        // menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    // handle settings button clicked in the drawer
    public void onSettingsClicked(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    // handle send feedback button clicked in the drawer
    public void onFeedbackClicked(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, "support@klinkerapps.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));

        startActivity(Intent.createChooser(intent, getString(R.string.send_feedback)));
    }

    public int getActionbarIcon() {
        return mIcon;
    }

    public DrawerLayout getNavigationDrawer() {
        return mDrawer;
    }

    public Fragment getCurrentFragment() {
        return mFragment;
    }
}
