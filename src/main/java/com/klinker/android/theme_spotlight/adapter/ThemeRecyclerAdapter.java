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
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.activity.ThemeActivity;
import com.klinker.android.theme_spotlight.data.IconLoader;
import com.klinker.android.theme_spotlight.fragment.ThemeFragment;
import com.klinker.android.theme_spotlight.fragment.ThemeListFragment;

import java.util.List;

public class ThemeRecyclerAdapter extends RecyclerView.Adapter<ThemeRecyclerAdapter.ViewHolder>{

    private static final String TAG = "ThemeRecyclerAdapter";
    private final ThemeListFragment fragment;
    private final List<Market.App> items;

    private LruCache<String, Bitmap> mIconCache;

    public ThemeRecyclerAdapter(ThemeListFragment fragment, List<Market.App> items) {
        this.fragment = fragment;
        this.items = items;

        // set up the icon cacher
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mIconCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public int getItemCount() {
        // the + 1 was an attempt to add a loading indicator to the bottom to
        // replace a listview footer, didn't work though
        return items.size()/* + 1*/;
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
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;

        if (position == items.size()) {
            if (getRealItemCount() == 0) {
                holder.progressBar.setVisibility(View.GONE);
            } else {
                // view should be the loading view
                holder.progressBar.setVisibility(View.VISIBLE);
            }
        } else {
            final Market.App item = items.get(position);

            holder.title.setText(item.getTitle());
            holder.publisher.setText(item.getCreator());

            // this is a holder so that we can confirm we are still in the correct
            // position after the icon has loaded
            holder.icon.setTag(item.getId());

            // check if we already have this item stored
            Bitmap icon = getBitmapFromMemCache(item.getId());
            if (icon != null) {
                holder.icon.setImageBitmap(icon);
            } else {
                // since we are loading on a different thread for the icon, set the current
                // one to transparent (don't want it looking funny during recycling
                holder.icon.setImageResource(android.R.color.transparent);

                // start a new thread to download and cache our icon
                IconLoader loader = new IconLoader(item, holder.icon, fragment.getAuthActivity(), mIconCache, Market.GetImageRequest.AppImageUsage.ICON);
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

    public void remove(Market.App item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mIconCache.get(key);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView publisher;
        public ImageView icon;
        public View progressBar;
        public int position;

        public ViewHolder(View item) {
            super(item);

            title = (TextView) item.findViewById(R.id.title);
            publisher = (TextView) item.findViewById(R.id.publisher);
            icon = (ImageView) item.findViewById(R.id.icon);
            progressBar = item.findViewById(R.id.loading);
        }
    }
}
