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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.adapter.ScreenshotAdapter;
import com.klinker.android.theme_spotlight.data.FeaturedTheme;
import com.klinker.android.theme_spotlight.data.NetworkIconLoader;
import com.klinker.android.theme_spotlight.view.HorizontalListView;

public class FeaturedThemeFragment extends AuthFragment {

    public static final String EXTRA_THEME = "extra_featured_theme";

    private FeaturedTheme mTheme;

    private View mLayout;
    private ImageView icon;
    private HorizontalListView screenshotList;
    private TextView themeName;
    private TextView themeDescription;
    private Button download;
    private Button viewSource;

    public static FeaturedThemeFragment newInstance(FeaturedTheme theme) {
        FeaturedThemeFragment fragment = new FeaturedThemeFragment();
        setArguements(fragment, theme);
        return fragment;
    }

    public static void setArguements(FeaturedThemeFragment fragment, FeaturedTheme theme) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_THEME, theme);
        fragment.setArguments(bundle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTheme = (FeaturedTheme) getArguments().getSerializable(EXTRA_THEME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_theme, container, false);

        // load all the views for later
        icon = (ImageView) mLayout.findViewById(R.id.icon);
        screenshotList = (HorizontalListView) mLayout.findViewById(R.id.screenshot_list);
        themeName = (TextView) mLayout.findViewById(R.id.theme_name);
        themeDescription = (TextView) mLayout.findViewById(R.id.publisher_name);
        download = (Button) mLayout.findViewById(R.id.download);
        viewSource = (Button) mLayout.findViewById(R.id.view_source);

        return mLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpApp();
    }

    private void setUpApp() {
        // start a new thread to download and cache our icon
        NetworkIconLoader loader = new NetworkIconLoader(mTheme.getIconUrl(), icon, mTheme.getIconUrl());
        new Thread(loader).start();

        themeName.setText(mTheme.getName());

        String description = mTheme.getShortDescription();
        if (description == null) {
            themeDescription.setVisibility(View.GONE);
        } else {
            themeDescription.setText(mTheme.getShortDescription());
        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWebViewer(mTheme.getDownloadUrl());
            }
        });

        if (mTheme.getSourceUrl() != null) {
            viewSource.setVisibility(View.VISIBLE);
            viewSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startWebViewer(mTheme.getSourceUrl());
                }
            });
        } else {
            viewSource.setVisibility(View.GONE);
        }

        screenshotList.setAdapter(new ScreenshotAdapter(getAuthActivity(), mTheme.getDownloadUrl(), screenshotList.getHeight(),
                screenshotList.getWidth() - getResources().getDimensionPixelSize(R.dimen.screenshot_width_padding)));
    }

    private void startWebViewer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
