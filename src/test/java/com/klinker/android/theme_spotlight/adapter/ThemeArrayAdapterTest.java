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

package com.klinker.android.theme_spotlight.adapter;

import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.AbstractSpotlightTest;
import com.klinker.android.theme_spotlight.activity.AuthActivity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class ThemeArrayAdapterTest extends AbstractSpotlightTest {

    private ThemeArrayAdapter adapter;

    @Mock
    private AuthActivity context;

    @Before
    public void setup() {
        super.setup();

        ArrayList<Market.App> apps = new ArrayList<Market.App>();

        adapter = spy(new ThemeArrayAdapter(context, apps));
    }

    @Test
    public void testGetCount() throws Exception {
        assertEquals(adapter.getCount(), 0);
    }

    @Test
    public void testGetView() throws Exception {
        // hmm, I don't know how to test this :( someone help?
    }
}