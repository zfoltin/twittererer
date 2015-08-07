package com.crowdmix.twittererer.presenters;

import com.crowdmix.twittererer.services.TwitterService;
import com.crowdmix.twittererer.views.TimelineView;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import rx.Scheduler;

public class TimelinePresenter {

    private TwitterService service;
    private Scheduler scheduler;
    private WeakReference<TimelineView> view;

    @Inject
    public TimelinePresenter(TwitterService service, Scheduler scheduler) {
        this.service = service;
        this.scheduler = scheduler;
    }

    public void initialise(TimelineView view) {
        this.view = new WeakReference<>(view);

        service.getTimelineItems()
                .observeOn(scheduler)
                .subscribe(view::showTimeline);
    }
}
