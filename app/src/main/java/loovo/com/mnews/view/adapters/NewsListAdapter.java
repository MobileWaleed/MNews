package loovo.com.mnews.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import loovo.com.mnews.R;
import loovo.com.mnews.db.entity.NewsItem;
import loovo.com.mnews.utils.Utils;
import loovo.com.mnews.view.callback.NewsCallback;


public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {

    private List<NewsItem> mNewsList = new ArrayList<>();
    private NewsCallback mNewsCallback;

    public NewsListAdapter(NewsCallback mNewsCallback) {
        this.mNewsCallback = mNewsCallback;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_items, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

        NewsItem newsItem = mNewsList.get(position);
        Glide.with(holder.newsImage.getContext())
                .load(newsItem.urlToImage)
                .into(holder.newsImage);

        holder.itemView.setOnClickListener(view -> mNewsCallback.onClick(newsItem));
        holder.domain.setText(newsItem.getSource().getName());
        holder.title.setText(newsItem.title);
        holder.author.setText(newsItem.author);
        holder.relativeTime.setText(Utils.getRelativeTimeFromDate(Utils.convertStringToDate(newsItem.publishedDate)));

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void setData(List<NewsItem> newsList) {
        this.mNewsList = newsList;
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        mNewsList.clear();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class
     */
    public class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.featureImage)
        public ImageView newsImage;
        @BindView(R.id.domain)
        public TextView domain;
        @BindView(R.id.title)
        public TextView title;
        @BindView(R.id.author)
        public TextView author;
        @BindView(R.id.relativeTime)
        public TextView relativeTime;
        public View itemView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.itemView = itemView;
        }
    }

}
