package com.zedeff.twittererer.views;

import android.support.annotation.StringRes;

import com.zedeff.twittererer.models.TimelineItem;

import java.util.List;

public interface TimelineView {

    void showTimeline(List<TimelineItem> timelineItems);

    void showNoTweets();

    void showMessage(@StringRes int messageResId);
}
