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

import android.app.Activity;
import android.text.Spanned;
import android.text.SpannedString;
import android.widget.TextView;
import com.klinker.android.theme_spotlight.AbstractSpotlightTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ChangelogAdapterTest extends AbstractSpotlightTest {

    private ChangelogAdapter adapter;

    @Mock
    private Activity context;

    @Mock
    private TextView textView;

    @Before
    public void setup() {
        super.setup();

        Spanned[] array = new Spanned[] {
                new SpannedString("String 1"),
                new SpannedString("String 2"),
                new SpannedString("Changelog item 3")
        };

        adapter = spy(new ChangelogAdapter(context, array));
    }

    @Test
    public void testGetItem() {
        assertEquals("String 2", adapter.getItem(1).toString());
    }

    @Test
    public void testGetCount() {
        assertEquals(adapter.getCount(), 3);
    }

    @Test
    public void testGetView() {
        doReturn(textView).when(adapter).inflateChangelog();
        TextView view = (TextView) adapter.getView(0, null, null);
        assertNotNull(view);

        view = (TextView) adapter.getView(2, null, null);
        assertNotNull(view);
    }
}