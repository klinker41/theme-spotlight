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

package com.klinker.android.theme_spotlight.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.LruCache;
import android.widget.ImageView;
import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.activity.AuthActivity;

import java.io.File;
import java.io.FileOutputStream;

// handles downloading, caching a reusing the app's icon
public class IconLoader implements Runnable {

    private static final String TAG = "IconLoader";

    private Handler mHandler;
    private AuthActivity context;
    private int imageNumber;
    private Market.App item;
    private ImageView imageView;
    private LruCache<String, Bitmap> cache;
    private Market.GetImageRequest.AppImageUsage usage;

    public IconLoader(Market.App item, ImageView imageView, AuthActivity context, int imageNumber,
                      Market.GetImageRequest.AppImageUsage usage) {
        this(item, imageView, context, imageNumber, null, usage);
    }

    public IconLoader(Market.App item, ImageView imageView, AuthActivity context,
                      LruCache<String, Bitmap> cache, Market.GetImageRequest.AppImageUsage usage) {
        this(item, imageView, context, 1, cache, usage);
    }

    public IconLoader(Market.App item, ImageView imageView, AuthActivity context, int imageNumber,
                      LruCache<String, Bitmap> cache, Market.GetImageRequest.AppImageUsage usage) {
        mHandler = new Handler();
        this.context = context;
        this.imageNumber = imageNumber;
        this.item = item;
        this.imageView = imageView;
        this.cache = cache;
        this.usage = usage;
    }

    @Override
    public void run() {
        // cache the file in our cache directory and keep it around so we don't have to keep downloading
        // it every time we access the item
        String screenshot = (usage == Market.GetImageRequest.AppImageUsage.SCREENSHOT) ? ("_screenshot_" + imageNumber) : "";
        final String fileName = context.getCacheDir() + "/" + item.getPackageName() + screenshot + ".png";

        if (new File(fileName).exists()) {
            // if the file exists already, skip downloading and processing it and just apply it
            setIcon(fileName);
        } else {
            // create a new market session
            MarketSession session = new MarketSession();
            session.getContext().setAuthSubToken(context.getAuthToken().getAuthToken());
            session.getContext().setAndroidId(context.getAuthToken().getAndroidId());

            // get the icon for the app
            Market.GetImageRequest imgReq = Market.GetImageRequest.newBuilder().setAppId(item.getId())
                    .setImageUsage(usage)
                    .setImageId(Integer.toString(imageNumber))
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
        addBitmapToMemoryCache(item.getId(), icon);

        if (imageView.getTag() == null || imageView.getTag().toString().equals(item.getId())) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(icon);

                    imageView.setAlpha(0.0f);
                    imageView.animate()
                            .alpha(1.0f)
                            .setDuration(200)
                            .start();
                }
            });
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (cache != null && key != null && bitmap != null) {
            cache.put(key, bitmap);
        }
    }
}
