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

package com.klinker.android.theme_spotlight;

import com.klinker.android.theme_spotlight.data.FeaturedTheme;
import com.klinker.android.theme_spotlight.data.FeaturedThemer;

import java.util.Arrays;
import java.util.Comparator;

/**
 * This holds all featured themers to be displayed in the featured themers section.
 */
public class Themers {

    private static final FeaturedThemer JAKE_KLINKER = new FeaturedThemer(
            "Jake Klinker",
            "EvolveSMS Main Developer",
            "https://lh4.googleusercontent.com/-kD8HBhQOtBc/Ue1x1iothBI/AAAAAAAAMVA/DTisicS7axw/s256-no/f5117d67-1850-4a52-b3a4-e6b267abca17",
            new FeaturedTheme[] {
                    Themes.JK_EVOLVE_HANGOUTS,
                    Themes.JK_EVOLVE_BLUE,
                    Themes.JK_EVOLVE_BLUE_CLASSIC,
                    Themes.JK_IOS_THEME,
                    Themes.JK_STOCK_THEME
            }
    );

    private static final FeaturedThemer KLINKER_APPS = new FeaturedThemer(
            "Klinker Apps",
            null,
            "http://klinkerapps.com/dev-upload/include/views/img/logo.gif",
            "Klinker Apps"
    );

    // list of all themers, will be alphabetized when displayed
    public static final FeaturedThemer[] FEATURED_THEMERS = new FeaturedThemer[]{
            JAKE_KLINKER,
            KLINKER_APPS,
    };

    static {
        // Sort the themers in alphabetical order
        Arrays.sort(FEATURED_THEMERS, new Comparator<FeaturedThemer>() {
            @Override
            public int compare(FeaturedThemer themer1, FeaturedThemer themer2) {
                return themer1.getName().compareTo(themer2.getName());
            }
        });
    }
}
