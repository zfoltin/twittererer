package com.zedeff.twittererer.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import com.zedeff.twittererer.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * These tests use UI Automator as the Twitter authentication mechanism cannot be tested with Espresso
 *
 *
 * Prerequisites:
 *
 * Make sure the test device has the official Twitter app installed
 * Register a Twitter test user so we can log in with that user
 *
 * Note: make sure all accessibility services are turned off (e.g.: LastPass) otherwise you get this
 * http://stackoverflow.com/questions/27132799/java-lang-securityexception-permission-denial-getintentsender-when-using-uia
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class LoginActivityTest {

    private static final String APP_PACKAGE = "com.zedeff.twittererer";
    private static final int TIMEOUT = 5000;

    private UiDevice device;

    @Before
    public void startMainActivityFromHomeScreen() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        device.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(APP_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), TIMEOUT);
    }

    @Test
    public void loginSuccessful() throws UiObjectNotFoundException {
        UiObject loginButton = device.findObject(new UiSelector().text("Log in with Twitter"));
        loginButton.clickAndWaitForNewWindow(TIMEOUT);

        UiObject allowButton = device.findObject(new UiSelector().text("Allow"));
        allowButton.clickAndWaitForNewWindow(TIMEOUT);

        UiObject activityTitle = device.findObject(new UiSelector()
                .text(getInstrumentation().getTargetContext().getString(R.string.title_activity_twitter_feed)));
        assertThat(activityTitle, notNullValue());
    }

    @Test
    public void loginCancelled() throws UiObjectNotFoundException {
        UiObject loginButton = device.findObject(new UiSelector().text("Log in with Twitter"));
        loginButton.clickAndWaitForNewWindow(TIMEOUT);

        UiObject allowButton = device.findObject(new UiSelector().text("Cancel"));
        allowButton.clickAndWaitForNewWindow(TIMEOUT);

        loginButton = device.findObject(new UiSelector().text("Log in with Twitter"));
        assertThat(loginButton, notNullValue());
    }

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
