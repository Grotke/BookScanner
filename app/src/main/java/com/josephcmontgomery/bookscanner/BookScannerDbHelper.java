package com.josephcmontgomery.bookscanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
            BookScannerContract.Books.COLUMN_NAME_ISBN + TEXT_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_SUBTITLE + TEXT_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_PAGE_COUNT + INTEGER_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_AVERAGE_RATING + REAL_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_RATINGS_COUNT + INTEGER_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_DATE_SCANNED + TEXT_TYPE + COMMA_SEP +
            BookScannerContract.Books.COLUMN_NAME_LOCATION_ID + INTEGER_TYPE +
            " );";

    private static final String SQL_CREATE_AUTHORS_OF_BOOKS_TABLE =
            "CREATE TABLE " + BookScannerContract.AuthorsOfBooks.TABLE_NAME + " (" +
                    BookScannerContract.AuthorsOfBooks._ID + " INTEGER PRIMARY KEY autoincrement, " +
                    BookScannerContract.AuthorsOfBooks.COLUMN_NAME_BOOK_ID + INTEGER_TYPE + COMMA_SEP +
                    BookScannerContract.AuthorsOfBooks.COLUMN_NAME_AUTHOR_ID + INTEGER_TYPE +
                    " );";

    private static final String SQL_CREATE_CATEGORIES_TABLE =
            "CREATE TABLE " + BookScannerContract.Categories.TABLE_NAME + " (" +
                    BookScannerContract.Categories._ID + " INTEGER PRIMARY KEY autoincrement, " +
                    BookScannerContract.Categories.COLUMN_NAME_CATEGORY + TEXT_TYPE +
    " );";

    private static final String SQL_CREATE_LOCATION_TABLE =
            "CREATE TABLE " + BookScannerContract.Location.TABLE_NAME + " (" +
                    BookScannerContract.Location._ID + " INTEGER PRIMARY KEY autoincrement, " +
                    BookScannerContract.Location.COLUMN_NAME_BOOK_LOCATION + TEXT_TYPE +
                    " );";

    private static final String SQL_CREATE_BOOK_CATEGORIES_TABLE =
            "CREATE TABLE " + BookScannerContract.BookCategories.TABLE_NAME + " (" +
                    BookScannerContract.BookCategories._ID + " INTEGER PRIMARY KEY autoincrement, " +
                    BookScannerContract.BookCategories.COLUMN_NAME_BOOK_ID + INTEGER_TYPE + COMMA_SEP +
                    BookScannerContract.BookCategories.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE +
                    " );";

    private static final String SQL_CREATE_AUTHORS_TABLE =
            "CREATE TABLE " + BookScannerContract.Authors.TABLE_NAME + " (" +
                    BookScannerContract.Authors._ID + " INTEGER PRIMARY KEY autoincrement, " +
                    BookScannerContract.Authors.COLUMN_NAME_NAME + TEXT_TYPE +
                    " );";

    /*private static final String SQL_CREATE_ENTRIES = SQL_CREATE_BOOKS_TABLE + SQL_CREATE_BOOK_CATEGORIES_TABLE
            + SQL_CREATE_AUTHORS_OF_BOOKS_TABLE + SQL_CREATE_CATEGORIES_TABLE
            + SQL_CREATE_LOCATION_TABLE + SQL_CREATE_AUTHORS_TABLE;*/


    public BookScannerDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        createAllTables(db);
    }

    private void createAllTables(SQLiteDatabase db){
        Log.e("CREATION", "Creating Stuff");
        String[] SQL_CREATE_ENTRIES = {SQL_CREATE_BOOKS_TABLE, SQL_CREATE_BOOK_CATEGORIES_TABLE,
                SQL_CREATE_AUTHORS_OF_BOOKS_TABLE, SQL_CREATE_CATEGORIES_TABLE,
                SQL_CREATE_LOCATION_TABLE, SQL_CREATE_AUTHORS_TABLE};
        for(String query: SQL_CREATE_ENTRIES){
            db.execSQL(query);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //db.execSQL(SQL_DELETE_ENTRIES);
        //onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.e("OPENING", "Opened Database");
        super.onOpen(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
