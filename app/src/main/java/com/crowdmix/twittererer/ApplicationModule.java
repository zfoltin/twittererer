package com.crowdmix.twittererer;

import android.app.Application;

import com.crowdmix.twittererer.services.TwitterService;
import com.twitter.sdk.android.core.Session;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@Module
public class ApplicationModule {

    private final Application application;
    private Session twitterSession;

    public ApplicationModule(Application application, Session twitterSession) {
        this.application = application;
        this.twitterSession = twitterSession;
    }

    @Provides
    @Singleton
    Application application() {
        return application;
    }

    @Provides
    @Singleton
    TwitterService provideTwitterService() {
        return new TwitterService(twitterSession);
    }

    @Provides
    @Singleton
    Scheduler provideScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
