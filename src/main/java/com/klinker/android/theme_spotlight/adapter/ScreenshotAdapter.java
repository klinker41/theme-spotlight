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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.activity.AuthActivity;
import com.klinker.android.theme_spotlight.activity.ScreenshotViewerActivity;
import com.klinker.android.theme_spotlight.data.IconLoader;
import com.klinker.android.theme_spotlight.data.NetworkIconLoader;

public class ScreenshotAdapter extends ArrayAdapter<Bitmap> {
    private static final String TAG = "ScreenshotAdapter";

    private final AuthActivity context;
    private final Market.App app;
    private final int numItems;
    private final int minHeight;
    private final int minWidth;
    private final String downloadUrl;

    public ScreenshotAdapter(AuthActivity context, Market.App app, int minHeight, int minWidth) {
        super(context, R.layout.screenshot_item);
        this.context = context;
        this.app = app;
        this.numItems = app.getExtendedInfo().getScreenshotsCount();
        this.minHeight = minHeight;
        this.minWidth = minWidth;
        this.downloadUrl = null;
    }

    public ScreenshotAdapter(AuthActivity context, String downloadUrl, int minHeight, int minWidth) {
        super(context, R.layout.screenshot_item);
        this.context = context;
        this.app = null;
        this.numItems = 1;
        this.minHeight = minHeight;
        this.minWidth = minWidth;
        this.downloadUrl = downloadUrl;
    }

    @Override
    public int getCount() {
        return numItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView v = (ImageView) inflateScreenshot();

        // will not work unless we configure these values
        v.setMinimumHeight(minHeight);
        v.setMinimumWidth(minWidth);

        // load the new image off of the ui thread
        if (downloadUrl == null) {
            v.setTag(app.getId());
            new Thread(new IconLoader(app, v, context, position, Market.GetImageRequest.AppImageUsage.SCREENSHOT)).start();
        } else {
            v.setTag(downloadUrl);
            new Thread(new NetworkIconLoader(context, downloadUrl, v, downloadUrl)).start();
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName;

                if (downloadUrl == null) {
                    String screenshot = "_screenshot_" + position;
                    fileName = context.getCacheDir() + "/" + app.getPackageName() + screenshot + ".png";
                } else {
                    fileName = context.getCacheDir() + "/" + downloadUrl + ".png";
                }

                Intent intent = new Intent(context, ScreenshotViewerActivity.class);
                intent.putExtra(ScreenshotViewerActivity.EXTRA_FILE_NAME, fileName);
                context.startActivity(intent);
            }
        });

        return v;
    }

    // inflate screenshot view and return it
    public View inflateScreenshot() {
        LayoutInflater inflater = context.getLayoutInflater();
        return inflater.inflate(R.layout.screenshot_item, null);
    }
}