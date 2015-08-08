package com.crowdmix.twittererer.ui.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

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

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.tweets)
    RecyclerView tweetList;

    TimelineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        DaggerActivityComponent.builder()
                .applicationComponent(App.component())
                .build()
                .inject(this);

        setSupportActionBar((Toolbar) findViewById(R.id.ab_toolbar));

        initTweetList();

        swipeRefresh.setColorSchemeResources(R.color.brand_blue, R.color.dark_blue);
        swipeRefresh.setOnRefreshListener(presenter::refreshTweets);

        presenter.initialise(this);
    }

    private void initTweetList() {
        ButterKnife.bind(this);

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tweetList.setLayoutManager(lm);

        adapter = new TimelineAdapter();
        tweetList.setAdapter(adapter);
    }

    @Override
    public void showTimeline(List<TimelineItem> timelineItems) {
        adapter.setItems(timelineItems);
        swipeRefresh.setRefreshing(false);
    }
}
