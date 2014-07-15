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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public class NetworkIconLoader implements Runnable {

    private static final String TAG = "NetworkIconLoader";

    private Context context;
    private Handler mHandler;
    private String location;
    private String imageTag;
    private ImageView imageView;

    public NetworkIconLoader(Context context, String location, ImageView imageView, String tag) {
        this.context = context;
        mHandler = new Handler();
        this.location = location;
        this.imageView = imageView;
        this.imageTag = tag;
    }

    @Override
    public void run() {
        File f = new File(context.getCacheDir() + "/" + location + ".png");

        if (f.exists()) {
            Bitmap image = BitmapFactory.decodeFile(f.getPath());
            setIcon(image);
        } else {
            try {
                URL url = new URL(location);
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                setIcon(image);

                // cache the image for use later
                f.getParentFile().mkdirs();
                f.createNewFile();
                FileOutputStream output = new FileOutputStream(f);
                image.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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