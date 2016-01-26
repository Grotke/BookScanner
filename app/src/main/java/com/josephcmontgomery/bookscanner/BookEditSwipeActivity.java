package com.josephcmontgomery.bookscanner;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.josephcmontgomery.bookscanner.Tools.BookInformation;

import java.util.ArrayList;

public class BookEditSwipeActivity extends AppCompatActivity implements BookEditFragment.OnBookEditListener{
    private BookEditPagerAdapter adapter;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit_swipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpViewPager();
    }

    private void setUpViewPager(){
        adapter = new BookEditPagerAdapter(getSupportFragmentManager());
        adapter.setEditable(true);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        if(getIntent().getSerializableExtra("books") == null){
            adapter.setBooks(getApplicationContext());
        }
        else{
            adapter.setBooks((ArrayList<BookInformation>) getIntent().getSerializableExtra("books"));
        }
        pager.setCurrentItem(getIntent().getIntExtra("position", 0));
    }

    public void onSave(){
        setResult(RESULT_OK);
        finish();
    }

    public void onCancel(){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onDelete(){
        setResult(RESULT_FIRST_USER);
        finish();
    }
}
