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
import android.widget.ImageView;
import com.klinker.android.theme_spotlight.activity.AuthActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public class NetworkIconLoader extends AbstractImageLoader {

    private static final String TAG = "NetworkIconLoader";

    private String location;
    private String imageTag;

    public NetworkIconLoader(AuthActivity context, String location, ImageView imageView, String tag) {
        super(context, imageView);
        this.location = location;
        this.imageTag = tag;
    }

    @Override
    public void run() {
        File f = new File(getContext().getCacheDir() + "/" + location + ".png");

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

    private void setIcon(final Bitmap bitmap) {
        if (getImageView().getTag() == null || getImageView().getTag().toString().equals(imageTag)) {
            animateImageView(bitmap);
        }
    }
}