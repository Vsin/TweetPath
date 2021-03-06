package com.codepath.apps.tweetpath.utils;

import android.content.Context;

import com.codepath.apps.tweetpath.R;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {

    private static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
    private static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    private static final String REST_CONSUMER_KEY = "cl9Zlbjx8SHVPxRSYEUq8IONa";       // Change this
    private static final String REST_CONSUMER_SECRET = "kEoei7eOUvLOJ6J2V4EFgqnz4HgMsT17GVksH61vuQIfk0fZRK"; // Change this
    private static final String HOME_TIMELINE_URN = "statuses/home_timeline.json";
    private static final String MENTIONS_TIMELINE_URN = "statuses/mentions_timeline.json";
    private static final String POST_STATUSES_URN = "statuses/update.json";
    private static final String USER_TIMELINE_URN = "statuses/user_timeline.json";
    private static final String ACCOUNT_VERIFY_CREDENTIALS_URN = "account/verify_credentials.json";

    // Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
    private static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

    // See https://developer.chrome.com/multidevice/android/intents
    private static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

    public TwitterClient(Context context) {
        super(context, REST_API_INSTANCE,
                REST_URL,
                REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET,
                String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
                        context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
    }

    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(HOME_TIMELINE_URN);
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count_id", 25);
        params.put("since_id", 1);
        client.get(apiUrl, params, handler);
    }

    public void getHomeTimeline(AsyncHttpResponseHandler handler, long maxId) {
        String apiUrl = getApiUrl(HOME_TIMELINE_URN);

        RequestParams params = new RequestParams();
        params.put("count_id", 25);
        params.put("max_id", maxId - 1);
        client.get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(MENTIONS_TIMELINE_URN);
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count_id", 25);
        params.put("since_id", 1);
        client.get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(AsyncHttpResponseHandler handler, long maxId) {
        String apiUrl = getApiUrl(MENTIONS_TIMELINE_URN);
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count_id", 25);
        params.put("max_id", maxId - 1);
        client.get(apiUrl, params, handler);
    }

    public void postTweet(AsyncHttpResponseHandler handler, String text) {
        String apiUrl = getApiUrl(POST_STATUSES_URN);

        RequestParams params = new RequestParams();
        params.put("status", text);
        client.post(apiUrl, params, handler);
    }

    public void getCurrentUser(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ACCOUNT_VERIFY_CREDENTIALS_URN);

        client.get(apiUrl, handler);
    }

    public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(USER_TIMELINE_URN);
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        params.put("count_id", 25);
        params.put("since_id", 1);
        client.get(apiUrl, params, handler);

    }

    public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler, long maxId) {
        String apiUrl = getApiUrl(USER_TIMELINE_URN);
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        params.put("count_id", 25);
        params.put("max_id", maxId - 1);
        client.get(apiUrl, params, handler);

    }

}
