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
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.adapter.ScreenshotRecyclerAdapter;
import com.klinker.android.theme_spotlight.data.FeaturedTheme;
import com.klinker.android.theme_spotlight.data.NetworkIconLoader;
import com.klinker.android.theme_spotlight.util.AppUtils;
import com.klinker.android.theme_spotlight.util.PackageUtils;

public class FeaturedThemeFragment extends AuthFragment {

    public static final String EXTRA_THEME = "extra_featured_theme";

    private FeaturedTheme mTheme;

    private View mLayout;
    private ImageView icon;
    private RecyclerView screenshotList;
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

        icon = (ImageView) mLayout.findViewById(R.id.icon);
        screenshotList = (RecyclerView) mLayout.findViewById(R.id.screenshot_list);
        themeName = (TextView) mLayout.findViewById(R.id.theme_name);
        themeDescription = (TextView) mLayout.findViewById(R.id.publisher_name);
        download = (Button) mLayout.findViewById(R.id.download);
        viewSource = (Button) mLayout.findViewById(R.id.view_source);

        LinearLayoutManager manager = new LinearLayoutManager(getAuthActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        screenshotList.setLayoutManager(manager);

        setUpApp();

        return mLayout;
    }

    private void setUpApp() {
        if (mTheme == null) {
            return;
        }

        NetworkIconLoader loader = new NetworkIconLoader(getAuthActivity(), mTheme.getIconUrl(), icon, mTheme.getIconUrl());
        new Thread(loader).start();

        themeName.setText(mTheme.getName());

        String description = mTheme.getShortDescription();
        if (description == null) {
            themeDescription.setVisibility(View.GONE);
        } else {
            themeDescription.setText(mTheme.getShortDescription());
        }

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                screenshotList.setAdapter(new ScreenshotRecyclerAdapter(getAuthActivity(), mTheme.getScreenshotUrl(),
                        screenshotList.getHeight(), screenshotList.getWidth()));
            }
        }, 100);

        if (AppUtils.checkValidTheme(mTheme.getName(), mTheme.getShortDescription())) {
            if (PackageUtils.doesPackageExist(getActivity(), mTheme.getPackageName())) {
                download.setText(getString(R.string.apply));
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FeaturedTheme.ACTION);
                        intent.putExtra(FeaturedTheme.ARG_PACKAGE_NAME, mTheme.getPackageName());
                        getActivity().sendBroadcast(intent);

                        Toast.makeText(getActivity(), getString(R.string.theme_set), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startWebViewer(mTheme.getDownloadUrl());
                    }
                });
            }
        } else {
            download.setEnabled(false);
            if (PackageUtils.doesPackageExist(getActivity(), mTheme.getPackageName())) {
                download.setText(R.string.installed);
            } else {
                download.setText(R.string.not_a_theme);
            }
        }
    }

    private void startWebViewer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean isSearchable() {
        return false;
    }
}
