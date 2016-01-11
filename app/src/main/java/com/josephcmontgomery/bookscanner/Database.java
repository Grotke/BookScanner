package com.josephcmontgomery.bookscanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
//TODO: Push to Github. Fix database posting multiple records.
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
        insertMultipleRecords(book.authors, BookScannerContract.Authors.COLUMN_NAME_NAME, book.isbn, BookScannerContract.AuthorsOfBooks.COLUMN_NAME_BOOK_ID, BookScannerContract.AuthorsOfBooks.TABLE_NAME, BookScannerContract.AuthorsOfBooks.COLUMN_NAME_AUTHOR_ID, BookScannerContract.Authors.TABLE_NAME);
        insertMultipleRecords(book.categories, BookScannerContract.Categories.COLUMN_NAME_CATEGORY, book.isbn, BookScannerContract.BookCategories.COLUMN_NAME_BOOK_ID, BookScannerContract.BookCategories.TABLE_NAME, BookScannerContract.BookCategories.COLUMN_NAME_CATEGORY_ID, BookScannerContract.Categories.TABLE_NAME);
    }

    public static Cursor getAllBooks(Context context){
        String getAllBooksQuery = "select " +
                "t1."+BookScannerContract.Books._ID + " as _id, " +
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

    private static void insertMultipleRecords(ArrayList<String> values, String validationFieldName, String isbn, String bookIdFieldName, String targetTable, String targetFieldName, String validationTable){
        ContentValues targetValue = new ContentValues();
        String[] bookValue = {isbn};
        String[] bookFieldName = {BookScannerContract.Books.COLUMN_NAME_ISBN};
        long bookID = getIdIfValuesInTable(bookValue, bookFieldName, BookScannerContract.Books.TABLE_NAME);
        if(bookID != -1) {
            for (int i = 0; i < values.size(); i++) {
                ContentValues validationValue = new ContentValues();
                validationValue.put(validationFieldName, values.get(i));
                long ID = db.insertWithOnConflict(validationTable, "null", validationValue, SQLiteDatabase.CONFLICT_IGNORE);

                targetValue.put(bookIdFieldName, bookID);
                targetValue.put(targetFieldName, ID);
                db.insertWithOnConflict(targetTable, "null", targetValue, SQLiteDatabase.CONFLICT_IGNORE);
            }
        }
    }

    public static void close(){
        db.close();
    }
}
