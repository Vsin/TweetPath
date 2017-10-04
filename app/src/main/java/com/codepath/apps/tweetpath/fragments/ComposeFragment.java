package com.codepath.apps.tweetpath.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweetpath.R;
import com.codepath.apps.tweetpath.models.User;

public class ComposeFragment extends DialogFragment {

    private OnTweetSubmitListener mListener;
    private EditText mComposeEditText;
    private String profileImageUrl;

    public ComposeFragment() {
        // Required empty public constructor
    }

    public static ComposeFragment newInstance(User user) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("profileImageUrl", user.getProfileImageUrl());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileImageUrl = getArguments().getString("profileImageUrl", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        Button mTweetButton;
        ImageButton mCancelButton;
        ImageView mProfileImageView;

        mComposeEditText = view.findViewById(R.id.etCompose);
        mCancelButton = view.findViewById(R.id.ibCancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mTweetButton = view.findViewById(R.id.btnTweet);
        mTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTweetButtonClick();
            }
        });
        mProfileImageView = view.findViewById(R.id.ivProfileIcon);
        Glide.with(this).load(profileImageUrl).into(mProfileImageView);
    }

    private void onTweetButtonClick() {
        if (mListener != null) {
            mListener.onSubmitTweet(mComposeEditText.getText().toString());
        }
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTweetSubmitListener) {
            mListener = (OnTweetSubmitListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTweetSubmitListener {
        void onSubmitTweet(String text);
    }

}
