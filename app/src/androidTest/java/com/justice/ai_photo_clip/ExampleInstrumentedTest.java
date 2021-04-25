package com.justice.ai_photo_clip;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test. (테스트 중인 앱의 컨텍스트.)
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("info.androidhive.imagefilters", appContext.getPackageName());
    }
}
