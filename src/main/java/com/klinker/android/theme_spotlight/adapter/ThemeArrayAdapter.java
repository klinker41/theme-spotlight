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

import java.io.FileOutputStream;
import java.util.List;

public class ThemeArrayAdapter extends ArrayAdapter<Market.App> {

    private final Context context;
    private final List<Market.App> items;

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
        holder.id = item.getId();

        LoadIcon loader = new LoadIcon(item, holder);
        new Thread(loader).start();

        return rowView;
    }

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
            MarketSession session = new MarketSession();
            session.getContext().setAuthSubToken(AuthToken.getAuthToken());
            session.getContext().setAndroidId(AuthToken.getAndroidId());

            Market.GetImageRequest imgReq = Market.GetImageRequest.newBuilder().setAppId(item.getId())
                    .setImageUsage(Market.GetImageRequest.AppImageUsage.ICON)
                    .setImageId("1")
                    .build();

            session.append(imgReq, new MarketSession.Callback<Market.GetImageResponse>() {

                @Override
                public void onResult(Market.ResponseContext responseContext, Market.GetImageResponse response) {
                    try {
                        String fileName = context.getCacheDir() + "/" + item.getPackageName() + ".png";
                        FileOutputStream fos = new FileOutputStream(fileName);
                        fos.write(response.getImageData().toByteArray());
                        fos.close();
                        final Bitmap icon = BitmapFactory.decodeFile(fileName);

                        if (holder.id.equals(item.getId())) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.icon.setImageBitmap(icon);
                                }
                            });
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            session.flush();
        }
    }
}
