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
            new FeaturedTheme[]{
                    Themes.JK_EVOLVE_HANGOUTS,
                    Themes.JK_EVOLVE_BLUE,
                    Themes.JK_EVOLVE_BLUE_CLASSIC,
                    Themes.JK_IOS_THEME,
                    Themes.JK_STOCK_THEME
            }
    );

    private static final FeaturedThemer LUKE_KLINKER = new FeaturedThemer(
            "Luke Klinker",
            "Talon for Twitter Main Developer",
            "http://www.gravatar.com/avatar/d27bf79782954066cc185f7207e0c5fd.png",
            "Klinker Apps"
    );

    private static final FeaturedThemer KLINKER_APPS = new FeaturedThemer(
            "Klinker Apps",
            null,
            "http://klinkerapps.com/dev-upload/include/views/img/logo.gif",
            "Klinker Apps"
    );
    
    private static final FeaturedThemer JON_MERRITT = new FeaturedThemer(
            "Jon Merritt",
             null,
            "https://lh4.googleusercontent.com/-DO2R3EsKx3I/AAAAAAAAAAI/AAAAAAAADew/3edzsY4jaZ0/s120-c/photo.jpg",
            "Optimal Designs"
            );
    
    private static final FeaturedThemer BORDEN_GRAPHICS = new FeaturedThemer(
            "BORDEN GRAPHICS",
             null,
            "https://lh3.googleusercontent.com/-WqwyyX1pysU/UsdbPuxBqkI/AAAAAAAATB4/jjabm1eJ1F4/w521-h520-no/b.gif",
            "BORDEN GRAPHICS"
    );

    private static final FeaturedThemer NATE_GANTT = new FeaturedThemer(
            "Nate Gantt",
             null,
            "https://lh5.googleusercontent.com/-5LHsOdckHLU/UZEKiWqvoyI/AAAAAAAAJNg/o_VZ6c7tfAI/w722-h724-no/BetterNate_blur.png",
            "[nXt3_Apps]"
    );
    
    private static final FeaturedThemer HOBIE_HELBICH = new FeaturedThemer(
            "Hobie Helbich",
             null,
            "https://lh4.googleusercontent.com/-4ewb6zcg1j0/U9bT3DJGNCI/AAAAAAAAN2Q/eYKj1wYpQ0c/s553-no/HOBI3CATevolveboatlogo.png",
            "HOBI3CAT"
    );

    private static final FeaturedThemer ROBERTO_DARKO = new FeaturedThemer(
            "Roberto Darko",
             null,
            "https://lh4.googleusercontent.com/-8CJZrnuDkyU/UtA35c6bbxI/AAAAAAAAEW8/4eqc5Ts1Qww/s256-no/Google%252B1_2.jpg",
            "Darko Apps"
    );

    private static final FeaturedThemer FLYINGRHINOCMG = new FeaturedThemer(
            "FlyingRhinoCMG",
             null,
            "https://lh6.googleusercontent.com/-A-vYPVIuZ3o/U9XQIBvw5aI/AAAAAAABQ4I/kQPhT3RdDbQ/s200/avatar3-highres.png",
            "FlyingRhinoCMG"
    );

    // list of all themers, will be alphabetized when displayed
    public static final FeaturedThemer[] FEATURED_THEMERS = new FeaturedThemer[]{
            JAKE_KLINKER,
            LUKE_KLINKER,
            KLINKER_APPS,
            JON_MERRITT,
            BORDEN_GRAPHICS,
            NATE_GANTT,
            HOBIE_HELBICH,
            ROBERTO_DARKO,
            FLYINGRHINOCMG
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
