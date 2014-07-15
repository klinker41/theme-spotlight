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