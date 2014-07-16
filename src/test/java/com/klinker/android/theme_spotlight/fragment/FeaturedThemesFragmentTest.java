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

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.klinker.android.theme_spotlight.AbstractSpotlightTest;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.adapter.FeaturedThemeAdapter;
import com.klinker.android.theme_spotlight.data.FeaturedThemer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FeaturedThemesFragmentTest extends AbstractSpotlightTest {

    private FeaturedThemesFragment fragment;
    private FeaturedThemer themer;

    @Mock
    private Bundle bundle;

    @Mock
    private LayoutInflater inflater;

    @Mock
    private ViewGroup viewGroup;

    @Mock
    private RecyclerView listView;

    @Mock
    private Handler handler;

    @Before
    public void setup() {
        super.setup();
        themer = new FeaturedThemer("test", "description", "iconUrl", "test name");
        fragment = FeaturedThemesFragment.newInstance(themer);
    }

    @Test
    public void testCreateView() throws Exception {
        FeaturedThemesFragment spy = Mockito.spy(fragment);
        spy.setThemer(themer);
        doNothing().when(spy).setUpRecyclerView();
        doNothing().when(spy).setRecyclerViewAdapter(any(RecyclerView.Adapter.class));
        when(inflater.inflate(R.layout.fragment_theme_list, null)).thenReturn(listView);
        doReturn(listView).when(spy).getRecyclerView();
        doReturn(listView).when(spy).superOnCreateView(inflater, viewGroup, bundle);
        doReturn(false).when(spy).isTwoPane();
        doNothing().when(spy).setRecyclerViewAdapter(any(FeaturedThemeAdapter.class));
        spy.onCreateView(inflater, viewGroup, bundle);
        spy.onActivityCreated(bundle);
        assertNotNull(spy.getRecyclerView());
    }
}