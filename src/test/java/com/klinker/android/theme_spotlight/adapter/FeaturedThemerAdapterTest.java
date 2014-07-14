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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.klinker.android.theme_spotlight.AbstractSpotlightTest;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.data.FeaturedTheme;
import com.klinker.android.theme_spotlight.data.FeaturedThemer;
import com.klinker.android.theme_spotlight.fragment.FeaturedThemerFragment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FeaturedThemerAdapterTest extends AbstractSpotlightTest {

    private FeaturedThemerAdapter adapter;

    @Mock
    private FeaturedThemerFragment fragment;

    @Mock
    private ViewGroup parent;

    @Mock
    private LayoutInflater inflater;

    @Mock
    private View view;

    @Before
    public void setup() {
        super.setup();

        FeaturedThemer[] themers = new FeaturedThemer[] {
                new FeaturedThemer("1", null, "icon", new FeaturedTheme[] {}),
                new FeaturedThemer("2", "test description", "icon2", "klinker")
        };

        adapter = spy(new FeaturedThemerAdapter(fragment, themers));
    }

    @Test
    public void testGetCount() {
        assertTrue(adapter.getItemCount() == 2);
    }

    @Test
    public void testCreateViewHolder() {
        doReturn(null).when(adapter).createViewHolder(any(View.class));
        doReturn(inflater).when(adapter).getLayoutInflater();
        doReturn(view).when(inflater).inflate(R.layout.themer_item, parent, false);

        adapter.onCreateViewHolder(parent, 1);

        verify(view, atLeastOnce()).setOnClickListener(any(View.OnClickListener.class));
        verifyNoMoreInteractions(view);
    }

}