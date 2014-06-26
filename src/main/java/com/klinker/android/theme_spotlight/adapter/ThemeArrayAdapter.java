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

import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.activity.AuthActivity;
import com.klinker.android.theme_spotlight.data.IconLoader;

import java.util.List;

public class ThemeArrayAdapter extends ArrayAdapter<Market.App> {

    private final AuthActivity context;
    private final List<Market.App> items;

    private LruCache<String, Bitmap> mIconCache;

    // hold data for recycling
    static class ViewHolder {
        public TextView title;
        public TextView publisher;
        public ImageView icon;
    }

    public ThemeArrayAdapter(AuthActivity context, List<Market.App> items) {
        super(context, R.layout.theme_item);
        this.context = context;
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
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        // recycle the view correctly
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.theme_item, null);

            // initialize what we want to display views on
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.title);
            viewHolder.publisher = (TextView) rowView.findViewById(R.id.publisher);
            viewHolder.icon = (ImageView) rowView.findViewById(R.id.icon);

            rowView.setTag(viewHolder);
        }

        // set the new text to the item
        final ViewHolder holder = (ViewHolder) rowView.getTag();
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
            IconLoader loader = new IconLoader(item, holder.icon, context, mIconCache);
            new Thread(loader).start();
        }

        return rowView;
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mIconCache.get(key);
    }
}
