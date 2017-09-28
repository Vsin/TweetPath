package com.codepath.apps.tweetpath.models;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class Tweet implements Comparator<Tweet>, Comparable<Tweet> {

    private String mBody;
    private long mID;
    private String mCreatedAt;
    private User mUser;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.mBody = jsonObject.getString("text");
        tweet.mID = jsonObject.getLong("id");
        tweet.mCreatedAt = jsonObject.getString("created_at");
        tweet.mUser = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;
    }

    public String getBody() {
        return mBody;
    }

    public User getUser() {
        return mUser;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    @Override
    public int compare(Tweet tweet, Tweet t1) {
        if (tweet.mID > t1.mID) {
            return 1;
        } else if (tweet.mID < t1.mID) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int compareTo(@NonNull Tweet tweet) {
        if (this.mID > tweet.mID) {
            return 1;
        } else if (this.mID < tweet.mID) {
            return -1;
        } else {
            return 0;
        }
    }

    public long getID() {
        return mID;
    }
}

