package com.codepath.apps.tweetpath.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.tweetpath.fragments.HomeTimelineFragment;
import com.codepath.apps.tweetpath.fragments.MentionsTimelineFragment;

public class TweetListFragmentPagerAdapter extends SmartFragmentStatePagerAdapter {

    private static final int NUM_ITEMS = 2;
    private final String tabTitles[] = new String[] {"Home", "Mentions"};
    private enum FRAGMENT { HomeTimeline, MentionsTimeline }


    public TweetListFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        if (position == FRAGMENT.HomeTimeline.ordinal()) return new HomeTimelineFragment();
        if (position == FRAGMENT.MentionsTimeline.ordinal()) return new MentionsTimelineFragment();
        return null;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
