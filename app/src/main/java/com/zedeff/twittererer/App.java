package com.zedeff.twittererer;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.crashlytics.android.Crashlytics;
import com.zedeff.twittererer.services.TwitterService;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Singleton;

import dagger.Component;
import io.fabric.sdk.android.Fabric;
import rx.Scheduler;

public class App extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    @SuppressWarnings("SpellCheckingInspection")
    public static final String TWITTER_KEY = "Hgt8d2nLhZdQUaIvsyxYuPhb5";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String TWITTER_SECRET = "MeliZLYavlMiyhYTs97zyTCUOzznnog9cIrgfRHfc0TbN9PkP4";

    private static BaseApplicationComponent applicationComponent;

    public static BaseApplicationComponent component() {
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));

        applicationComponent = DaggerApp_ApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    @VisibleForTesting
    public static void overrideComponent(BaseApplicationComponent component) {
        applicationComponent = component;
    }

    @Singleton
    @Component(modules = {ApplicationModule.class})
    public interface ApplicationComponent extends BaseApplicationComponent {
    }

    public interface BaseApplicationComponent {

        Application application();

        TwitterService twitterService();

        Scheduler scheduler();
    }
}
