package com.tipstat.tipstatchallenge.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tipstat.tipstatchallenge.R;
import com.tipstat.tipstatchallenge.adapter.MembersAdapter;
import com.tipstat.tipstatchallenge.common.Constants;
import com.tipstat.tipstatchallenge.controller.BaseApplication;
import com.tipstat.tipstatchallenge.models.Member;
import com.tipstat.tipstatchallenge.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by mithun on 24/10/15
 */
public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getSimpleName();

    ArrayList<Member> members = new ArrayList<Member>();
    ArrayList<Member> filteredMembers = new ArrayList<Member>();
    RecyclerView rvMembers;
    TextView tvFilter, tvTotalMember, tvApiHits;
    RelativeLayout filterLayout;

    Button tvHeight, tvWeight;

    private ProgressDialog dialog;
    private MembersAdapter adapter;
    private LinkedHashSet<Member.Ethnicity> ethnicities = new LinkedHashSet<Member.Ethnicity>();
    private Member.Ethnicity currentEthnicity;
    private int currentEthnicityCursor;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        dialog.setMessage("Loading...");
        showPDialog();

        members.clear();
        filteredMembers.clear();
        // Adding request to request queue
        BaseApplication.getInstance().addToRequestQueue(getMembersRequest());
        BaseApplication.getInstance().addToRequestQueue(getApiHitsRequest());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);
        filterLayout = (RelativeLayout) rootView.findViewById(R.id.filter_layout);
        tvFilter = (TextView) rootView.findViewById(R.id.tv_filter_value);
        tvTotalMember = (TextView) rootView.findViewById(R.id.tv_total_member_count);
        tvApiHits = (TextView) rootView.findViewById(R.id.tv_api_hits_count);

        tvHeight = (Button) rootView.findViewById(R.id.tv_height);
        tvWeight = (Button) rootView.findViewById(R.id.tv_weight);

        tvHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(filteredMembers, new HeightComparator());
                adapter.notifyDataSetChanged();
            }
        });

        tvWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(filteredMembers, new WeightComparator());
                adapter.notifyDataSetChanged();
            }
        });

        rvMembers = (RecyclerView) rootView.findViewById(R.id.rv_articles);
        configureRecyclerView(rvMembers);

        filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<Member.Ethnicity> iterator = ethnicities.iterator();
                while (iterator.hasNext()) {
                    Member.Ethnicity e = iterator.next();
                    if (e.ordinal() == currentEthnicity.ordinal())
                        break;
                }

                if (iterator.hasNext()) {
                    currentEthnicity = iterator.next();
                    filterList();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void configureRecyclerView(RecyclerView rv) {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        adapter = new MembersAdapter(filteredMembers);
        adapter.setOnItemClickListener(new MembersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Member member = filteredMembers.get(position);
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(Constants.EXTRA_MEMBER, member);
                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);
    }

    // hide/show Dialogs
    private void showPDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    private void hidePDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void filterList() {
        filteredMembers.clear();
        tvFilter.setText(Utils.getFilterText(currentEthnicity));
        for (Member member : members) {
            if (member.getEthnicity().ordinal() == currentEthnicity.ordinal()) {
                filteredMembers.add(member);
            }
        }
        adapter.notifyDataSetChanged();
    }

    // Volley request
    JsonObjectRequest getMembersRequest() {
        // Creating volley request obj
        JsonObjectRequest memberReq = new JsonObjectRequest(Request.Method.GET, Constants.NEWS_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hidePDialog();

                // Parsing json
                try {
                    if (response.has("members")) {
                        JSONArray array = response.getJSONArray("members");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);

                            String id = item.getString("id");
                            String dob = item.getString("dob");
                            String status = item.getString("status");
                            String ethnicity = item.getString("ethnicity");
                            String weight = item.getString("weight");
                            String height = item.getString("height");
                            String is_veg = item.getString("is_veg");
                            boolean isVeg = "1".equals(is_veg) ? true : false;
                            String strDrink = item.getString("drink");
                            boolean drink = "1".equals(strDrink) ? true : false;
                            String image = item.getString("image");

                            Member member = new Member(id, dob, status, ethnicity, weight, height, isVeg, drink, image);
                            ethnicities.add(member.getEthnicity());
                            members.add(member);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (members.size() > 0) {
                    currentEthnicity = members.get(0).getEthnicity();
                    filterList();
                }

                tvTotalMember.setText("" + members.size());
                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        int socketTimeout = 30000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        memberReq.setRetryPolicy(policy);

        return memberReq;
    }

    // Volley request
    JsonObjectRequest getApiHitsRequest() {
        // Creating volley request obj
        JsonObjectRequest apiHitsReq = new JsonObjectRequest(Request.Method.GET, Constants.API_HTIS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Parsing json
                try {
                    if (response.has("api_hits")) {
                        String apiHitsCount = response.getString("api_hits");
                        tvApiHits.setText(apiHitsCount);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        int socketTimeout = 30000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        apiHitsReq.setRetryPolicy(policy);

        return apiHitsReq;
    }

    class HeightComparator implements Comparator<Member> {

        @Override
        public int compare(Member lhs, Member rhs) {
            return (int)(lhs.getHeight()-rhs.getHeight());
        }
    }

    class WeightComparator implements Comparator<Member> {

        @Override
        public int compare(Member lhs, Member rhs) {
            return (int)(lhs.getWeight()-rhs.getWeight());
        }
    }
}
