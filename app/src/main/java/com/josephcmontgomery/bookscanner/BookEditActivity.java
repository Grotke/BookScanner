package com.josephcmontgomery.bookscanner;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.josephcmontgomery.bookscanner.Database.Database;
import com.josephcmontgomery.bookscanner.Tools.BookInformation;
import com.josephcmontgomery.bookscanner.Tools.ImageFetcher;

import java.text.DateFormat;
import java.util.Date;

public class BookEditActivity extends AppCompatActivity implements BookEditFragment.OnBookEditListener {
    private final int RATING_BAR_COLOR = 0xffffc60b;

    private void onReturn(int resultCode){
        setResult(resultCode);
        finish();
    }

    public void onSave(){
        onReturn(RESULT_OK);
    }

    public void onCancel(){
        onReturn(RESULT_CANCELED);
    }

    public void onDelete(){
        onReturn(RESULT_FIRST_USER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);
        setUpToolbar();
        Log.e("EDIT", "Got here");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        BookEditFragment bookFrag = new BookEditFragment();
        bookFrag.setArguments(getIntent().getExtras());
        ft.add(R.id.edit_container, bookFrag);
        ft.commit();
        Log.e("EDIT", "PAST HERE");

        /*BookInformation book = (BookInformation)getIntent().getSerializableExtra("bookInfo");
        boolean editable = false;
        if(book.title.isEmpty()){
            editable = true;
        }
        if(getIntent().getExtras().getBoolean("deleteEnabled")){
            setUpDeleteButton(book);
            editable = true;
        }
        setBookUpdateTime(book);

        setUIFields(book, editable);

        setUpSaveButton(book);
        setUpCancelButton();*/
    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setBookUpdateTime(BookInformation book){
        Date date = new Date();
        book.timeLastUpdated = DateFormat.getDateInstance().format(date);
    }

    private void setUIFields(BookInformation book, boolean editable){
        //Image first to give more time to possibly retrieve online.
        setImage(book);
        setTitleEdit(book, editable);
        setLocationEdit(book);
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

    private void setLocationEdit(BookInformation book){
        //XML bugged, must set InputType in code.
        EditText locationEdit = (EditText) findViewById(R.id.bookedit_location_edit);
        locationEdit.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        locationEdit.setText(book.location);
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
                if (v.getId() == R.id.bookedit_save_button) {
                    EditText location = (EditText) findViewById(R.id.bookedit_location_edit);
                    EditText title = (EditText) findViewById(R.id.bookedit_book_title);
                    String titleStr = title.getText().toString().trim();
                    if (titleStr.isEmpty()) {
                        setResult(RESULT_CANCELED);
                    } else {
                        book.title = titleStr;
                        book.location = location.getText().toString().trim();
                        Database.insertBook(book, getApplicationContext());
                        setResult(RESULT_OK);
                    }
                    finish();
                }
            }
        });
    }

    private void setUpCancelButton(){
        Button cancelBtn = (Button) findViewById(R.id.bookedit_cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.bookedit_cancel_button){
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
    }

    private void setUpDeleteButton(final BookInformation book){
        Button deleteBtn = (Button) findViewById(R.id.bookedit_delete_button);
        deleteBtn.setVisibility(View.VISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.bookedit_delete_button) {
                    Database.deleteBookById(book.bookId, getApplicationContext());
                    setResult(RESULT_FIRST_USER);
                    finish();
                }
            }
        });
    }
}
