package com.crowdmix.twittererer.ui.activities;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.crowdmix.twittererer.App;
import com.crowdmix.twittererer.DaggerMockApplicationComponent;
import com.crowdmix.twittererer.MockApplicationModule;
import com.crowdmix.twittererer.R;
import com.crowdmix.twittererer.models.TimelineItem;
import com.crowdmix.twittererer.models.User;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * UI tests using Espresso
 * <p>
 * I prefer to use a test environment whenever I can, instead of mocking the API.
 * In case of Twitter the API calls have to be mocked.
 * <p>
 * Another difficulty is that log in with Twitter cannot be done with Espresso
 */
@RunWith(AndroidJUnit4.class)
public class TimelineActivityTest {

    List<TimelineItem> timelineItems;

    @Rule
    public ActivityTestRule<TimelineActivity> activityTestRule = new ActivityTestRule<TimelineActivity>(TimelineActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();

            App.BaseApplicationComponent component = DaggerMockApplicationComponent.builder()
                    .mockApplicationModule(new MockApplicationModule((App) InstrumentationRegistry.getTargetContext().getApplicationContext()))
                    .build();
            App.overrideComponent(component);

            User bobUser = new User("Bob", "bob", "http://assets.rollingstone.com/assets/images/list_item/bob-marley-20110420/square.jpg");
            when(component.twitterService().getMyDetails()).thenReturn(Observable.just(bobUser));

            timelineItems = new ArrayList<>();
            timelineItems.add(new TimelineItem("10m", "hello", bobUser));
            when(component.twitterService().getTimelineItems()).thenReturn(Observable.just(timelineItems));

            when(component.twitterService().getCurrentUser()).thenReturn(bobUser);
            when(component.twitterService().sendTweet(anyString())).thenReturn(Observable.just(true));
        }
    };

    @Test
    public void canSeeTimelineWhenThereAreTweets() {
        onView(withText(R.string.title_activity_twitter_feed))
                .check(matches(isDisplayed()));

        onView(withText("Bob"))
                .check(matches(isDisplayed()));
        onView(withText("@bob"))
                .check(matches(isDisplayed()));
        onView(withText("10m"))
                .check(matches(isDisplayed()));
        onView(withText("hello"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void refreshTweets() {
        onView(withText("Bob"))
                .check(matches(isDisplayed()));
        onView(withText("Richie Rich"))
                .check(doesNotExist());

        timelineItems.add(new TimelineItem("5s", "boo", new User("Richie Rich", "richie-rich", "http://ia.media-imdb.com/images/M/MV5BMTA4NjI0NzE2NjNeQTJeQWpwZ15BbWU4MDU0NzMwNjQx._V1_SY317_CR6,0,214,317_AL_.jpg")));

        onView(withId(R.id.swipe_refresh))
                .perform(swipeDown());

        onView(withText("Bob"))
                .check(matches(isDisplayed()));
        onView(withText("Richie Rich"))
                .check(matches(isDisplayed()));
        onView(withText("@richie-rich"))
                .check(matches(isDisplayed()));
        onView(withText("5s"))
                .check(matches(isDisplayed()));
        onView(withText("boo"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void seeNoTweetsMessageWhenThereAreNoTweets() {
        onView(withText(R.string.title_activity_twitter_feed))
                .check(matches(isDisplayed()));

        timelineItems.clear();

        onView(withId(R.id.swipe_refresh))
                .perform(swipeDown());

        onView(withText(R.string.label_no_tweets))
                .check(matches(isDisplayed()));
    }

    @Test
    public void sendTweet() throws InterruptedException {
        onView(withId(R.id.action_tweet))
                .perform(click());

        String tweetText = "My latest unique tweet with #" + System.currentTimeMillis();
        onView(withId(R.id.tw__tweet_text))
                .perform(typeText(tweetText), pressImeActionButton());

        onView(withText(tweetText))
                .check(matches(isDisplayed()));
    }
}
