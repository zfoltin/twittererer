package com.crowdmix.twittererer.presenters;

import com.crowdmix.twittererer.models.TimelineItem;
import com.crowdmix.twittererer.models.User;
import com.crowdmix.twittererer.services.TwitterService;
import com.crowdmix.twittererer.views.TimelineView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimelinePresenterTest {

    @Mock
    TwitterService twitterService;
    @Mock
    TimelineView mockView;

    TimelinePresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new TimelinePresenter(twitterService, Schedulers.immediate());
    }

    @Test
    public void initialiseLoadsTimelineData() {
        List<TimelineItem> timelineItems = new ArrayList<>();
        timelineItems.add(new TimelineItem("", "", new User("Bob", "bob", "")));

        when(twitterService.getTimelineItems()).thenReturn(Observable.just(timelineItems));

        presenter.initialise(mockView);

        verify(mockView).showTimeline(timelineItems);
    }
}
