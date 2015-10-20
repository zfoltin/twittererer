package com.zedeff.twittererer;

import android.app.Application;

import com.zedeff.twittererer.services.TwitterService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

import static org.mockito.Mockito.mock;

@Module
public class MockApplicationModule {

    private Application application;

    public MockApplicationModule(Application application) {
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
        return mock(TwitterService.class);
    }

    @Provides
    @Singleton
    Scheduler provideScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
