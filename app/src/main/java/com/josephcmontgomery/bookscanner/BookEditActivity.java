package com.josephcmontgomery.bookscanner;

import android.content.Intent;
import android.os.Bundle;
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
        setButtonListeners();
    }

    private void setBookFields(){
        ImageView icon = (ImageView) findViewById(R.id.bookedit_book_image);
        ImageFetcher.loadImage(book.isbn, book.imageURL, icon);
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

    private void setButtonListeners(){
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
    }

}
