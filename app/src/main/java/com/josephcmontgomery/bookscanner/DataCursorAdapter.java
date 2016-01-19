package com.josephcmontgomery.bookscanner;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Joseph on 12/27/2015.
 */
public class DataCursorAdapter extends CursorAdapter {
    public DataCursorAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ImageView icon = (ImageView) view.findViewById(R.id.book_cover_image);
        String isbn = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_ISBN));
        final String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_IMAGE_URL));
        ImageFetcher.loadImage(isbn, imageUrl, icon);
        TextView title = (TextView) view.findViewById(R.id.book_title_entry);
        TextView location = (TextView) view.findViewById(R.id.book_location_entry);
        String strTitle = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_TITLE));
        String strLocation = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_LOCATION));
        String subTitle = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_SUBTITLE));
        String finalStr = strTitle;
        if(!subTitle.equals("")){
            finalStr = strTitle + "\n" + subTitle;
        }
        title.setText(finalStr);
        location.setText(strLocation);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


}
