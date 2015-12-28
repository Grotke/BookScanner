package com.josephcmontgomery.bookscanner;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

/**
 * Created by Joseph on 12/27/2015.
 */
public class DataCursorAdapter extends CursorAdapter {
    public DataCursorAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = (TextView) view.findViewById(R.id.book_title_entry);
        TextView location = (TextView) view.findViewById(R.id.book_location_entry);
        final ImageView icon = (ImageView) view.findViewById(R.id.book_cover_image);
        String strTitle = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_TITLE));
        String strLocation = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_SUBTITLE));
        String subTitle = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_SUBTITLE));
        final String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_IMAGE_URL));
        String finalStr = strTitle;
        if(!subTitle.equals("")){
            Log.e("SUBTITLE", subTitle);
            finalStr = strTitle + "\n" + subTitle;
        }
        else{
            Log.e("BLANK", "This is blank");
        }
        title.setText(finalStr);
        location.setText(strLocation);

        new AsyncTask<Void, Void, Drawable>() {
            Drawable thumb_d;
            @Override
            protected Drawable doInBackground(Void... params) {
                try {
                    URL thumb_u = new URL(imageUrl);
                    thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
                } catch (Exception e) {
                    // log error
                }
                return thumb_d;
            }

            @Override
            protected void onPostExecute(Drawable result) {
                if (thumb_d != null)
                    icon.setImageDrawable(thumb_d);
            }

        }.execute();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


}
