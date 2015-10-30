package com.tipstat.tipstatchallenge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.tipstat.tipstatchallenge.R;
import com.tipstat.tipstatchallenge.common.Constants;
import com.tipstat.tipstatchallenge.models.Member;

/**
 * Created by mithun on 24/10/15
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Profile");

        Intent intent = getIntent();
        Member member = intent.getParcelableExtra(Constants.EXTRA_MEMBER);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_MEMBER, member);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, profileFragment).commit();
    }
}
