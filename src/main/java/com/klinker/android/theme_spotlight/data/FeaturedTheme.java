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

public class FeaturedTheme {

    private final String screenshotUrl;
    private final String downloadUrl;
    private final String shortDescription;
    private final String name;
    private final String iconUrl;
    private final String sourceUrl;

    /**
     * Create a new theme that is not on the play store, sourceUrl can be null
     */
    public FeaturedTheme(String name, String shortDescription, String iconUrl, String downloadUrl,
                         String screenshotUrl, String sourceUrl) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.iconUrl = iconUrl;
        this.downloadUrl = downloadUrl;
        this.screenshotUrl = screenshotUrl;
        this.sourceUrl = sourceUrl;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }
}
