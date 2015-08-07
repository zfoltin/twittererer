package com.crowdmix.twittererer.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdmix.twittererer.App;
import com.crowdmix.twittererer.R;
import com.crowdmix.twittererer.models.TimelineItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private List<TimelineItem> timelineItems;

    public void setItems(List<TimelineItem> timelineItems) {
        this.timelineItems = timelineItems;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tweet, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TimelineItem timelineItem = timelineItems.get(i);
        Picasso.with(App.component().application())
                .load(timelineItem.getUser().getProfileImageUrl())
                .fit()
                .into(viewHolder.avatarImageView);
        viewHolder.nameView.setText(timelineItem.getUser().getName());
        viewHolder.handleView.setText("@" + timelineItem.getUser().getScreenName());
        viewHolder.timeView.setText(timelineItem.getCreatedAt());
        viewHolder.textView.setText(Html.fromHtml(timelineItem.getText()));
    }

    @Override
    public int getItemCount() {
        return timelineItems != null ? timelineItems.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.avatar)
        ImageView avatarImageView;
        @Bind(R.id.name)
        TextView nameView;
        @Bind(R.id.handle)
        TextView handleView;
        @Bind(R.id.time)
        TextView timeView;
        @Bind(R.id.text)
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
