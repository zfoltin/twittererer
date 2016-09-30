package com.zedeff.twittererer.ui.activities;

import com.zedeff.twittererer.App;

import dagger.Component;

@PerActivity
@Component(dependencies = App.BaseApplicationComponent.class)
interface ActivityComponent {

    void inject(TimelineActivity activity);
}
