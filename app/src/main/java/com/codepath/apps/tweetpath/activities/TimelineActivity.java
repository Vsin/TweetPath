package com.codepath.apps.tweetpath.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.tweetpath.R;
import com.codepath.apps.tweetpath.TwitterApp;
import com.codepath.apps.tweetpath.adapters.TweetAdapter;
import com.codepath.apps.tweetpath.fragments.ComposeFragment;
import com.codepath.apps.tweetpath.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweetpath.models.Tweet;
import com.codepath.apps.tweetpath.models.User;
import com.codepath.apps.tweetpath.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeFragment.OnTweetSubmitListener {

    LinearLayoutManager mLinearLayoutManager;
    private TwitterClient mClient;
    private TweetAdapter mTweetAdapter;
    private List<Tweet> mTweets;
    private RecyclerView mRvTweets;
    private ConnectivityManager mConnectivityManager;
    private User currentUser;

    @Override
    public void submitTweet(String text) {
        mClient.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet tweet;
                Log.d("Submit Tweet Object: ", String.valueOf(statusCode));
                try {
                    tweet = Tweet.fromJSON(response);
                    mTweets.add(0, tweet);
                    mTweetAdapter.notifyItemInserted(0);
                    mRvTweets.scrollToPosition(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

        setupRecyclerView();

        mConnectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        populateTimeline();

        setupScrollListener();

        getUserInfo();
    }

    private void setupRecyclerView() {

        mTweets = new ArrayList<>();
        mTweetAdapter = new TweetAdapter(mTweets);
        mLinearLayoutManager = new LinearLayoutManager(this);

        mRvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        mRvTweets.setAdapter(mTweetAdapter);
        mRvTweets.setLayoutManager(mLinearLayoutManager);
    }

    private void populateTimeline() {

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            mClient.getHomeTimeline(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    for (int i = 0; i < response.length(); ++i) {
                        Tweet tweet;
                        try {
                            tweet = Tweet.fromJSON(response.getJSONObject(i));
                            mTweets.add(tweet);
                            mTweetAdapter.notifyItemInserted(mTweets.size() - 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    throwable.printStackTrace();
                }
            });
        }
    }

    private void setupScrollListener() {
        EndlessRecyclerViewScrollListener mScrollListener = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mClient.getMoreHomeTimeline(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                        for (int i = 0; i < response.length(); ++i) {
                            Tweet tweet;
                            try {
                                tweet = Tweet.fromJSON(response.getJSONObject(i));
                                mTweets.add(tweet);
                                mTweetAdapter.notifyItemInserted(mTweets.size() - 1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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
                }, Collections.min(mTweets).getID());
            }
        };

        mRvTweets.addOnScrollListener(mScrollListener);
    }

    public void getUserInfo() {
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
