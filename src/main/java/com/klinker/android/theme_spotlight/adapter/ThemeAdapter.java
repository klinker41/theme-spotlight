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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.activity.ThemeActivity;
import com.klinker.android.theme_spotlight.data.IconLoader;
import com.klinker.android.theme_spotlight.fragment.ThemeFragment;
import com.klinker.android.theme_spotlight.fragment.ThemeListFragment;

import java.util.List;

public class ThemeAdapter extends AbstractCachingRecyclerAdapter {

    private static final String TAG = "ThemeAdapter";
    private final ThemeListFragment fragment;
    private final List<Market.App> items;
    private int defaultViewHeight;

    public ThemeAdapter(ThemeListFragment fragment, List<Market.App> items) {
        super();
        this.fragment = fragment;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        // + 1 provides an extra view to be used as a loading footer
        return items.size() + 1;
    }

    public int getRealItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.theme_item, parent, false);
        final ViewHolder holder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.position != getRealItemCount()) {
                    Market.App clickedApp = items.get(holder.position);

                    // if this is a single pane view, then start a new activity to display our theme
                    // if this is a dual pane view, then post this to the spotlight activity themeItemClicked
                    // where we will then display that theme in a fragment on the screen
                    if (fragment.isTwoPane()) {
                        fragment.themeItemClicked(clickedApp);
                    } else {
                        Intent intent = new Intent(fragment.getActivity(), ThemeActivity.class);
                        intent.putExtra(ThemeFragment.ARG_PACKAGE_NAME, clickedApp.getPackageName());
                        fragment.getActivity().startActivity(intent);
                    }
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;

        // set the height in case it has been reset for some reason
        if (defaultViewHeight == 0) {
            defaultViewHeight = ((View) holder.icon.getParent()).getLayoutParams().height;
        } else {
            ((View) holder.icon.getParent()).getLayoutParams().height = defaultViewHeight;
        }

        if (position == items.size()) {
            // this is used as a loading footer since recycler views don't explicitly have footers available
            holder.title.setVisibility(View.INVISIBLE);
            holder.description.setVisibility(View.INVISIBLE);
            holder.icon.setVisibility(View.INVISIBLE);

            if (getRealItemCount() == 0) {
                holder.progressBar.setVisibility(View.GONE);
                ((View) holder.icon.getParent()).getLayoutParams().height = 0;
            } else {
                holder.progressBar.setVisibility(View.VISIBLE);
            }
        } else {
            holder.title.setVisibility(View.VISIBLE);
            holder.description.setVisibility(View.VISIBLE);
            holder.icon.setVisibility(View.VISIBLE);

            final Market.App item = items.get(position);

            holder.title.setText(item.getTitle());
            holder.description.setText(item.getCreator());

            // this is a holder so that we can confirm we are still in the correct
            // position after the icon has loaded
            holder.icon.setTag(item.getId());

            // check if we already have this item stored
            Bitmap icon = getBitmapFromMemCache(item.getId());
            if (icon != null) {
                holder.icon.setImageBitmap(icon);
            } else {
                holder.icon.setImageDrawable(new ColorDrawable(android.R.color.transparent));

                IconLoader loader = new IconLoader(item, holder.icon, fragment.getAuthActivity(), getIconCache(), Market.GetImageRequest.AppImageUsage.ICON);
                new Thread(loader).start();
            }

            if (holder.progressBar.getVisibility() == View.VISIBLE) {
                holder.progressBar.setVisibility(View.GONE);
            }

            fragment.syncMoreThemes(position);
        }
    }

    public void add(Market.App item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void removeAll() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }
}
