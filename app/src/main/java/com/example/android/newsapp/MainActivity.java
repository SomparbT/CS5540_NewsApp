package com.example.android.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.data.Contract;
import com.example.android.newsapp.data.DBHelper;
import com.example.android.newsapp.data.DatabaseUtils;
import com.example.android.newsapp.data.NewsItem;
import com.example.android.newsapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Void>, NewsAdapter.ItemClickListener{


    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private Cursor mCursor;
    private SQLiteDatabase mDb;

    static final String TAG = "mainActivity";

    //Create a constant int to uniquely identify loader.
    private static final int NEWS_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_news);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //check never installed by get sharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirst = prefs.getBoolean("isfirst", true);

        //if first time installed, load news from network to database
        if (isFirst) {
            load();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isfirst", false);
            editor.commit();
        }

        /*
         * Initialize the loader
         */
        getSupportLoaderManager().initLoader(NEWS_LOADER, null, this);

        //create job schedule
        ScheduleUtilities.scheduleRefresh(this);
    }



    @Override
    protected void onStart() {
        super.onStart();
        mDb = new DBHelper(MainActivity.this).getReadableDatabase();
        mCursor = DatabaseUtils.getAll(mDb);
        mAdapter = new NewsAdapter(mCursor, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDb.close();
        mCursor.close();
    }

    //use asyncTaskLoader to load news from network to db
    @Override
    public Loader<Void> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Void>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public Void loadInBackground() {
                RefreshTasks.refreshNews(MainActivity.this);
                return null;
            }

        };
    }

    //load news from db to recyclerView
    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        mLoadingIndicator.setVisibility(View.GONE);
        mDb = new DBHelper(MainActivity.this).getReadableDatabase();
        mCursor = DatabaseUtils.getAll(mDb);

        mAdapter = new NewsAdapter(mCursor, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    //when click open link to news website
    @Override
    public void onItemClick(Cursor cursor, int clickedItemIndex) {
        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_URL));
        Log.d(TAG, String.format("Url %s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //refresh when click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_refresh) {
            load();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void load() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();

    }
}
