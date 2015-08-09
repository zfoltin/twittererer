package com.crowdmix.twittererer;

import android.app.Application;

import com.crowdmix.twittererer.services.TwitterService;
import com.twitter.sdk.android.Twitter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application application() {
        return application;
    }

    @Provides
    @Singleton
    TwitterService provideTwitterService() {
        return new TwitterService(Twitter.getSessionManager().getActiveSession());
    }

    @Provides
    @Singleton
    Scheduler provideScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
