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

import com.josephcmontgomery.bookscanner.Tools.BookInformation;
import com.josephcmontgomery.bookscanner.Tools.ImageFetcher;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookEditActivity extends AppCompatActivity {
    private final int RATING_BAR_COLOR = 0xffffc60b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);

        setUpToolbar();

        BookInformation book = (BookInformation)getIntent().getSerializableExtra("bookInfo");
        setBookUpdateTime(book);
        boolean editable = false;
        if(book.title.isEmpty()){
            editable = true;
        }

        setUIFields(book, editable);

        setUpSaveButton(book);
        setUpDiscardButton();
    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setBookUpdateTime(BookInformation book){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        book.timeLastUpdated = dateFormat.format(date);
    }

    private void setUIFields(BookInformation book, boolean editable){
        //Image first to give more time to possibly retrieve online.
        setImage(book);
        setTitleEdit(book, editable);
        setLocationEdit();
        setRatingBar(book);
        setDate(book);
        setISBN(book);
        setNumRatings(book);
    }

    private void setImage(BookInformation book){
        ImageView icon = (ImageView) findViewById(R.id.bookedit_book_image);
        ImageFetcher.loadImage(book.imageURL, icon);
    }

    private void setTitleEdit(BookInformation book, boolean editable){
        //XML bugged, must set InputType in code.
        EditText titleEdit = (EditText) findViewById(R.id.bookedit_book_title);
        int editType;
        if(!editable) {
            editType = InputType.TYPE_NULL;
        }
        else{
            editType = InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
        }
        titleEdit.setInputType(editType);
        titleEdit.setText(book.title);
    }

    private void setLocationEdit(){
        //XML bugged, must set InputType in code.
        EditText locationEdit = (EditText) findViewById(R.id.bookedit_location_edit);
        locationEdit.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
    }

    private void setRatingBar(BookInformation book){
        RatingBar ratingBar = (RatingBar) findViewById(R.id.bookedit_book_rating_bar);
        DrawableCompat.setTint(ratingBar.getProgressDrawable(), RATING_BAR_COLOR);
        ratingBar.setRating((float) book.averageRating);
    }

    private void setDate(BookInformation book){
        TextView date = (TextView) findViewById(R.id.bookedit_scan_date);
        date.setText("Last Updated: " + book.timeLastUpdated);
    }
    private void setISBN(BookInformation book){
        TextView isbn = (TextView) findViewById(R.id.bookedit_isbn);
        isbn.setText("ISBN: " + book.isbn);
    }

    private void setNumRatings(BookInformation book){
        TextView numRatings = (TextView) findViewById(R.id.bookedit_book_rating);
        numRatings.setText(String.valueOf(book.averageRating) + "/5.0 (" + String.valueOf(book.ratingsCount) + " reviews)");
    }

    private void setUpSaveButton(final BookInformation book){
        Button saveBtn = (Button) findViewById(R.id.bookedit_save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.bookedit_save_button){
                    EditText location = (EditText) findViewById(R.id.bookedit_location_edit);
                    EditText title = (EditText) findViewById(R.id.bookedit_book_title);
                    String titleStr = title.getText().toString();
                    if(titleStr.isEmpty()){
                        setResult(RESULT_CANCELED);
                    }
                    else {
                        book.title = titleStr;
                        book.location = location.getText().toString();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("bookInfo", book);
                        setResult(RESULT_OK, returnIntent);
                    }
                    finish();
                }
            }
        });
    }

    private void setUpDiscardButton(){
        Button discardBtn = (Button) findViewById(R.id.bookedit_discard_button);
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
