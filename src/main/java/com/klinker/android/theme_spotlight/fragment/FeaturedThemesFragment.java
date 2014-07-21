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

package com.klinker.android.theme_spotlight.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.activity.SpotlightActivity;
import com.klinker.android.theme_spotlight.adapter.FeaturedThemeAdapter;
import com.klinker.android.theme_spotlight.data.FeaturedTheme;
import com.klinker.android.theme_spotlight.data.FeaturedThemer;

public class FeaturedThemesFragment extends AuthFragment {

    private static final String FEATURED_THEMER = "extra_featured_themer";

    private FeaturedThemer mThemer;
    private SpotlightActivity mContext;
    private RecyclerView mRecyclerView;
    private View mProgressBar;
    private FeaturedThemeAdapter mAdapter;

    public static FeaturedThemesFragment newInstance(FeaturedThemer featuredThemer) {
        FeaturedThemesFragment frag = new FeaturedThemesFragment();
        setArguments(frag, featuredThemer);
        return frag;
    }

    public static void setArguments(FeaturedThemesFragment frag, FeaturedThemer featuredThemer) {
        Bundle args = new Bundle();
        args.putSerializable(FEATURED_THEMER, featuredThemer);
        frag.setArguments(args);
    }

    public FeaturedThemesFragment() {
        // all fragments should contain an empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        superOnCreate(savedInstanceState);
        mThemer = (FeaturedThemer) getArguments().getSerializable(FEATURED_THEMER);
    }

    // for testing
    public void superOnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (SpotlightActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        superOnCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_theme_list, null);

        mRecyclerView = (RecyclerView) v.findViewById(android.R.id.list);
        setUpRecyclerView();
        mProgressBar = v.findViewById(R.id.loading_progress);

        mAdapter = new FeaturedThemeAdapter(this, mThemer.getThemes());
        setRecyclerViewAdapter(mAdapter);

        if (isTwoPane()) {
            v.setBackgroundColor(getResources().getColor(android.R.color.white));
        }

        return v;
    }

    public boolean isTwoPane() {
        return mContext.isTwoPane();
    }

    public void themeItemClicked(FeaturedTheme app) {
        mContext.themeItemClicked(app);
    }

    public View superOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setAdapter(adapter);
        mProgressBar.setVisibility(View.GONE);
    }

    public void setThemer(FeaturedThemer themer) {
        mThemer = themer;
    }

    @Override
    public boolean isSearchable() {
        return false;
    }
}
