package com.zedeff.twittererer;

import android.app.Application;

import com.zedeff.twittererer.services.TwitterService;
import com.zedeff.twittererer.services.TwitterServiceImpl;
import com.twitter.sdk.android.Twitter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@Module
class ApplicationModule {

    private final Application application;

    ApplicationModule(Application application) {
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
        return new TwitterServiceImpl(Twitter.getSessionManager().getActiveSession());
    }

    @Provides
    @Singleton
    Scheduler provideScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
