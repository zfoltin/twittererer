package com.crowdmix.twittererer.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crowdmix.twittererer.App;
import com.crowdmix.twittererer.R;
import com.crowdmix.twittererer.models.TimelineItem;
import com.crowdmix.twittererer.presenters.TimelinePresenter;
import com.crowdmix.twittererer.views.TimelineView;

import java.util.List;

import javax.inject.Inject;

public class TimelineActivity extends AppCompatActivity implements TimelineView {

    @Inject
    TimelinePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ActivityComponent component = DaggerActivityComponent.builder()
                .applicationComponent(App.component())
                .build();

        component.inject(this);

        presenter.initialise(this);
    }

    @Override
    public void showTimeline(List<TimelineItem> timelineItems) {

    }
}
