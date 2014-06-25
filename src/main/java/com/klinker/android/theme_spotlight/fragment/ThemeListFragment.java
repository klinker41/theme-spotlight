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
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.activity.AuthActivity;
import com.klinker.android.theme_spotlight.adapter.ThemeArrayAdapter;

import java.util.List;

public class ThemeListFragment extends ListFragment {

    private static final String TAG = "AbstractThemeFragment";

    public static final String BASE_SEARCH = "base_search_parameter";
    private static final int NUM_THEMES_TO_QUERY = 10;

    private AuthActivity mContext;
    private Handler mHandler;

    private String mBaseSearch;
    private String currentSearch = "";
    private int currentSearchIndex = 0;

    // get an instance of this fragment
    public static ThemeListFragment newInstance(String baseSearch) {
        ThemeListFragment frag = new ThemeListFragment();
        setArguements(frag, baseSearch);
        return frag;
    }

    public ThemeListFragment() {

    }

    // set up our base search via arguments
    public static void setArguements(ThemeListFragment frag, String baseSearch) {
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
        mContext = (AuthActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHandler = new Handler();

        // get the themes that we want to display, can only load 10 at a time
        getThemes(currentSearchIndex);
        return super.onCreateView(inflater, container, savedInstanceState);
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
                try {
                    // create our session to look at themes from
                    MarketSession session = new MarketSession();
                    session.getContext().setAuthSubToken(mContext.getAuthToken().getAuthToken());
                    session.getContext().setAndroidId(mContext.getAuthToken().getAndroidId());

                    // create a simple query
                    String query = getSearch(currentSearch);
                    Market.AppsRequest appsRequest = Market.AppsRequest.newBuilder()
                            .setQuery(query)
                            .setStartIndex(startIndex)
                            .setEntriesCount(length)
                            .setWithExtendedInfo(false) // don't need extended info, this will slow us down
                            .build();

                    // post our request
                    session.append(appsRequest, new MarketSession.Callback<Market.AppsResponse>() {
                        @Override
                        public void onResult(Market.ResponseContext context, Market.AppsResponse response) {
                            List<Market.App> apps = response.getAppList();
                            setApps(apps);
                        }
                    });
                    session.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // set the apps to the listview and initialize other parts of the list
    private void setApps(final List<Market.App> apps) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                setListAdapter(new ThemeArrayAdapter(mContext, apps));
            }
        });
    }

    // combine the base search and current search param
    public String getSearch(String search) {
        return mBaseSearch + " " + search;
    }
}
