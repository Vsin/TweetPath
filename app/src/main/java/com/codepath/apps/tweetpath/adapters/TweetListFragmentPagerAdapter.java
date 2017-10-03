package com.codepath.apps.tweetpath.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.codepath.apps.tweetpath.activities.TimelineActivity;
import com.codepath.apps.tweetpath.fragments.HomeTimelineFragment;
import com.codepath.apps.tweetpath.fragments.MentionsTimelineFragment;

/**
 * Created by Vsin on 10/2/17.
 */

public class TweetListFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context mContext;

    public TweetListFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return new HomeTimelineFragment();
        if (position == 1) return new MentionsTimelineFragment();
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
