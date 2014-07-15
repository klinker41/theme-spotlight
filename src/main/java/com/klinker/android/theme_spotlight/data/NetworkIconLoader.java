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
import android.widget.ImageView;

import java.net.URL;

public class NetworkIconLoader implements Runnable {

    private static final String TAG = "NetworkIconLoader";

    private Handler mHandler;
    private String location;
    private String imageTag;
    private ImageView imageView;

    public NetworkIconLoader(String location, ImageView imageView, String tag) {
        mHandler = new Handler();
        this.location = location;
        this.imageView = imageView;
        this.imageTag = tag;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(location);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            setIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // set the icon and animate it in with a fade animation
    private void setIcon(final Bitmap bitmap) {
        if (imageView.getTag() == null || imageView.getTag().toString().equals(imageTag)) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);

                    imageView.setAlpha(0.0f);
                    imageView.animate()
                            .alpha(1.0f)
                            .setDuration(200)
                            .start();
                }
            });
        }
    }
}