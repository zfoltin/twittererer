package com.zedeff.twittererer.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zedeff.twittererer.App;
import com.zedeff.twittererer.R;
import com.zedeff.twittererer.models.TimelineItem;
import com.zedeff.twittererer.presenters.TimelinePresenter;
import com.zedeff.twittererer.ui.adapters.TimelineAdapter;
import com.zedeff.twittererer.views.TimelineView;

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
    @Bind(R.id.no_tweets)
    TextView noTweetsView;

    TimelineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        DaggerActivityComponent.builder()
                .baseApplicationComponent(App.component())
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_tweet) {
            showNewTweetDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNewTweetDialog() {
        final EditText tweetText = new EditText(this);
        tweetText.setId(R.id.tw__tweet_text);
        tweetText.setSingleLine();
        tweetText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        tweetText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(140)});
        tweetText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.label_what_is_happening);
        builder.setPositiveButton(R.string.action_tweet, (dialog, which) -> {
            presenter.tweet(tweetText.getText().toString());
        });

        AlertDialog alert = builder.create();
        alert.setView(tweetText, 64, 0, 64, 0);
        alert.show();

        tweetText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                alert.getButton(DialogInterface.BUTTON_POSITIVE).callOnClick();
                return true;
            }
            return false;
        });
    }

    @Override
    public void showTimeline(List<TimelineItem> timelineItems) {
        swipeRefresh.setVisibility(View.VISIBLE);
        tweetList.setVisibility(View.VISIBLE);
        noTweetsView.setVisibility(View.GONE);
        adapter.setItems(timelineItems);
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showNoTweets() {
        swipeRefresh.setVisibility(View.GONE);
        tweetList.setVisibility(View.GONE);
        noTweetsView.setVisibility(View.VISIBLE);
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showMessage(@StringRes int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show();
    }
}
