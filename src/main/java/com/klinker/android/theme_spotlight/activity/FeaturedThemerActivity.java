package com.klinker.android.theme_spotlight.activity;

import android.app.Fragment;
import android.os.Bundle;
import com.klinker.android.theme_spotlight.Themers;
import com.klinker.android.theme_spotlight.data.FeaturedThemer;
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
            // TODO need to implement a new fragment
            setCurrentFragment(new Fragment());
        }
    }

    @Override
    public void setupActionbar(int position) {
        getActionBar().setTitle(mFeaturedThemer.getName());
        getActionBar().setIcon(android.R.color.transparent);
    }

    public String getPublisherQuery(String publisher) {
        return "pub:" + publisher;
    }

    @Override
    public void setupDrawerToggle() { }
}
