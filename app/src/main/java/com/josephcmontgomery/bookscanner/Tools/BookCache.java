package com.josephcmontgomery.bookscanner.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joseph on 1/30/2016.
 */
public class BookCache {
    private static HashMap<String, BookInformation> books = new HashMap<>();

    private BookCache(){

    }

    public static void addBook(BookInformation book){
        books.put(book.isbn, book);
    }

    public static ArrayList<BookInformation> getBooks(){
        ArrayList<BookInformation> returnBooks = new ArrayList<>(books.size());
        for (Map.Entry<String, BookInformation> entry : books.entrySet())
        {
            returnBooks.add(entry.getValue());
        }
        return returnBooks;
    }

    public static void removeBook(BookInformation book){
        books.remove(book.isbn);
    }

    public static void clearBooks(){
        books.clear();
    }
}
