package com.josephcmontgomery.bookscanner;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class BookEditActivity extends AppCompatActivity implements BookEditFragment.OnBookEditListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);
        setUpToolbar();
        attachFragment();
    }

    private void attachFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        BookEditFragment bookFrag = new BookEditFragment();
        bookFrag.setArguments(getIntent().getExtras());
        ft.add(R.id.edit_container, bookFrag);
        ft.commit();
    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

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
}
