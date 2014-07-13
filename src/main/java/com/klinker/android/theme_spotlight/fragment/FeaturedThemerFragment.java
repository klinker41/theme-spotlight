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
import com.klinker.android.theme_spotlight.Themers;
import com.klinker.android.theme_spotlight.activity.SpotlightActivity;
import com.klinker.android.theme_spotlight.adapter.FeaturedThemerAdapter;

public class FeaturedThemerFragment extends AuthFragment {

    private static final String TAG = "FeaturedThemerFragment";

    private SpotlightActivity mContext;
    private LayoutInflater mInflater;

    private RecyclerView mRecyclerView;
    private FeaturedThemerAdapter mAdapter;
    private View mProgressBar;

    // create new instance of our featured list
    public static FeaturedThemerFragment newInstance() {
        FeaturedThemerFragment frag = new FeaturedThemerFragment();
        return frag;
    }

    public FeaturedThemerFragment() {
        // all fragments should contain an empty constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (SpotlightActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        superOnCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_theme_list, null);

        mRecyclerView = (RecyclerView) v.findViewById(android.R.id.list);
        setUpRecyclerView();
        mProgressBar = v.findViewById(R.id.loading_progress);
        mProgressBar.setVisibility(View.GONE);

        mAdapter = new FeaturedThemerAdapter(this, Themers.FEATURED_THEMERS);
        setRecyclerViewAdapter(mAdapter);

        if (((SpotlightActivity) getActivity()).isTwoPane()) {
            v.setBackgroundResource(android.R.color.white);
        }

        return v;
    }

    public void superOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
    }

    public boolean isTwoPane() {
        return mContext.isTwoPane();
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
        mRecyclerView.setAdapter(adapter);
    }
}
