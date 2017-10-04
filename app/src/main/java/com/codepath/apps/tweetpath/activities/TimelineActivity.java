package com.codepath.apps.tweetpath.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.tweetpath.R;
import com.codepath.apps.tweetpath.TwitterApp;
import com.codepath.apps.tweetpath.adapters.TweetListFragmentPagerAdapter;
import com.codepath.apps.tweetpath.fragments.ComposeFragment;
import com.codepath.apps.tweetpath.fragments.HomeTimelineFragment;
import com.codepath.apps.tweetpath.fragments.TweetsListFragment;
import com.codepath.apps.tweetpath.models.Tweet;
import com.codepath.apps.tweetpath.models.User;
import com.codepath.apps.tweetpath.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity
        implements ComposeFragment.OnTweetSubmitListener,
        TweetsListFragment.TweetSelectedListener {

    private ConnectivityManager mConnectivityManager;
    private User currentUser;
    private TwitterClient mClient;
    private TweetListFragmentPagerAdapter mTweetListFragmentPagerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_compose) {
            launchComposeActivity();
        }

        if (id == R.id.action_profile) {
            launchProfileActivity(currentUser);
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchProfileActivity(User user) {
        Intent launchProfile = new Intent(this, ProfileActivity.class);
        launchProfile.putExtra("user", user);
        startActivity(launchProfile);
    }

    private void launchComposeActivity() {
        FragmentManager fm = getSupportFragmentManager();

        ComposeFragment composeFragment = ComposeFragment.newInstance(currentUser);
        composeFragment.show(fm, "compose");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mTweetListFragmentPagerAdapter = new TweetListFragmentPagerAdapter(getSupportFragmentManager(), this);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mTweetListFragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mClient = TwitterApp.getRestClient();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getUserInfo();

    }

    private void getUserInfo() {
        mConnectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            mClient.getCurrentUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        currentUser = User.fromJSON(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    super.onSuccess(statusCode, headers, response);
                }
            });
        } else {
            Toast.makeText(this, R.string.no_internet_connectivity, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSubmitTweet(String text) {

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            mClient.postTweet(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    HomeTimelineFragment homeTimelineFragment =
                            (HomeTimelineFragment) mTweetListFragmentPagerAdapter.getRegisteredFragment(0);
                    homeTimelineFragment.prependTweet(response);
                }
            }, text);
        }
    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        launchProfileActivity(tweet.getUser());
    }
}
