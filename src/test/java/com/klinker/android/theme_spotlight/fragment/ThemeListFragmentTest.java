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
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.AbstractSpotlightTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class ThemeListFragmentTest extends AbstractSpotlightTest {

    public ThemeListFragment fragment;

    @Mock
    private Bundle bundle;

    @Mock
    private LayoutInflater inflater;

    @Mock
    private ViewGroup viewGroup;

    @Mock
    private ListView listView;

    @Before
    public void setup() {
        super.setup();
        fragment = ThemeListFragment.newInstance("EvolveSMS");
    }

    @Test
    public void testNewInstance() throws Exception {
        assertNotNull(fragment);
    }

    @Test
    public void testGetSearch() throws Exception {
        ThemeListFragment spy = Mockito.spy(fragment);
        doNothing().when(spy).superOnCreate(any(Bundle.class));
        spy.onCreate(bundle);

        assertEquals(spy.getSearch("theme"), "EvolveSMS theme");
    }

    @Test
    public void testSetApps() throws Exception {
        ThemeListFragment spy = Mockito.spy(fragment);
        doNothing().when(spy).setListAdapterPost(any(Handler.class), anyList());
        doNothing().when(spy).superOnCreate(any(Bundle.class));
        spy.onCreate(bundle);
        spy.setApps(new ArrayList<Market.App>());
        assertNotNull(spy.getApps());
    }

    @Test
    public void testCreateView() throws Exception {
        ThemeListFragment spy = Mockito.spy(fragment);
        doNothing().when(spy).getThemes(anyInt());
        doNothing().when(spy).setupListView();
        doReturn(listView).when(spy).getListView();
        doReturn(listView).when(spy).superOnCreateView(inflater, viewGroup, bundle);
        doReturn(false).when(spy).isTwoPane();
        spy.onCreateView(inflater, viewGroup, bundle);
        spy.onActivityCreated(bundle);
        assertNotNull(spy.getListView());
    }
}