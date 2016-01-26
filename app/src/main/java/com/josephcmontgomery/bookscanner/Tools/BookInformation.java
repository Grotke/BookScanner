package com.josephcmontgomery.bookscanner.Tools;

import android.database.Cursor;

import com.josephcmontgomery.bookscanner.Database.BookScannerContract;

import java.io.Serializable;

public class BookInformation implements Serializable{
    public int bookId;
    public String title;
    public String isbn;
    public int pageCount;
    public double averageRating;
    public int ratingsCount;
    public String description;
    public String location;
    public String imageURL;
    public String timeLastUpdated;

    public BookInformation(){
        bookId = -1;
        title = "";
        isbn = "";
        pageCount = 0;
        averageRating = 0;
        ratingsCount = 0;
        description = "";
        location = "";
        imageURL = "";
        timeLastUpdated = "";
    }

    @Override
    public String toString(){
        return "ISBN: " + isbn +
                "\nTITLE: " + title +
                "\nDESCRIPTION: " + description +
                "\nPAGE COUNT: "+ pageCount+
                "\nAVERAGE RATING: " + averageRating +
                "\nRATINGS COUNT: " + ratingsCount +
                "\nLOCATION: " + location +
                "\nTIME LAST UPDATED: " + timeLastUpdated;
    }

    public static BookInformation packBookFromCursor(Cursor cursor){
        BookInformation book = new BookInformation();
        book.title = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_TITLE));
        book.timeLastUpdated = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_DATE_SCANNED));
        book.averageRating = cursor.getDouble(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_AVERAGE_RATING));
        book.bookId = cursor.getInt(cursor.getColumnIndexOrThrow(BookScannerContract.Books._ID));
        book.description = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_DESCRIPTION));
        book.isbn = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_ISBN));
        book.location = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_LOCATION));
        book.pageCount = cursor.getInt(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_PAGE_COUNT));
        book.ratingsCount = cursor.getInt(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_RATINGS_COUNT));
        book.imageURL = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_IMAGE_URL));
        return book;
    }
}
