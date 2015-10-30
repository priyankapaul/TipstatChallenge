package com.tipstat.tipstatchallenge.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tipstat.tipstatchallenge.R;
import com.tipstat.tipstatchallenge.controller.BaseApplication;
import com.tipstat.tipstatchallenge.models.Member;

import java.util.List;

/**
 * Created by mithun on 24/10/15.
 */
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ArticleViewHolder> {
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    OnItemClickListener onItemClickListener;

    ImageLoader imageLoader = BaseApplication.getInstance().getImageLoader();

    List<Member> members;

    public MembersAdapter(List<Member> members) {
        this.members = members;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        ArticleViewHolder avh = new ArticleViewHolder(view);
        return avh;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        if(imageLoader==null)
            imageLoader = BaseApplication.getInstance().getImageLoader();
        holder.articleDesc.setText(members.get(position).getStatus());
        holder.articlePhoto.setImageUrl(members.get(position).getImage(), imageLoader);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        NetworkImageView articlePhoto;
        TextView articleDesc;
        Button articleMore;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view_member);
            articlePhoto = (NetworkImageView) itemView.findViewById(R.id.member_photo);
            articleDesc = (TextView) itemView.findViewById(R.id.member_desc);
            articleMore = (Button) itemView.findViewById(R.id.member_more);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null) {
                onItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}