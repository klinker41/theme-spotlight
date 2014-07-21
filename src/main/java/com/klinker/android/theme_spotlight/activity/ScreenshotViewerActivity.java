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

package com.klinker.android.theme_spotlight.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.klinker.android.theme_spotlight.R;
import uk.co.senab.photoview.PhotoViewAttacher;

import java.io.File;

public class ScreenshotViewerActivity extends Activity {

    private static final String TAG = "PhotoViewerDialog";
    public static final String EXTRA_FILE_NAME = "extra_file_name";

    private Context context;
    private String fileName;
    private ImageView picture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        fileName = getIntent().getStringExtra(EXTRA_FILE_NAME);

        if (fileName == null) {
            finish();
            return;
        }

        // if kitkat, enable translucent navigation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.activity_screenshot_viewer);

        picture = (ImageView) findViewById(R.id.picture);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(picture);

        try {
            picture.setImageURI(Uri.fromFile(new File(fileName)));
        } catch (Error e) {
            e.printStackTrace();
        }

        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                ((Activity) context).finish();
            }
        });

        ActionBar ab = getActionBar();
        ColorDrawable transparent = new ColorDrawable(getResources().getColor(android.R.color.transparent));
        ab.setBackgroundDrawable(transparent);
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
        ab.setTitle("");
        ab.setIcon(transparent);
    }
}