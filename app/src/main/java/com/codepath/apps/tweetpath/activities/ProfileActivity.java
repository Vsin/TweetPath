package com.codepath.apps.tweetpath.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweetpath.R;
import com.codepath.apps.tweetpath.fragments.UserTimelineFragment;
import com.codepath.apps.tweetpath.models.User;

public class ProfileActivity extends AppCompatActivity {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mUser = getIntent().getParcelableExtra("user");

        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(mUser);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.flContainer, userTimelineFragment);
        ft.commit();

        try {
            getSupportActionBar().setTitle(mUser.getScreenName());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        populateUserHeadline();
    }

    private void populateUserHeadline() {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(mUser.getName());
        tvTagline.setText(mUser.getTagline());
        tvFollowers.setText(mUser.getFollowersCount() + " Followers");
        tvFollowing.setText(mUser.getFollowingCount() + " Following");
        Glide.with(this).load(mUser.getProfileImageUrl()).into(ivProfileImage);
    }
}
