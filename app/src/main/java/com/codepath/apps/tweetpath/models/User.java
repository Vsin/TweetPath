package com.codepath.apps.tweetpath.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vsin on 9/25/17.
 */

public class User {

    private String mName;
    private long uid;
    private String mScreenName;
    private String mProfileImageUrl;

    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();

        user.mName = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.mScreenName = jsonObject.getString("screen_name");
        user.mProfileImageUrl = jsonObject.getString("profile_image_url");

        return user;
    }

    public String getName() {
        return mName;
    }

    public String getProfileImageUrl() {
        return mProfileImageUrl;
    }

    public String getScreenNameForDisplay() { return "@" + mScreenName; }
}
