package com.crowdmix.twittererer.views;

import com.crowdmix.twittererer.models.TimelineItem;

import java.util.List;

public interface TimelineView {

    void showTimeline(List<TimelineItem> timelineItems);
}
