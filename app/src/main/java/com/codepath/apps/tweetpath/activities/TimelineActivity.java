package com.codepath.apps.tweetpath.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.tweetpath.R;
import com.codepath.apps.tweetpath.TwitterApp;
import com.codepath.apps.tweetpath.fragments.ComposeFragment;
import com.codepath.apps.tweetpath.fragments.TweetsListFragment;
import com.codepath.apps.tweetpath.models.User;
import com.codepath.apps.tweetpath.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeFragment.OnTweetSubmitListener {

    private ConnectivityManager mConnectivityManager;
    private User currentUser;
    private TweetsListFragment mFragmentHomeTimeline;
    private TweetsListFragment mFragmentMentionsTimeline;
    private TwitterClient mClient;

    @Override
    public void submitTweet(String text) {

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            mClient.postTweet(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    mFragmentHomeTimeline.submitTweet(response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("Submit Tweet Array: ", String.valueOf(statusCode));
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            }, text);
        }
    }

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

        return super.onOptionsItemSelected(item);
    }

    private void launchComposeActivity() {
        FragmentManager fm = getSupportFragmentManager();

        ComposeFragment composeFragment = ComposeFragment.newInstance(currentUser.getProfileImageUrl());
        composeFragment.show(fm, "compose");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mClient = TwitterApp.getRestClient();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        mFragmentHomeTimeline = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        mFragmentMentionsTimeline = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);

        mConnectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            getUserInfo();
        } else {
            Toast.makeText(this, "No internet connectivity.", Toast.LENGTH_LONG).show();
        }

    }

    private void getUserInfo() {
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

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
