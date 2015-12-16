package com.josephcmontgomery.bookscanner;

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
        public static final String COLUMN_NAME_PUBLISHED_DATE = "publishedDate";
        public static final String COLUMN_NAME_AVERAGE_RATING = "averageRating";
        public static final String COLUMN_NAME_RATINGS_COUNT = "ratingsCount";
        public static final String COLUMN_NAME_DATE_SCANNED = "dateScanned";
        public static final String COLUMN_NAME_LOCATION_ID = "locationId";
    }

    /*public static abstract class Authors implements BaseColumns{
        public static final String TABLE_NAME ="authors";
        public static final String COLUMN_NAME_NAME = "name";
    } */

    public static abstract class AuthorsOfBooks implements BaseColumns{
        public static final String TABLE_NAME = "authorsOfBooks";
        public static final String COLUMN_NAME_BOOK_ID = "bookId";
        public static final String COLUMN_NAME_AUTHOR = "author";
    }

    public static abstract class Categories implements BaseColumns{
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }

    public static abstract class BookCategories implements BaseColumns{
        public static final String TABLE_NAME = "bookCategories";
        public static final String COLUMN_NAME_BOOK_ID = "bookId";
        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
    }

    public static abstract class Location implements BaseColumns{
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_NAME_BOOK_LOCATION = "bookLocation";
    }
}
