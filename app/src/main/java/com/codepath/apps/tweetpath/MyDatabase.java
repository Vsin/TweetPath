package com.codepath.apps.tweetpath;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {

    static final String NAME = "RestClientDatabase";

    static final int VERSION = 1;
}
