package com.example.android.newsapp.data;

import android.provider.BaseColumns;

/**
 * Created by Siriporn on 7/23/2017.
 */

//create contract to store news
public class Contract {

    public static class TABLE_NEWS implements BaseColumns {
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_URL_TO_IMAGE = "url_to_image";
        public static final String COLUMN_NAME_DATE = "date";
    }
}

