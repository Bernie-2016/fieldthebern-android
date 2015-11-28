package com.berniesanders.fieldthebern.config;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.test.mock.MockContext;

import com.berniesanders.fieldthebern.R;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigImplTest {
    @Test
    public void testUserAgent() throws PackageManager.NameNotFoundException {
        Context context = mock(MockContext.class);
        PackageManager manager = mock(PackageManager.class);
        PackageInfo info = mock(PackageInfo.class);
        info.versionName = "1.0.0";
        info.versionCode = 7;
        when(manager.getPackageInfo("packageName", 0)).thenReturn(info);
        when(context.getString(R.string.baseUrl)).thenReturn("baseUrl");
        when(context.getString(R.string.canvassUrl)).thenReturn("canvassUrl");
        when(context.getString(R.string.oauth2ClientId)).thenReturn("client");
        when(context.getString(R.string.oauth2ClientSecret)).thenReturn("secret");
        when(context.getPackageName()).thenReturn("packageName");
        when(context.getPackageManager()).thenReturn(manager);

        Config config = new ConfigImpl(context);
        assertEquals(config.getUserAgent(), "FieldTheBern/1.0.0-b7");
    }
}
