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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.activity.AuthActivity;
import com.klinker.android.theme_spotlight.data.IconLoader;

public class ScreenshotAdapter extends ArrayAdapter<Bitmap> {
    private final AuthActivity context;
    private final Market.App app;
    private final Bitmap[] items;
    private final int minHeight;
    private final int minWidth;

    public ScreenshotAdapter(AuthActivity context, Market.App app, int minHeight, int minWidth) {
        super(context, R.layout.screenshot_item);
        this.context = context;
        this.app = app;
        this.items = new Bitmap[app.getExtendedInfo().getScreenshotsCount()];
        this.minHeight = minHeight;
        this.minWidth = minWidth;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView v = (ImageView) inflateScreenshot();

        // will not work unless we configure these values
        v.setMinimumHeight(minHeight);
        v.setMinimumWidth(minWidth);

        // load the new image off of the ui thread
        v.setTag(app.getId());
        new Thread(new IconLoader(app, v, context, position, Market.GetImageRequest.AppImageUsage.SCREENSHOT)).start();

        return v;
    }

    // inflate screenshot view and return it
    public View inflateScreenshot() {
        LayoutInflater inflater = context.getLayoutInflater();
        return inflater.inflate(R.layout.screenshot_item, null);
    }
}