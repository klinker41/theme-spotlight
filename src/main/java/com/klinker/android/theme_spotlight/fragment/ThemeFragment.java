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

package com.klinker.android.theme_spotlight.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.klinker.android.theme_spotlight.R;

public class ThemeFragment extends Fragment {

    private static final String TAG = "ThemeFragment";
    public static final String ARG_PACKAGE_NAME = "package_name";

    private View mLayout;
    private String mPackageName;

    public static ThemeFragment newInstance(String packageName) {
        ThemeFragment fragment = new ThemeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PACKAGE_NAME, packageName);
        fragment.setArguments(args);
        return fragment;
    }

    public ThemeFragment() {
        // all fragments should always have a default constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the package name that we want to load in the fragment
        mPackageName = getArguments().getString(ARG_PACKAGE_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_theme, null);

        // TODO load app according to package name here

        return mLayout;
    }
}
