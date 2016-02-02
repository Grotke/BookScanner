package com.josephcmontgomery.bookscanner.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookCache {
    private static HashMap<String, BookInformation> books = new HashMap<>();
    private static boolean booksHaveBeenRequested = false;

    private BookCache(){}

    public static void addBook(BookInformation book){
        if(booksHaveBeenRequested){
            books.clear();
            booksHaveBeenRequested = false;
        }
        books.put(book.isbn, book);
    }

    public static ArrayList<BookInformation> getBooks(){
        booksHaveBeenRequested = true;
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
}
