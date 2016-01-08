package com.josephcmontgomery.bookscanner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

public class BookEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*Button dontSaveBtn = new Button(getApplicationContext());
        Button saveBtn = new Button(getApplicationContext());
        toolbar.addView(saveBtn);
        toolbar.addView(dontSaveBtn);
        dontSaveBtn.setGravity(Gravity.RIGHT);
        //dontSaveBtn.setBackgroundColor(Color.RED);
        dontSaveBtn.setText("DISCARD");
        saveBtn.setGravity(Gravity.RIGHT);
        //saveBtn.setBackgroundColor(Color.GREEN);
        saveBtn.setText("SAVE");
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        saveBtn.setLayoutParams(params);
        dontSaveBtn.setLayoutParams(params);*/
        EditText edit = (EditText) findViewById(R.id.bookedit_book_title);
        edit.setInputType(InputType.TYPE_NULL);
        EditText location = (EditText) findViewById(R.id.bookedit_location_edit);
        location.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        RatingBar bar = (RatingBar) findViewById(R.id.bookedit_book_rating_bar);
        DrawableCompat.setTint(bar.getProgressDrawable(), 0xffffc60b);
        //bar.setIsIndicator(true);
        //bar.setStepSize(0.5f);
        //bar.setRating(3.5f);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
