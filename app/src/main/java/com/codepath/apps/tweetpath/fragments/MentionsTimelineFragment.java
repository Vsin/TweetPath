package com.codepath.apps.tweetpath.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codepath.apps.tweetpath.TwitterApp;
import com.codepath.apps.tweetpath.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Vsin on 10/1/17.
 */

public class MentionsTimelineFragment extends TweetsListFragment {
    private TwitterClient mClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClient = TwitterApp.getRestClient();
        populateTimeline();
        setupScrollListener();
    }

    private void populateTimeline() {
        mClient.getMentionsTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                populateTimeline(response);
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
