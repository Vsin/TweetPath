package com.codepath.apps.tweetpath.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.tweetpath.R;
import com.codepath.apps.tweetpath.TwitterApp;
import com.codepath.apps.tweetpath.adapters.TweetAdapter;
import com.codepath.apps.tweetpath.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweetpath.models.Tweet;
import com.codepath.apps.tweetpath.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Vsin on 10/1/17.
 */

public class TweetsListFragment extends Fragment {

    TweetAdapter mTweetAdapter;
    List<Tweet> mTweets;
    private RecyclerView mRvTweets;
    private LinearLayoutManager mLinearLayoutManager;

    public TweetsListFragment() {
        // required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweet_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setupRecyclerView(view);
        initiateTimeline();
        setupScrollListener();
        super.onViewCreated(view, savedInstanceState);
    }

    public void initiateTimeline() {
    }

    private void setupRecyclerView(View v) {

        mTweets = new ArrayList<>();
        mTweetAdapter = new TweetAdapter(mTweets);
        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mRvTweets = v.findViewById(R.id.rvTweet);
        mRvTweets.setAdapter(mTweetAdapter);
        mRvTweets.setLayoutManager(mLinearLayoutManager);
    }

    public void populateTimeline(JSONArray response) {
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

    public void setupScrollListener() {
        EndlessRecyclerViewScrollListener mScrollListener = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                ConnectivityManager mConnectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    TwitterClient client = TwitterApp.getRestClient();
                    client.getHomeTimeline(new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            populateMoreTimeline();
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
            }
        };

        mRvTweets.addOnScrollListener(mScrollListener);
    }

    public void populateMoreTimeline() {

    }

    public void submitTweet(JSONObject response) {
        Tweet tweet;
        try {
            tweet = Tweet.fromJSON(response);
            mTweets.add(0, tweet);
            mTweetAdapter.notifyItemInserted(0);
            mRvTweets.scrollToPosition(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
