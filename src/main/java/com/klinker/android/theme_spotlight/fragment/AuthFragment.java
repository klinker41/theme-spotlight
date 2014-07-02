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
import android.app.Fragment;
import android.os.Handler;
import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.activity.AuthActivity;
import com.klinker.android.theme_spotlight.data.AuthToken;

public class AuthFragment extends Fragment {

    private AuthActivity mContext;

    public static AuthFragment newInstance() {
        AuthFragment fragment = new AuthFragment();
        return fragment;
    }

    public AuthFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (AuthActivity) activity;
    }

    public AuthToken getAuthToken() {
        return getAuthActivity().getAuthToken();
    }

    public AuthActivity getAuthActivity() {
        return mContext;
    }

    // load the app by creating a new session and requesting by the package name. we can then load
    // just one result and get what we are looking for
    public void loadApp(final String packageName, final Handler handler, final OnAppLoadFinishedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // create our session to look at themes from
                    MarketSession session = new MarketSession();
                    session.getContext().setAuthSubToken(getAuthToken().getAuthToken());
                    session.getContext().setAndroidId(getAuthToken().getAndroidId());

                    // create a simple query
                    String query = getPackageQuery(packageName);
                    Market.AppsRequest appsRequest = Market.AppsRequest.newBuilder()
                            .setQuery(query)
                            .setStartIndex(0)
                            .setEntriesCount(1)
                            .setWithExtendedInfo(true)
                            .build();

                    // post our request
                    session.append(appsRequest, new MarketSession.Callback<Market.AppsResponse>() {
                        @Override
                        public void onResult(Market.ResponseContext context, Market.AppsResponse response) {
                            final Market.App app = response.getAppList().get(0);

                            // post back to the ui thread to update the view
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onLoadFinished(app);
                                }
                            });
                        }
                    });
                    session.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // load comments for the app with the default length of 10. this is an api limitation
    public void loadComments(final Market.App app, final int startIndex, final Handler handler,
                             final OnCommentsLoadFinishedListener listener) {
        loadComments(app, startIndex, ThemeListFragment.NUM_THEMES_TO_QUERY, handler, listener);
    }

    private void loadComments(final Market.App app, final int startIndex, final int entriesCount,
                             final Handler handler, final OnCommentsLoadFinishedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // create our session to look at themes from
                    MarketSession session = new MarketSession();
                    session.getContext().setAuthSubToken(getAuthToken().getAuthToken());
                    session.getContext().setAndroidId(getAuthToken().getAndroidId());

                    // create a simple query
                    Market.CommentsRequest commentsRequest = Market.CommentsRequest.newBuilder()
                            .setAppId(app.getId())
                            .setStartIndex(startIndex)
                            .setEntriesCount(entriesCount)
                            .build();

                    session.append(commentsRequest, new MarketSession.Callback<Market.CommentsResponse>() {
                        @Override
                        public void onResult(Market.ResponseContext context, final Market.CommentsResponse response) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onLoadFinished(response);
                                }
                            });
                        }
                    });
                    session.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String getPackageQuery(String packageName) {
        return "pname:" + packageName;
    }

    public interface OnAppLoadFinishedListener {
        public void onLoadFinished(Market.App app);
    }

    public interface OnCommentsLoadFinishedListener {
        public void onLoadFinished(Market.CommentsResponse response);
    }
}
