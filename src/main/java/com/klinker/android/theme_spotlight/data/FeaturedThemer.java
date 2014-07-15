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

import java.io.Serializable;

public class FeaturedThemer implements Serializable {

    private final FeaturedTheme[] themes;
    private final String name;
    private final String description;
    private final String iconUrl;
    private final boolean fromPlaystore;
    private final String playStoreDeveloperName;

    /**
     * Create a new themer who's themes are not on the play store. Description can be null
     */
    public FeaturedThemer(String name, String description, String iconUrl, FeaturedTheme[] themes) {
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.themes = themes;
        this.fromPlaystore = false;
        this.playStoreDeveloperName = null;
    }

    /**
     * Create a new themer who's themes are on the play store, Description can be null
     */
    public FeaturedThemer(String name, String description, String iconUrl, String playStoreDeveloperAccountName) {
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.themes = null;
        this.fromPlaystore = true;
        this.playStoreDeveloperName = playStoreDeveloperAccountName;
    }

    public FeaturedTheme[] getThemes() {
        return themes;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public boolean isFromPlaystore() {
        return fromPlaystore;
    }

    public String getPlayStoreDeveloperName() {
        return playStoreDeveloperName;
    }
}
