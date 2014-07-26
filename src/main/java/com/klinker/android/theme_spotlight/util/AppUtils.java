package com.klinker.android.theme_spotlight.util;

public class AppUtils {

    public static final String EVOLVE = "Evolve";
    public static final String TALON = "Talon";

    public static boolean checkValidTheme(String title, String description) {
        if (title == null) title = "";
        if (description == null) description = "";

        return title.contains(EVOLVE) || title.contains(TALON)
                || description.contains(EVOLVE) || description.contains(TALON);
    }
}
