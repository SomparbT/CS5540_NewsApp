package com.example.android.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;
    TextView mErrorMessageDisplay;
    ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_newsapi_search_results_json);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link NetworkUtils}) for the news you'd like to find, displays
     * that URL in a TextView, and finally fires off an AsyncTask to perform the GET request using
     * our {@link NewsapiQueryTask}
     */
    private void makeNewsapiSearchQuery() {
        String newsapiQuery = mSearchBoxEditText.getText().toString();
        URL newsapiSearchUrl = NetworkUtils.buildUrl(newsapiQuery);
        mUrlDisplayTextView.setText(newsapiSearchUrl.toString());
        new NewsapiQueryTask().execute(newsapiSearchUrl);
    }

    public void showJsonDataView(){
        mErrorMessageDisplay.setVisibility(View.GONE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage(){
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mSearchResultsTextView.setVisibility(View.GONE);
    }
    public class NewsapiQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String newsapiSearchResults = null;
            try {
                newsapiSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newsapiSearchResults;
        }

        @Override
        protected void onPostExecute(String newsapiSearchResults) {
            mLoadingIndicator.setVisibility(View.GONE);
            if (newsapiSearchResults != null && !newsapiSearchResults.equals("")) {
                mSearchResultsTextView.setText(newsapiSearchResults);
                showJsonDataView();
            }else{
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeNewsapiSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
