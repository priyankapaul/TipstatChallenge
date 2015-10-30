package com.tipstat.tipstatchallenge.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tipstat.tipstatchallenge.R;
import com.tipstat.tipstatchallenge.common.Constants;
import com.tipstat.tipstatchallenge.controller.BaseApplication;
import com.tipstat.tipstatchallenge.models.Member;
import com.tipstat.tipstatchallenge.utils.Utils;

/**
 * Created by mithun on 24/10/15.
 */
public class ProfileFragment extends Fragment {

    Member member;
    ImageLoader imageLoader = BaseApplication.getInstance().getImageLoader();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            member = bundle.getParcelable(Constants.EXTRA_MEMBER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment_layout, container, false);

        TextView tvEthnicity = (TextView) rootView.findViewById(R.id.ethnicity);
        tvEthnicity.setText(Utils.getFilterText(member.getEthnicity()));

        NetworkImageView networkImageView = (NetworkImageView) rootView.findViewById(R.id.profile_picture);
        networkImageView.setImageUrl(member.getImage(), imageLoader);

        TextView tvBirthday = (TextView) rootView.findViewById(R.id.tv_birthday_value);
        tvBirthday.setText(member.getDob());

        TextView tvWeight = (TextView) rootView.findViewById(R.id.tv_weight_value);
        tvWeight.setText(getWeight(member.getWeight()));

        TextView tvHeight = (TextView) rootView.findViewById(R.id.tv_height_value);
        tvHeight.setText(""+member.getHeight());

        TextView tvVeg = (TextView) rootView.findViewById(R.id.tv_veg_value);
        tvVeg.setText(member.isVeg()? "Yes!":"No!");

        TextView tvDrink = (TextView) rootView.findViewById(R.id.tv_drink_value);
        tvDrink.setText(member.isDrink()? "Yes":"No");


        TextView tvStatus  = (TextView) rootView.findViewById(R.id.tv_status);
        tvStatus.setText(member.getStatus());

        return rootView;
    }

    private String getWeight(long weight) {
        float f = weight/100;
        return f+" KG";
    }

    private String getHeight(long height) {
        return height+" cm";
    }
}
