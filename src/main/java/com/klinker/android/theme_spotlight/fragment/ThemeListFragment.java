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
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.data.AuthToken;

import java.util.List;

public class ThemeListFragment extends ListFragment {

    private static final String TAG = "AbstractThemeFragment";

    public static final String BASE_SEARCH = "base_search_parameter";
    private static final int NUM_THEMES_TO_QUERY = 10;

    private Context mContext;

    private String mBaseSearch;
    private String currentSearch = "";
    private int currentSearchIndex = 0;

    public static ThemeListFragment newInstance(String baseSearch) {
        ThemeListFragment frag = new ThemeListFragment();
        setArguements(frag, baseSearch);
        return frag;
    }

    public static void setArguements(ThemeListFragment frag, String baseSearch) {
        Bundle args = new Bundle();
        args.putString(BASE_SEARCH, baseSearch);
        frag.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseSearch = getArguments().getString(BASE_SEARCH);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                    session.getContext().setAuthSubToken(AuthToken.getAuthToken());
                    session.getContext().setAndroidId(AuthToken.getAndroidId());

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

                            for (Market.App app : apps) {
                                Log.v(TAG, app.toString());
                            }
                        }
                    });
                    session.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // combine the base search and current search param
    private String getSearch(String search) {
        return mBaseSearch + " " + search;
    }
}
