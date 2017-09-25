package com.codepath.apps.tweetpath.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vsin on 9/25/17.
 */

public class User {

    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;

    static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();

        user.name = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url");

        return user;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
