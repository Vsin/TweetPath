package com.codepath.apps.tweetpath.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

    private String mBody;
    private long uid;
    private String mCreatedAt;
    private User mUser;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.mBody = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
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
}
