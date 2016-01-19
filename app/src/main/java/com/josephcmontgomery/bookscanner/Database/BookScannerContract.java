package com.josephcmontgomery.bookscanner.Database;

import android.provider.BaseColumns;

/**
 * Created by Joseph on 12/14/2015.
 */
public final class BookScannerContract {
    public BookScannerContract(){}

    public static abstract class Books implements BaseColumns {
        public static final String TABLE_NAME = "books";
        public static final String COLUMN_NAME_ISBN = "isbn";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PAGE_COUNT = "pageCount";
        public static final String COLUMN_NAME_AVERAGE_RATING = "averageRating";
        public static final String COLUMN_NAME_RATINGS_COUNT = "ratingsCount";
        public static final String COLUMN_NAME_DATE_SCANNED = "dateScanned";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_IMAGE_URL = "imageUrl";
    }
}