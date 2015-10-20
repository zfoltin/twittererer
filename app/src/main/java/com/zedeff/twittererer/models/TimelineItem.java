package com.zedeff.twittererer.models;

import lombok.Getter;

@Getter
public class TimelineItem {

    private final String createdAt;
    private final String text;
    private final User user;

    public TimelineItem(String createdAt, String text, User user) {
        this.createdAt = createdAt;
        this.text = text;
        this.user = user;
    }
}
