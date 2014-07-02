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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import com.klinker.android.theme_spotlight.AbstractSpotlightTest;
import com.klinker.android.theme_spotlight.activity.AuthActivity;
import com.klinker.android.theme_spotlight.activity.SpotlightActivity;
import org.junit.Test;
import org.robolectric.Robolectric;

import static junit.framework.Assert.assertNotNull;

public class AuthFragmentTest extends AbstractSpotlightTest {

    @Test
    public void testCreateFragment() throws Exception {
        AuthFragment fragment = AuthFragment.newInstance();
        startFragment(fragment);
        assertNotNull(fragment);
        assertNotNull(fragment.getAuthActivity());
    }

    public static void startFragment(Fragment fragment) {
        AuthActivity activity = Robolectric.buildActivity(SpotlightActivity.class)
                .create()
                .get();

        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();
    }
}