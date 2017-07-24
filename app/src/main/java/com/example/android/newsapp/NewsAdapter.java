package com.example.android.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.data.Contract;
import com.example.android.newsapp.data.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Siriporn on 6/22/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    //load data from database instead of network
    private ItemClickListener listener;
    private Cursor mCursor;
    private Context mContext;
    public static final String TAG = "NewsAdapter";

    public NewsAdapter(Cursor cursor, ItemClickListener listener){
        this.mCursor = cursor;
        this.listener = listener;
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
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
        return mCursor.getCount();
    }

    public interface ItemClickListener {
        void onItemClick(Cursor cursor, int clickedItemIndex);
    }

    class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mNewsTitleTextView;
        public final TextView mNewsDescriptionTextView;
        public final TextView mNewsDateTextView;
        public final ImageView mNewsImgImageView;


        public NewsAdapterViewHolder(View view){
            super(view);
            mNewsTitleTextView = (TextView)view.findViewById(R.id.tv_news_title);
            mNewsDescriptionTextView = (TextView)view.findViewById(R.id.tv_news_description);
            mNewsDateTextView = (TextView)view.findViewById(R.id.tv_news_date);
            mNewsImgImageView = (ImageView) view.findViewById(R.id.iv_img);

            view.setOnClickListener(this);
        }

        public void bind(int pos){
            //binding data from database to item
            //use picasso to show image
            mCursor.moveToPosition(pos);
            mNewsTitleTextView.setText(mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_TITLE)));
            mNewsDescriptionTextView.setText(mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION)));
            mNewsDateTextView.setText(mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_DATE)));
            String url = mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_URL_TO_IMAGE));
            Log.d(TAG, url);
            if(url != null){
                Picasso.with(mContext)
                        .load(url)
                        .into(mNewsImgImageView);
            }

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(mCursor, pos);
        }
    }
}
