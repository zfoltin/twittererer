package com.crowdmix.twittererer.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crowdmix.twittererer.App;
import com.crowdmix.twittererer.R;
import com.crowdmix.twittererer.models.TimelineItem;
import com.crowdmix.twittererer.presenters.TimelinePresenter;
import com.crowdmix.twittererer.ui.adapters.TimelineAdapter;
import com.crowdmix.twittererer.views.TimelineView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity implements TimelineView {

    @Inject
    TimelinePresenter presenter;

    @Bind(R.id.tweets)
    RecyclerView tweetsList;

    TimelineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ActivityComponent component = DaggerActivityComponent.builder()
                .applicationComponent(App.component())
                .build();

        component.inject(this);

        setupToolbar();

        ButterKnife.bind(this);

        adapter = new TimelineAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tweetsList.setLayoutManager(lm);
        tweetsList.setAdapter(adapter);

        presenter.initialise(this);
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.ab_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
    }

    @Override
    public void showTimeline(List<TimelineItem> timelineItems) {
        adapter.setItems(timelineItems);
    }
}
