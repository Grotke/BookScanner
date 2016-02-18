package com.josephcmontgomery.bookscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.josephcmontgomery.bookscanner.Tools.BookInformation;
import com.josephcmontgomery.bookscanner.Tools.ImageFetcher;

import java.util.List;

public class BookListAdapter extends ArrayAdapter<BookInformation> {

    public BookListAdapter(Context context, int resource, List<BookInformation> books) {
        super(context, resource, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        BookInformation book = getItem(position);
        setImage(view, book);
        setLocation(view, book);
        setTitle(view, book);

        return view;
    }

    private void setImage(View view, BookInformation book){
        final ImageView icon = (ImageView) view.findViewById(R.id.book_cover_image);
        final String imageUrl = book.imageURL;
        ImageFetcher.loadImage(imageUrl, icon);
    }

    private void setLocation(View view, BookInformation book){
        TextView location = (TextView) view.findViewById(R.id.book_location_entry);
        String strLocation = book.location;
        location.setText(strLocation);
    }

    private void setTitle(View view, BookInformation book){
        TextView title = (TextView) view.findViewById(R.id.book_title_entry);
        String strTitle = book.title;
        title.setText(strTitle);
    }

}
