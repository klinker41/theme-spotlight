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

package com.klinker.android.theme_spotlight.activity;

import com.klinker.android.theme_spotlight.AbstractSpotlightTest;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FeaturedThemerActivityTest extends AbstractSpotlightTest {

    private FeaturedThemerActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.buildActivity(FeaturedThemerActivity.class)
                .create()
                .get();
    }

    @Test
    public void testOnCreate() {
        assertNotNull(activity);
    }

    @Test
    public void testGetPublisherQuery() throws Exception {
        String testString = "test";
        assertEquals("pub:" + testString, activity.getPublisherQuery(testString));
    }
}