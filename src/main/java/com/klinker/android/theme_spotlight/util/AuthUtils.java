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

package com.klinker.android.theme_spotlight.util;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public class AuthUtils {

    // get the android id from the device
    public static String getAndroidID(Context context) {
        String[] query = new String[] {"android_id"};
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.google.android.gsf.gservices"), null, null, query, null);

        if ((cursor.moveToFirst()) && (cursor.getColumnCount() >= 2)) {
            return Long.toHexString(Long.parseLong(cursor.getString(1))).toUpperCase();
        }

        return null;
    }

    // get the google auth token
    public static String getAuthToken(Activity context, AccountManagerCallback<Bundle> callback) {
        String authToken = "null";

        try {
            AccountManager am = AccountManager.get(context);
            Bundle options = new Bundle();

            // access google play services to get this token
            am.getAuthToken(
                    am.getAccounts()[0],
                    "android",
                    options,
                    context,
                    callback,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authToken;
    }
}
