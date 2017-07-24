package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsapp.data.NewsItem;

import java.util.ArrayList;

/**
 * Created by Siriporn on 6/22/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    private ArrayList<NewsItem> mNewsData;
    ItemClickListener listener;

    public NewsAdapter(ArrayList<NewsItem> data, ItemClickListener listener){
        this.mNewsData = data;
        this.listener = listener;
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.news_item, parent, shouldAttachToParentImmediately);
        NewsAdapterViewHolder holder = new NewsAdapterViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNewsData.size();
    }

    public interface ItemClickListener {
        void onItemClick(int clickedItemIndex);
    }

    class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mNewsTitleTextView;
        public final TextView mNewsDescriptionTextView;
        public final TextView mNewsDateTextView;

        public NewsAdapterViewHolder(View view){
            super(view);
            mNewsTitleTextView = (TextView)view.findViewById(R.id.tv_news_title);
            mNewsDescriptionTextView = (TextView)view.findViewById(R.id.tv_news_description);
            mNewsDateTextView = (TextView)view.findViewById(R.id.tv_news_date);

            view.setOnClickListener(this);
        }

        public void bind(int pos){
            NewsItem item = mNewsData.get(pos);
            mNewsTitleTextView.setText(item.getTitle());
            mNewsDescriptionTextView.setText(item.getDescription());
            mNewsDateTextView.setText(item.getDate());
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        }
    }
}
