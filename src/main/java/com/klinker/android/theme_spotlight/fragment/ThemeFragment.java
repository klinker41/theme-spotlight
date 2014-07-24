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

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.adapter.CommentsAdapter;
import com.klinker.android.theme_spotlight.adapter.ScreenshotRecyclerAdapter;
import com.klinker.android.theme_spotlight.data.FeaturedTheme;
import com.klinker.android.theme_spotlight.data.IconLoader;
import com.klinker.android.theme_spotlight.util.PackageUtils;

import java.util.ArrayList;
import java.util.List;

public class ThemeFragment extends AuthFragment {

    private static final String TAG = "ThemeFragment";
    public static final String ARG_PACKAGE_NAME = "package_name";

    private Handler mHandler;

    private View mLayout;
    private String mPackageName;

    private ImageView icon;
    private RecyclerView screenshotList;
    private TextView themeName;
    private TextView publisherName;
    private Button download;
    private View titleHolder;

    private ListView commentsList;
    private CommentsAdapter commentsAdapter;
    private List<Market.Comment> mComments;
    private int mCommentStartIndex = 0;

    public static ThemeFragment newInstance(String packageName) {
        ThemeFragment fragment = new ThemeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PACKAGE_NAME, packageName);
        fragment.setArguments(args);
        return fragment;
    }

    public ThemeFragment() {
        mComments = new ArrayList<Market.Comment>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        mPackageName = getArguments().getString(ARG_PACKAGE_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_theme, null);

        icon = (ImageView) mLayout.findViewById(R.id.icon);
        screenshotList = (RecyclerView) mLayout.findViewById(R.id.screenshot_list);
        themeName = (TextView) mLayout.findViewById(R.id.theme_name);
        publisherName = (TextView) mLayout.findViewById(R.id.publisher_name);
        commentsList = (ListView) mLayout.findViewById(R.id.review_list);
        download = (Button) mLayout.findViewById(R.id.download);
        titleHolder = mLayout.findViewById(R.id.theme_name_holder);

        LinearLayoutManager manager = new LinearLayoutManager(getAuthActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        screenshotList.setLayoutManager(manager);

        return mLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadApp(mPackageName, mHandler, new OnAppLoadFinishedListener() {
            @Override
            public void onLoadFinished(Market.App app) {
                setApp(app);
            }
        });
    }

    // set up the view we want to show finally, need to post back to the ui thread
    // since we queried on a separate thread
    public void setApp(final Market.App app) {
        icon.setTag(app.getId());
        new Thread(new IconLoader(app, icon, getAuthActivity(), null, Market.GetImageRequest.AppImageUsage.ICON))
                .start();

        themeName.setText(app.getTitle());
        publisherName.setText(app.getCreator());

        screenshotList.setAdapter(new ScreenshotRecyclerAdapter(getAuthActivity(), app, screenshotList.getHeight(),
                screenshotList.getWidth() - getResources().getDimensionPixelSize(R.dimen.screenshot_width_padding)));

        if (commentsList != null && commentsAdapter == null) {
            loadComments(app, mCommentStartIndex, mHandler, commentsListener);
        }

        titleHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(app.getTitle())
                        .setMessage(getAppDetails(app))
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + app.getPackageName())));
            }
        });

        if (PackageUtils.doesPackageExist(getActivity(), mPackageName)) {
            download.setText(getString(R.string.installed));
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FeaturedTheme.ACTION);
                    intent.putExtra(FeaturedTheme.ARG_PACKAGE_NAME, mPackageName);
                    getActivity().sendBroadcast(intent);

                    Toast.makeText(getActivity(), getString(R.string.theme_set), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            if (app.hasPrice() && !download.getText().toString().endsWith(")")) {
                download.setText(download.getText().toString() + " (" + app.getPrice().replace("US", "") + ")");
            }
        }
    }

    // set up the comments list and update it as you scroll down it
    public void setComments(Market.CommentsResponse comments) {
        mComments.addAll(comments.getCommentsList());

        if (commentsAdapter == null) {
            commentsAdapter = new CommentsAdapter(getAuthActivity(), mComments);
            commentsList.setAdapter(commentsAdapter);
        } else {
            commentsAdapter.notifyDataSetChanged();
        }
    }

    // get the app details that I feel are relevant for it
    private Spanned getAppDetails(Market.App app) {
        StringBuilder builder = new StringBuilder();
        builder.append(boldTextHtml("Publisher: ") + app.getCreator() + "<br>");

        if (app.hasRating()) {
            builder.append(boldTextHtml("Total Rating: ") + app.getRating() + "<br>");
            builder.append(boldTextHtml("Number of Ratings: ") + app.getRatingsCount() + "<br>");
        }

        if (app.hasPrice()) {
            builder.append(boldTextHtml("Price: ") + app.getPrice() + "<br>");
        }

        if (app.hasExtendedInfo()) {
            Market.App.ExtendedInfo info = app.getExtendedInfo();

            if (info.hasDownloadsCount()) {
                builder.append(boldTextHtml("Downloads: ") + info.getDownloadsCountText() + "<br>");
            }

            if (info.hasInstallSize()) {
                builder.append(boldTextHtml("Size: ") + info.getInstallSize() / 1000.0 + " kb<br><br>");
            }

            if (info.hasDescription()) {
                builder.append(boldTextHtml("Description: ") + info.getDescription().replace("\n", "<br>")
                        .replace("Recent changes:", "<b>Recent Changes: </b>")
                        .replace("Content rating: ", "<b>Content Rating: </b>"));
            }
        }

        return Html.fromHtml(builder.toString());
    }

    // return a bolded text string to show when using Html.fromHtml()
    private String boldTextHtml(String text) {
        return "<b>" + text + "</b>";
    }

    private OnCommentsLoadFinishedListener commentsListener = new OnCommentsLoadFinishedListener() {
        @Override
        public void onLoadFinished(Market.CommentsResponse response) {
            setComments(response);
        }
    };

    @Override
    public boolean isSearchable() {
        return false;
    }
}
