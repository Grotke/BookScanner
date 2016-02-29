package com.josephcmontgomery.bookscanner;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.josephcmontgomery.bookscanner.Database.Database;
import com.josephcmontgomery.bookscanner.Tools.BookCache;
import com.josephcmontgomery.bookscanner.Tools.BookInformation;

import java.util.ArrayList;

public class BookPagerAdapter extends FragmentStatePagerAdapter{
    private Context context;
    private Cursor cursor;
    private ArrayList<BookInformation> books;
    private boolean editable = false;

    public BookPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        BookInformation book = getBookFromDataSource(position);

        return createProperFragment(book);
    }

    private BookInformation getBookFromDataSource(int position){
        BookInformation book;
        if(cursor != null){
            cursor.moveToPosition(position);
            book = BookInformation.packBookFromCursor(cursor);
        }
        else if(!books.isEmpty()){
            book = books.get(position);
        }
        else{
            book = new BookInformation();
        }
        return book;
    }

    private Fragment createProperFragment(BookInformation book){
        if(editable) {
            return BookEditFragment.newInstance(book);
        }
        else{
            return BookDetailFragment.newInstance(book);
        }
    }

    @Override
    public int getCount() {
        if(cursor != null){
            return cursor.getCount();
        }
        if(books != null){
            return books.size();
        }
        return 0;
    }

    public void setBooks(ArrayList<BookInformation> books){
        this.books = books;
        notifyDataSetChanged();
    }

    public void setBooks(Context context){
        this.context = context;
        cursor = Database.getAllBooks(context);
        notifyDataSetChanged();
    }

    public void removeBook(int position){
        BookInformation book = getBookFromDataSource(position);
        if(book.bookId != -1){
            Database.deleteBookById(book.bookId, context);
            cursor = Database.getAllBooks(context);
            notifyDataSetChanged();
        }
        else{
            if(books != null) {
                books.remove(position);
                notifyDataSetChanged();
            }
        }
        BookCache.removeBook(book);
    }

    public void setEditable(boolean editable){
        this.editable = editable;
    }

    public boolean isEditable(){
        return editable;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position+1) + "/" + getCount();
    }
}
