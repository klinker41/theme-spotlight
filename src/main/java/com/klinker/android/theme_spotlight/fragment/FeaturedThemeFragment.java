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
import com.klinker.android.theme_spotlight.data.FeaturedTheme;

public class FeaturedThemeFragment extends Fragment {

    public static final String EXTRA_THEME = "extra_featured_theme";

    private FeaturedTheme mTheme;

    public static FeaturedThemeFragment newInstance(FeaturedTheme theme) {
        FeaturedThemeFragment fragment = new FeaturedThemeFragment();
        setArguements(fragment, theme);
        return fragment;
    }

    public static void setArguements(FeaturedThemeFragment fragment, FeaturedTheme theme) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_THEME, theme);
        fragment.setArguments(bundle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTheme = (FeaturedTheme) getArguments().getSerializable(EXTRA_THEME);
    }
}
