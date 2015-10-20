package com.zedeff.twittererer.ui.activities;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.InstrumentationTestCase;

import com.zedeff.twittererer.R;

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
public class LoginActivityTest extends InstrumentationTestCase {

    private static final String APP_PACKAGE = "com.zedeff.twittererer";
    private static final long TIMEOUT = 5000;

    private UiDevice device;

    public void setUp() {
        launchActivity(APP_PACKAGE, LoginActivity.class, null);
        device = UiDevice.getInstance(getInstrumentation());
    }

    public void testLoginSuccessful() throws UiObjectNotFoundException {
        UiObject loginButton = device.findObject(new UiSelector().text(getInstrumentation().getTargetContext().getString(R.string.tw__login_btn_txt)));
        loginButton.clickAndWaitForNewWindow(TIMEOUT);

        UiObject allowButton = device.findObject(new UiSelector().text("Allow"));
        allowButton.clickAndWaitForNewWindow(TIMEOUT);

        UiObject activityTitle = device.findObject(new UiSelector()
                .text(getInstrumentation().getTargetContext().getString(R.string.title_activity_twitter_feed)));
        assertNotNull(activityTitle);
    }

    public void testLoginCancelled() throws UiObjectNotFoundException {
        UiObject loginButton = device.findObject(new UiSelector().text(getInstrumentation().getTargetContext().getString(R.string.tw__login_btn_txt)));
        loginButton.clickAndWaitForNewWindow(TIMEOUT);

        UiObject allowButton = device.findObject(new UiSelector().text("Cancel"));
        allowButton.clickAndWaitForNewWindow(TIMEOUT);

        loginButton = device.findObject(new UiSelector().text(getInstrumentation().getTargetContext().getString(R.string.tw__login_btn_txt)));
        assertNotNull(loginButton);
    }
}
