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

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.activity.SpotlightActivity;
import com.klinker.android.theme_spotlight.adapter.ThemeAdapter;

import java.util.ArrayList;
import java.util.List;

public class ThemeListFragment extends AuthFragment {

    private static final String TAG = "ThemeListFragment";

    public static final String BASE_SEARCH = "base_search_parameter";
    public static final int NUM_THEMES_TO_QUERY = 10;
    private static final int FADE_DURATION = 400;
    private static final String EVOLVE_PACKAGE = "com.klinker.android.evolve_sms";
    private static final String TALON_PACKAGE = "com.klinker.android.twitter";

    private SpotlightActivity mContext;
    private Handler mHandler;
    private LayoutInflater mInflater;

    private String mBaseSearch;
    private String currentSearch = "";
    private int currentSearchIndex = 0;

    private RecyclerView mRecyclerView;
    private View mProgressBar;
    private ThemeAdapter mAdapter;

    private boolean isSyncing = false;

    // get an instance of this fragment
    public static ThemeListFragment newInstance(String baseSearch) {
        ThemeListFragment frag = new ThemeListFragment();
        setArguments(frag, baseSearch);
        return frag;
    }

    public ThemeListFragment() {
        // all fragments should contain an empty constructor
    }

    // set up our base search via arguments
    public static void setArguments(ThemeListFragment frag, String baseSearch) {
        Bundle args = new Bundle();
        args.putString(BASE_SEARCH, baseSearch);
        frag.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        superOnCreate(savedInstanceState);
        mBaseSearch = getArguments().getString(BASE_SEARCH);
    }

    public void superOnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (SpotlightActivity) activity;
        mHandler = new Handler();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        superOnCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_theme_list, null);

        mRecyclerView = (RecyclerView) v.findViewById(android.R.id.list);
        setUpRecyclerView();
        mProgressBar = v.findViewById(R.id.loading_progress);

        mAdapter = new ThemeAdapter(this, new ArrayList<Market.App>());
        setRecyclerViewAdapter(mAdapter);

        if (isTwoPane()) {
            v.setBackgroundResource(android.R.color.white);
        }

        return v;
    }

    public boolean isTwoPane() {
        return mContext.isTwoPane();
    }

    public void themeItemClicked(Market.App app) {
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get the themes that we want to display, can only load 10 at a time
        getThemes(currentSearchIndex);
    }

    public void getThemes(int startIndex) {
        getThemes(startIndex, NUM_THEMES_TO_QUERY);
    }

    // get all of the themes in the supplied range
    private void getThemes(final int startIndex, final int length) {
        // network call, so we need to start a thread and then post a callback back to the ui
        // once the call is completed
        new Thread(new Runnable() {
            @Override
            public void run() {
                isSyncing = true;
                try {
                    // create our session to look at themes from
                    MarketSession session = new MarketSession();
                    session.getContext().setAuthSubToken(mContext.getAuthToken().getAuthToken());
                    session.getContext().setAndroidId(mContext.getAuthToken().getAndroidId());

                    // create a simple query
                    final String query = getSearch(currentSearch);
                    Market.AppsRequest appsRequest = Market.AppsRequest.newBuilder()
                            .setQuery(query)
                            .setStartIndex(startIndex)
                            .setEntriesCount(length)
                            .setWithExtendedInfo(true) // get extended info so that we can verify the theme name against
                            .build();                  // either the name evolvesms or talon

                    // pause the loading for a short amount of time, this helps the recycler view
                    // keep up and prevents it from scrolling janky when more views are added
                    try { Thread.sleep(500); } catch (Exception e) { }

                    // post our request
                    session.append(appsRequest, new MarketSession.Callback<Market.AppsResponse>() {
                        @Override
                        public void onResult(Market.ResponseContext context, Market.AppsResponse response) {
                            // response.getAppList() is marked as unmodifiable, we need to change that so we can
                            // strip items out when necessary
                            ArrayList<Market.App> apps = new ArrayList<Market.App>(response.getAppList());

                            // check the first for whether or not it is evolve or talon, and if so
                            // strip it out as we don't want to display it. want to also verify the query
                            // is correct so that later if I choose to do a Klinker Apps featured themer as
                            // an example, it will still show those packages
                            if ((apps.get(0).getPackageName().equals(EVOLVE_PACKAGE) && query.contains(SpotlightActivity.EVOLVE_SMS)) ||
                                    (apps.get(0).getPackageName().equals(TALON_PACKAGE) && query.contains(SpotlightActivity.TALON))) {
                                apps.remove(0);
                            }

                            setApps(apps);
                            isSyncing = false;
                        }
                    });
                    session.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getMoreThemes() {
        currentSearchIndex += NUM_THEMES_TO_QUERY;
        getThemes(currentSearchIndex);

        // recycler views do not support footers, yet... damn
//        if (mRecyclerView.getFooterViewsCount() == 0) {
//            // set a footer to always spin at the bottom of the list
//            ProgressBar spinner = (ProgressBar) mInflater.inflate(R.layout.loading_footer, null);
//            mRecyclerView.addFooterView(spinner);
//        }
    }

    // set the apps to the listview and initialize other parts of the list
    public void setApps(final List<Market.App> apps) {
        // verify the base search so that I don't show junk results
        final String verifyTitle;
        if (mBaseSearch.equals(SpotlightActivity.EVOLVE_SMS)) {
            verifyTitle = "Evolve";
        } else {
            verifyTitle = "Talon";
        }
        
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                setListAdapterPost();

                for (Market.App app : apps) {
                    if (titleVerified(verifyTitle, app)) {
                        mAdapter.add(app, mAdapter.getRealItemCount());
                    }
                }
            }
        });
    }

    // verify that we should add the app to the list. this should occur when the title or description contains
    // the text for evolve or talon, or the base search is based on publisher name
    private boolean titleVerified(String verify, Market.App app) {
        return app.getTitle().contains(verify) ||
                app.getExtendedInfo().getDescription().contains(verify) ||
                mBaseSearch.startsWith("pub:");
    }

    public void setListAdapterPost() {
        // if we haven't yet set an adapter, set it now. If we have already, just
        // notify that our data has changed and it should reload
        if (mAdapter.getRealItemCount() == 0) {
            ObjectAnimator listAnimator = ObjectAnimator.ofFloat(mRecyclerView, View.ALPHA, 0.0f, 1.0f);
            listAnimator.setDuration(FADE_DURATION);
            listAnimator.start();
            ObjectAnimator progressAnimator = ObjectAnimator.ofFloat(mProgressBar, View.ALPHA, 1.0f, 0.0f);
            progressAnimator.setDuration(FADE_DURATION);
            progressAnimator.start();

            // after the first run, immediately get more themes since we can only
            // pull 10 at a time
            getMoreThemes();
        }
    }

    // combine the base search and current search param
    public String getSearch(String search) {
        return mBaseSearch + " " + search;
    }

    public void syncMoreThemes(int position) {
        if (position >= mAdapter.getRealItemCount() - 2 && !isSyncing) {
            isSyncing = true;
            getMoreThemes();
        }
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }
}
