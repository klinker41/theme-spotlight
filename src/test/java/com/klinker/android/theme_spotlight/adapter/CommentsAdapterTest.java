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
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class CommentsAdapterTest extends AbstractSpotlightTest {

    private CommentsAdapter adapter;

    @Mock
    private AuthActivity activity;

    @Mock
    private List<Market.Comment> comments;

//    @Mock
//    private Market.Comment.Builder builder;

    @Override
    public void setup() {
        super.setup();
        // create or mock these comment objects, so makes this hard to test
//        builder.setAuthorName("Jake")
//                .setRating(5)
//                .setText("great theme!");
//        comments.add(builder.buildPartial());
//        builder.setAuthorName("Luke Klinker")
//                .setRating(4)
//                .setText("nice");
//        comments.add(builder.buildPartial());
        adapter = new CommentsAdapter(activity, comments);
    }

    @Test
    public void testGetCount() throws Exception {
        assertEquals(0, adapter.getCount());
    }
}