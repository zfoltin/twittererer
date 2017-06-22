package com.zedeff.twittererer.test.presenters;

import com.zedeff.twittererer.R;
import com.zedeff.twittererer.models.TimelineItem;
import com.zedeff.twittererer.models.User;
import com.zedeff.twittererer.presenters.TimelinePresenter;
import com.zedeff.twittererer.services.TwitterService;
import com.zedeff.twittererer.views.TimelineView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimelinePresenterTest {

    @Mock
    TwitterService twitterService;
    @Mock
    TimelineView mockView;
    @Captor
    ArgumentCaptor<List<TimelineItem>> timelineItemsCaptor;

    private TimelinePresenter presenter;
    private User user;
    private List<TimelineItem> timelineItems;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new TimelinePresenter(twitterService, Schedulers.trampoline());

        user = new User("Bob", "bob", "");
        timelineItems = new ArrayList<>();
        timelineItems.add(new TimelineItem("", "", user));

        when(twitterService.getTimelineItems()).thenReturn(Observable.just(timelineItems));
        when(twitterService.getMyDetails()).thenReturn(Observable.just(user));
    }

    @Test
    public void initialiseGetsMyDetails() {
        presenter.initialise(mockView);
        verify(twitterService).setCurrentUser(user);
    }

    @Test
    public void initialiseLoadsTimelineData() {
        presenter.initialise(mockView);
        verify(mockView).showTimeline(timelineItems);
    }

    @Test
    public void initialiseShowNoTweets() {
        timelineItems.clear();
        presenter.initialise(mockView);
        verify(mockView).showNoTweets();
    }

    @Test
    public void tweetAddsToTimeline() {
        when(twitterService.sendTweet("hello")).thenReturn(Observable.just(true));
        presenter.initialise(mockView);

        presenter.tweet("hello");

        verify(mockView, times(2)).showTimeline(timelineItemsCaptor.capture());

        assertThat(timelineItemsCaptor.getValue(), is(notNullValue()));
        assertThat(timelineItemsCaptor.getValue().get(0).getText(), is(equalTo("hello")));
    }

    @Test
    public void whenTweetSuccessfulThenShowSuccessMessage() {
        when(twitterService.sendTweet("hello")).thenReturn(Observable.just(true));
        presenter.initialise(mockView);

        presenter.tweet("hello");

        verify(mockView).showMessage(R.string.alert_tweet_successful);
    }

    @Test
    public void whenTweetFailsThenShowErrorMessage() {
        when(twitterService.sendTweet("hello")).thenReturn(Observable.error(new Exception()));
        presenter.initialise(mockView);

        presenter.tweet("hello");

        verify(mockView).showMessage(R.string.alert_tweet_failed);
    }
}
