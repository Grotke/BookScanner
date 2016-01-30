package com.josephcmontgomery.bookscanner;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.josephcmontgomery.bookscanner.Tools.BookInformation;

import java.util.ArrayList;

public class BookEditPagerAdapter extends FragmentStatePagerAdapter{
    private Cursor cursor;
    private ArrayList<BookInformation> books;
    private boolean editable = false;

    public BookEditPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        BookInformation book;
        if(cursor != null){
            cursor.moveToPosition(position);
            book = BookInformation.packBookFromCursor(cursor);
        }
        else{
            book = books.get(position);
        }
        return BookEditFragment.newInstance(book, editable);
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

    public void setBooks(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public void setEditable(boolean editable){
        this.editable = editable;
    }

}
