package com.josephcmontgomery.bookscanner.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.josephcmontgomery.bookscanner.Tools.BookInformation;

public class Database {
    private static SQLiteDatabase db = null;

    public static void insertBook(BookInformation book, Context context){
        if(db == null){
            initializeDB(context);
        }

        db.insertWithOnConflict(BookScannerContract.Books.TABLE_NAME, "null", packBookContentValues(book), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static Cursor getAllBooks(Context context){
        String getAllBooksQuery = "select * from " + BookScannerContract.Books.TABLE_NAME;
        if(db == null){
            initializeDB(context);
        }
        return db.rawQuery(getAllBooksQuery, null);
    }

    private static void initializeDB(Context context){
        //Delete database for debugging purposes.
        context.deleteDatabase(BookScannerDbHelper.DATABASE_NAME);
        BookScannerDbHelper helper = new BookScannerDbHelper(context);
        db = helper.getWritableDatabase();
    }

    private static ContentValues packBookContentValues(BookInformation book){
        ContentValues values = new ContentValues();
        values.put(BookScannerContract.Books.COLUMN_NAME_ISBN, book.isbn);
        values.put(BookScannerContract.Books.COLUMN_NAME_TITLE, book.title);
        values.put(BookScannerContract.Books.COLUMN_NAME_SUBTITLE, book.subtitle);
        values.put(BookScannerContract.Books.COLUMN_NAME_AVERAGE_RATING, book.averageRating);
        values.put(BookScannerContract.Books.COLUMN_NAME_DESCRIPTION, book.description);
        values.put(BookScannerContract.Books.COLUMN_NAME_PAGE_COUNT, book.pageCount);
        values.put(BookScannerContract.Books.COLUMN_NAME_RATINGS_COUNT, book.ratingsCount);
        values.put(BookScannerContract.Books.COLUMN_NAME_DATE_SCANNED, book.timeLastUpdated);
        values.put(BookScannerContract.Books.COLUMN_NAME_IMAGE_URL, book.imageURL);
        values.put(BookScannerContract.Books.COLUMN_NAME_LOCATION, book.location);
        return values;
    }

    public static void close(){
        db.close();
    }
}
