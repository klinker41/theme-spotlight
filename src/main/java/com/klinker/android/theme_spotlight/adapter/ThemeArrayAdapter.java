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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.data.AuthToken;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ThemeArrayAdapter extends ArrayAdapter<Market.App> {

    private final Context context;
    private final List<Market.App> items;

    // hold data for recycling
    static class ViewHolder {
        public TextView title;
        public TextView publisher;
        public ImageView icon;
        public String id;
    }

    public ThemeArrayAdapter(Context context, List<Market.App> items) {
        super(context, R.layout.theme_item);
        this.context = context;
        this.items = items;
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
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
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

        // set everything
        holder.title.setText(item.getTitle());
        holder.publisher.setText(item.getCreator());

        // this is a holder so that we can confirm we are still in the correct
        // position after the icon has loaded
        holder.id = item.getId();

        // start a new thread to download and cache our icon
        LoadIcon loader = new LoadIcon(item, holder);
        new Thread(loader).start();

        return rowView;
    }

    // handles downloading, caching a reusing the app's icon
    private class LoadIcon implements Runnable {

        private Handler mHandler;
        private Market.App item;
        private ViewHolder holder;

        public LoadIcon(Market.App item, ViewHolder holder) {
            mHandler = new Handler();
            this.item = item;
            this.holder = holder;
        }

        @Override
        public void run() {
            // cache the file in our cache directory and keep it around so we don't have to keep downloading
            // it every time we access the item
            final String fileName = context.getCacheDir() + "/" + item.getPackageName() + ".png";

            if (new File(fileName).exists()) {
                // if the file exists already, skip downloading and processing it and just apply it
                setIcon(fileName);
            } else {
                // create a new market session
                MarketSession session = new MarketSession();
                session.getContext().setAuthSubToken(AuthToken.getAuthToken());
                session.getContext().setAndroidId(AuthToken.getAndroidId());

                // get the icon for the app
                Market.GetImageRequest imgReq = Market.GetImageRequest.newBuilder().setAppId(item.getId())
                        .setImageUsage(Market.GetImageRequest.AppImageUsage.ICON)
                        .setImageId("0")
                        .build();

                // post the request
                session.append(imgReq, new MarketSession.Callback<Market.GetImageResponse>() {

                    @Override
                    public void onResult(Market.ResponseContext responseContext, Market.GetImageResponse response) {
                        try {
                            // save the image to the file name
                            FileOutputStream fos = new FileOutputStream(fileName);
                            fos.write(response.getImageData().toByteArray());
                            fos.close();

                            // set the icon from the just saved file
                            setIcon(fileName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                session.flush();
            }
        }

        // set the icon and animate it in with a fade animation
        private void setIcon(String fileName) {
            final Bitmap icon = BitmapFactory.decodeFile(fileName);

            if (holder.id.equals(item.getId())) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.icon.setImageBitmap(icon);

                        holder.icon.setAlpha(0.0f);
                        holder.icon.animate()
                                .alpha(1.0f)
                                .setDuration(200)
                                .start();
                    }
                });
            }
        }
    }
}
