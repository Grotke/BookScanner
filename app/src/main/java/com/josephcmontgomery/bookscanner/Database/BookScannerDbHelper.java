package com.josephcmontgomery.bookscanner.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joseph on 12/14/2015.
 */
public class BookScannerDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BookScanner.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_BOOKS_TABLE =     "CREATE TABLE " + BookScannerContract.Books.TABLE_NAME + " (" +
            BookScannerContract.Books._ID + " INTEGER PRIMARY KEY autoincrement, " +
            BookScannerContract.Books.COLUMN_NAME_ISBN + " TEXT not null unique" + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_SUBTITLE + TEXT_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_PAGE_COUNT + INTEGER_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_AVERAGE_RATING + REAL_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_RATINGS_COUNT + INTEGER_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_DATE_SCANNED + TEXT_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_IMAGE_URL + TEXT_TYPE +
            " );";

    public BookScannerDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        createAllTables(db);
    }

    private void createAllTables(SQLiteDatabase db){
        String[] SQL_CREATE_ENTRIES = {SQL_CREATE_BOOKS_TABLE};
        for(String query: SQL_CREATE_ENTRIES){
            db.execSQL(query);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
