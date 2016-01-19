package com.josephcmontgomery.bookscanner.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.josephcmontgomery.bookscanner.BookInformation;
/**
 * Created by Joseph on 12/15/2015.
 */
public class Database {
    private static SQLiteDatabase db = null;
    private static BookScannerDbHelper helper;


    public static void insertBook(BookInformation book, Context context){
        if(db == null){
            initializeDB(context);
        }

        db.insertWithOnConflict(BookScannerContract.Books.TABLE_NAME, "null", packBookValues(book), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static Cursor getAllBooks(Context context){
        String getAllBooksQuery = "select " +
                "t1."+ BookScannerContract.Books._ID + " as _id, " +
                BookScannerContract.Books.COLUMN_NAME_ISBN + " as isbn, " +
                BookScannerContract.Books.COLUMN_NAME_TITLE + " as title, "+
                BookScannerContract.Books.COLUMN_NAME_SUBTITLE + " as subtitle, "+
                BookScannerContract.Books.COLUMN_NAME_DESCRIPTION + " as description, " +
                BookScannerContract.Books.COLUMN_NAME_PAGE_COUNT + " as pageCount, " +
                BookScannerContract.Books.COLUMN_NAME_AVERAGE_RATING + " as avRating, " +
                BookScannerContract.Books.COLUMN_NAME_RATINGS_COUNT + " as ratingsCount, " +
                BookScannerContract.Books.COLUMN_NAME_DATE_SCANNED + " as dateScanned, " +
                BookScannerContract.Books.COLUMN_NAME_IMAGE_URL + " as imageUrl, " +
                BookScannerContract.Books.COLUMN_NAME_LOCATION + " as location from " +
                BookScannerContract.Books.TABLE_NAME + " as t1";
                if(db == null){
                    initializeDB(context);
                }
                return db.rawQuery(getAllBooksQuery, null);
    }

    private static void initializeDB(Context context){
        helper = new BookScannerDbHelper(context);
        context.deleteDatabase(helper.DATABASE_NAME);
        db = helper.getWritableDatabase();
    }



    private static ContentValues packBookValues(BookInformation book){
        ContentValues values = new ContentValues();
        values.put(BookScannerContract.Books.COLUMN_NAME_ISBN, book.isbn);
        values.put(BookScannerContract.Books.COLUMN_NAME_TITLE, book.title);
        values.put(BookScannerContract.Books.COLUMN_NAME_SUBTITLE, book.subtitle);
        values.put(BookScannerContract.Books.COLUMN_NAME_AVERAGE_RATING, book.averageRating);
        values.put(BookScannerContract.Books.COLUMN_NAME_DESCRIPTION, book.description);
        values.put(BookScannerContract.Books.COLUMN_NAME_PAGE_COUNT, book.pageCount);
        values.put(BookScannerContract.Books.COLUMN_NAME_RATINGS_COUNT, book.ratingsCount);
        values.put(BookScannerContract.Books.COLUMN_NAME_DATE_SCANNED, System.currentTimeMillis());
        values.put(BookScannerContract.Books.COLUMN_NAME_IMAGE_URL, book.imageURL);
        values.put(BookScannerContract.Books.COLUMN_NAME_LOCATION, book.location);
        return values;
    }

    private static long getIdIfValuesInTable(String[] values, String[] fieldNames, String tableName){
        String query = "select * from " + tableName + " where ";
        for(int i = 0; i < fieldNames.length; i++){
            if(i != 0){
                query = query + " and ";
            }
           query = query + fieldNames[i] + " = " + values[i];
        }

        Cursor result = db.rawQuery(query, null);
        if(result == null || result.getCount() < 1){
            return -1;
        }
        result.moveToFirst();
        return result.getInt(0);
    }

    public static void close(){
        db.close();
    }
}
