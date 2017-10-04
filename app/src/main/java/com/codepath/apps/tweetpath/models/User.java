package com.codepath.apps.tweetpath.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {

    private String mName;
    private long uid;
    private String mScreenName;
    private String mProfileImageUrl;
    private String mTagline;

    private int mFollowersCount;
    private int mFollowingCount;

    private User() {

    }

    private User(Parcel in) {
        mName = in.readString();
        uid = in.readLong();
        mScreenName = in.readString();
        mProfileImageUrl = in.readString();
        mTagline = in.readString();
        mFollowersCount = in.readInt();
        mFollowingCount = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();

        user.mName = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.mScreenName = jsonObject.getString("screen_name");
        user.mProfileImageUrl = jsonObject.getString("profile_image_url");
        user.mTagline = jsonObject.getString("description");
        user.mFollowersCount = jsonObject.getInt("followers_count");
        user.mFollowingCount = jsonObject.getInt("friends_count");

        return user;
    }

    public String getName() {
        return mName;
    }

    public String getProfileImageUrl() {
        return mProfileImageUrl;
    }

    public String getScreenNameForDisplay() { return "@" + mScreenName; }

    public String getScreenName() {
        return mScreenName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeLong(uid);
        parcel.writeString(mScreenName);
        parcel.writeString(mProfileImageUrl);
        parcel.writeString(mTagline);
        parcel.writeInt(mFollowersCount);
        parcel.writeInt(mFollowingCount);
    }

    public String getTagline() {
        return mTagline;
    }

    public int getFollowersCount() {
        return mFollowersCount;
    }

    public int getFollowingCount() {
        return mFollowingCount;
    }

}
