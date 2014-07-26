package com.klinker.android.theme_spotlight.util;

import android.content.Context;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.data.Settings;

public class AppUtils {

    public static final String EVOLVE = "Evolve";
    public static final String TALON = "Talon";

    // check to make sure a theme is actually a theme, ie its name or description actually contain evolve or talon
    // only reason that it shouldn't be would be under the featured themers where they have other apps
    public static boolean checkValidTheme(String title, String description) {
        if (title == null) title = "";
        if (description == null) description = "";

        return title.contains(EVOLVE) || title.contains(TALON)
                || description.contains(EVOLVE) || description.contains(TALON)
                || title.contains("Theme") || title.contains("theme");
    }

    // Check if an app should be added to the list or not based on the whether user has enabled free only and the
    // theme is free, and that the title contains the correct app name
    public static boolean shouldAddApp(Context context, Market.App app, String verifyAgainst, String baseSearch) {
        return titleVerified(verifyAgainst, baseSearch, app) && checkAddFree(context, app);
    }

    // check if an app should be added based on user's free only preference
    private static boolean checkAddFree(Context context, Market.App app) {
        boolean freeOnly = Settings.getInstance(context).freeOnly;
        return (freeOnly && !app.hasPrice()) || !freeOnly;
    }

    // verify that we should add the app to the list. this should occur when the title or description contains
    // the text for evolve or talon, or the base search is based on publisher name
    private static boolean titleVerified(String verify, String baseSearch, Market.App app) {
        return app.getTitle().contains(verify) ||
                app.getExtendedInfo().getDescription().contains(verify) ||
                baseSearch.startsWith("pub:");
    }
}
