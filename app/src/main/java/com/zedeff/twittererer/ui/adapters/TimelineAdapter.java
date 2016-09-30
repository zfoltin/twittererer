package com.zedeff.twittererer.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zedeff.twittererer.App;
import com.zedeff.twittererer.R;
import com.zedeff.twittererer.models.TimelineItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
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
        viewHolder.handleView.setText(String.format("@%s", timelineItem.getUser().getScreenName()));
        viewHolder.timeView.setText(timelineItem.getCreatedAt());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            viewHolder.textView.setText(Html.fromHtml(timelineItem.getText(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            //noinspection deprecation
            viewHolder.textView.setText(Html.fromHtml(timelineItem.getText()));
        }
    }

    @Override
    public int getItemCount() {
        return timelineItems != null ? timelineItems.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        ImageView avatarImageView;
        @BindView(R.id.name)
        TextView nameView;
        @BindView(R.id.handle)
        TextView handleView;
        @BindView(R.id.tweet_time)
        TextView timeView;
        @BindView(R.id.tweet_text)
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
