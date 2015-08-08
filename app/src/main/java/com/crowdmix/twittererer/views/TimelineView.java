package com.crowdmix.twittererer.views;

import android.support.annotation.StringRes;

import com.crowdmix.twittererer.models.TimelineItem;

import java.util.List;

public interface TimelineView {

    void showTimeline(List<TimelineItem> timelineItems);

    void showMessage(@StringRes int messageResId);
}
