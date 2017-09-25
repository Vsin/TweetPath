package com.codepath.apps.tweetpath.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.tweetpath.R;
import com.codepath.apps.tweetpath.TwitterApp;
import com.codepath.apps.tweetpath.adapters.TweetAdapter;
import com.codepath.apps.tweetpath.models.Tweet;
import com.codepath.apps.tweetpath.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetAdapter tweetAdapter;
    private List<Tweet> tweets;
    private RecyclerView rvTweets;
    private ConnectivityManager mConnectivityManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient();

        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets);
        rvTweets.setAdapter(tweetAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        mConnectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        populateTimeline();
    }

    private void populateTimeline() {

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            client.getHomeTimeline(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("Twitter Client", response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("Twitter Client", response.toString());

                    for (int i = 0; i < response.length(); ++i) {
                        Tweet tweet;
                        try {
                            Log.d("Populate Timeline", "Adding Tweet " + i);
                            tweet = Tweet.fromJSON(response.getJSONObject(i));
                            tweets.add(tweet);
                            Log.d("Populate Timeline", "Tweet Count: " + tweets.size());
                            tweetAdapter.notifyItemInserted(tweets.size() - 1);
                            Log.d("Populate Timeline", "Tweet Adapter Count: " + tweetAdapter.getItemCount());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("Twitter Client", responseString);
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Twitter Client", errorResponse.toString());
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("Twitter Client", errorResponse.toString());
                    throwable.printStackTrace();
                }
            });
        }
    }
}
