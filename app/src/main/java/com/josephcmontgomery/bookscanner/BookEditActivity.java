package com.josephcmontgomery.bookscanner;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.net.URL;

public class BookEditActivity extends AppCompatActivity {
    private Button saveBtn, discardBtn;
    private BookInformation book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText edit = (EditText) findViewById(R.id.bookedit_book_title);
        edit.setInputType(InputType.TYPE_NULL);
        EditText location = (EditText) findViewById(R.id.bookedit_location_edit);
        location.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        RatingBar bar = (RatingBar) findViewById(R.id.bookedit_book_rating_bar);
        DrawableCompat.setTint(bar.getProgressDrawable(), 0xffffc60b);
        saveBtn = (Button) findViewById(R.id.bookedit_save_button);
        discardBtn = (Button) findViewById(R.id.bookedit_discard_button);
        book = (BookInformation)getIntent().getSerializableExtra("bookInfo");
        setBookFields();


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.bookedit_save_button){
                    EditText location = (EditText) findViewById(R.id.bookedit_location_edit);
                    book.location = location.getText().toString();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("bookInfo", book);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.bookedit_discard_button){
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void setBookFields(){
        new AsyncTask<Void, Void, Drawable>() {
            Drawable thumb_d;
            @Override
            protected Drawable doInBackground(Void... params) {
                try {
                    URL thumb_u = new URL(book.imageURL);
                    thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
                } catch (Exception e) {
                    // log error
                }
                return thumb_d;
            }

            @Override
            protected void onPostExecute(Drawable result) {
                if (thumb_d != null) {
                    ImageView icon = (ImageView) findViewById(R.id.bookedit_book_image);
                    icon.setImageDrawable(thumb_d);
                }
            }

        }.execute();
        EditText edit = (EditText) findViewById(R.id.bookedit_book_title);
        edit.setInputType(InputType.TYPE_NULL);
        String title = book.title;
        if(!book.subtitle.equals("")) {
            title += ": " + book.subtitle;
        }
        edit.setText(title);
        EditText location = (EditText) findViewById(R.id.bookedit_location_edit);
        location.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        RatingBar bar = (RatingBar) findViewById(R.id.bookedit_book_rating_bar);
        bar.setRating((float) book.averageRating);
        TextView isbn = (TextView) findViewById(R.id.bookedit_isbn);
        isbn.setText("ISBN: " + book.isbn);
        TextView numRatings = (TextView) findViewById(R.id.bookedit_book_rating);
        numRatings.setText(String.valueOf(book.averageRating) + "/5.0 (" + String.valueOf(book.ratingsCount) + " reviews)");
        DrawableCompat.setTint(bar.getProgressDrawable(), 0xffffc60b);
    }

}
