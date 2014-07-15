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
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.data.AuthToken;
import com.klinker.android.theme_spotlight.data.FeaturedTheme;
import com.klinker.android.theme_spotlight.fragment.FeaturedThemeFragment;
import com.klinker.android.theme_spotlight.fragment.FeaturedThemerFragment;
import com.klinker.android.theme_spotlight.fragment.ThemeFragment;
import com.klinker.android.theme_spotlight.fragment.ThemeListFragment;
import com.klinker.android.theme_spotlight.util.Utils;

public class SpotlightActivity extends AuthActivity {

    private static final String TAG = "SpotlightActivity";

    // fragment positions in the drawer
    private static final int EVOLVE_FRAGMENT = 0;
    private static final int TALON_FRAGMENT = 1;
    private static final int FEATURED_FRAGMENT = 2;
    private static final String CURRENT_FRAGMENT = "current_fragment";

    // typefaces to use in the drawer
    private static final Typeface LIGHT_TEXT = Typeface.create("sans-serif-light", Typeface.NORMAL);
    private static final Typeface BOLD_TEXT = Typeface.create("sans-serif", Typeface.BOLD);

    // base searches
    public static final String EVOLVE_SMS = "EvolveSMS";
    public static final String TALON = "Talon theme";

    // stuff to manage the drawer
    private View contentHolder;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;
    private int mIcon;
    private TextView[] drawerButtons;

    // current fragment being shown
    private Fragment mFragment;
    private TextView selectItem;
    private int currentPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotlight);

        contentHolder = findViewById(R.id.content_holder);
        selectItem = (TextView) findViewById(R.id.select_item_label);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setupDrawerToggle();
        setupDrawerButtons();

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);
        } else {
            currentPosition = 0;
        }
    }

    @Override
    public void onAuthFinished(AuthToken token) {
        switchFragments(currentPosition);
    }

    // perform transaction and switch the old fragment for the new one
    public void switchFragments(int position) {
        currentPosition = position;
        setUpFragment(position);
        showDualPane(mFragment);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mFragment);
        replaceThemeWithBlank(transaction).commit();

        showTextLabel(true);

        // Highlight the selected item, update the author, and close the drawer
        boldDrawerItem(position);
        setupActionbar(position);
        mDrawer.closeDrawer(Gravity.START);
    }

    public void setupDrawerToggle() {
        // initialize the drawer
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

            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                int totalSlide = getResources().getDimensionPixelSize(R.dimen.total_content_slide_distance);
                float currentSlide = slideOffset * totalSlide;
                contentHolder.setTranslationX(currentSlide);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawer.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    public void setUpFragment(int position) {
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
                mFragment = FeaturedThemerFragment.newInstance();
                break;
        }
    }

    // handle whether or not view should be shown as two pane
    private void showDualPane(Fragment fragment) {
        View themeFrame = findViewById(R.id.theme_frame);
        if (themeFrame != null) {
            View contentFrame = findViewById(R.id.content_frame);
            LinearLayout.LayoutParams params;
            if (fragment instanceof FeaturedThemerFragment) {
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                themeFrame.setVisibility(View.GONE);
            } else {
                params = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.tablet_list_width), ViewGroup.LayoutParams.MATCH_PARENT);
                themeFrame.setVisibility(View.VISIBLE);
            }
            contentFrame.setLayoutParams(params);
        }
    }

    // set author and icon in the actionbar
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
        drawerButtons = new TextView[3];
        drawerButtons[0] = (TextView) findViewById(R.id.evolve_button);
        drawerButtons[1] = (TextView) findViewById(R.id.talon_button);
        drawerButtons[2] = (TextView) findViewById(R.id.featured_button);

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

    // Loop through and bold the correct items
    private void boldDrawerItem(int position) {
        for (int i = 0; i < drawerButtons.length; i++) {
            if (i == position) {
                drawerButtons[i].setTypeface(BOLD_TEXT);

                if (Utils.isAndroidL()) {
                    drawerButtons[i].setTextColor(getResources().getColor(R.color.primary_color));
                }
            } else {
                drawerButtons[i].setTypeface(LIGHT_TEXT);

                if (Utils.isAndroidL()) {
                    drawerButtons[i].setTextColor(getResources().getColor(R.color.drawer_text_color));
                }
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(CURRENT_FRAGMENT, currentPosition);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
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

    // allows for locking the drawer outside the activity
    public void lockDrawer(boolean locked) {
        mDrawer.setDrawerLockMode(locked ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    // whether or not to show the hint text on a tablet
    public void showTextLabel(boolean show) {
        if (isTwoPane()) {
            if (show) {
                selectItem.setVisibility(View.VISIBLE);
            } else {
                selectItem.setVisibility(View.GONE);
            }
        }
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

        // create a chooser to handle the intent and attach a author
        startActivity(Intent.createChooser(intent, getString(R.string.send_feedback)));
    }

    // this will be called from the fragment when an item is clicked and two pane is true
    public void themeItemClicked(Market.App item) {
        String packageName = item.getPackageName();

        // attach the theme fragment to the current frame since we are two pane
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.theme_frame, ThemeFragment.newInstance(packageName))
                .commit();

        showTextLabel(false);
    }

    // called when clicking on a theme in a specific featured themers arsenal
    public void themeItemClicked(FeaturedTheme theme) {
        // attach the theme fragment to the current frame since we are two pane
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.theme_frame, FeaturedThemeFragment.newInstance(theme))
                .commit();

        showTextLabel(false);
    }

    private FragmentTransaction replaceThemeWithBlank(FragmentTransaction transaction) {
        if (isTwoPane()) {
            transaction.replace(R.id.theme_frame, new Fragment());
        }
        return transaction;
    }

    public int getActionbarIcon() {
        return mIcon;
    }

    public Fragment getCurrentFragment() {
        return mFragment;
    }

    public void setCurrentFragment(Fragment fragment) {
        mFragment = fragment;
    }

    public boolean isTwoPane() {
        View themeFrame = findViewById(R.id.theme_frame);
        return themeFrame != null && themeFrame.getVisibility() == View.VISIBLE;
    }
}
