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

package com.klinker.android.theme_spotlight.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.activity.FeaturedThemeActivity;
import com.klinker.android.theme_spotlight.data.FeaturedTheme;
import com.klinker.android.theme_spotlight.data.NetworkIconLoader;
import com.klinker.android.theme_spotlight.fragment.FeaturedThemeFragment;
import com.klinker.android.theme_spotlight.fragment.FeaturedThemesFragment;

public class FeaturedThemeAdapter extends AbstractRecyclerAdapter {

    private FeaturedThemesFragment fragment;
    private FeaturedTheme[] themes;

    public FeaturedThemeAdapter(FeaturedThemesFragment fragment, FeaturedTheme[] themes) {
        super();
        this.fragment = fragment;
        this.themes = themes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.theme_item, parent, false);
        final ViewHolder holder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeaturedTheme theme = themes[holder.position];

                // if this is a single pane view, then start a new activity to display our theme
                // if this is a dual pane view, then post this to the spotlight activity themeItemClicked
                // where we will then display that theme in a fragment on the screen
                if (fragment.isTwoPane()) {
                    fragment.themeItemClicked(theme);
                } else {
                    Intent intent = new Intent(fragment.getActivity(), FeaturedThemeActivity.class);
                    intent.putExtra(FeaturedThemeFragment.EXTRA_THEME, theme);
                    fragment.getActivity().startActivity(intent);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FeaturedTheme theme = themes[position];

        holder.position = position;
        holder.title.setText(theme.getName());

        String description = theme.getShortDescription();
        if (description != null) {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(theme.getShortDescription());
        } else {
            holder.description.setVisibility(View.GONE);
        }

        // this is a holder so that we can confirm we are still in the correct
        // position after the icon has loaded
        holder.icon.setTag(theme.getIconUrl());

        // check if we already have this item stored
        Bitmap icon = getBitmapFromMemCache(theme.getIconUrl());
        if (icon != null) {
            holder.icon.setImageBitmap(icon);
        } else {
            holder.icon.setImageDrawable(new ColorDrawable(android.R.color.transparent));

            // start a new thread to download and cache our icon
            NetworkIconLoader loader = new NetworkIconLoader(fragment.getAuthActivity(), theme.getIconUrl(), holder.icon, theme.getIconUrl());
            new Thread(loader).start();
        }
    }

    @Override
    public int getItemCount() {
        return themes.length;
    }
}
