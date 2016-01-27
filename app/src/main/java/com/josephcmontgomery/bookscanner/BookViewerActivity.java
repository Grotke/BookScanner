package com.josephcmontgomery.bookscanner;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.josephcmontgomery.bookscanner.Tools.BookInformation;

import java.util.ArrayList;

public class BookViewerActivity extends AppCompatActivity implements BookEditFragment.OnBookEditListener, BookListFragment.OnBookListListener {
    private final int BOOK_SWIPE_EDIT_REQUEST = 2;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        attachBookListFragment();
        if(getResources().getBoolean(R.bool.dual_pane) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){//&& getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setUpViewPager();
        }
    }

    private void attachBookListFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        BookListFragment bookFrag = new BookListFragment();
        bookFrag.setArguments(getIntent().getExtras());
        ft.add(R.id.book_list_container, bookFrag);
        ft.commit();
    }

    private void setUpViewPager(){
        BookEditPagerAdapter adapter = new BookEditPagerAdapter(getSupportFragmentManager());
        adapter.setEditable(true);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setVisibility(View.VISIBLE);
        if(getIntent().getSerializableExtra("books") == null){
            adapter.setBooks(getApplicationContext());
        }
        else{
            adapter.setBooks((ArrayList<BookInformation>) getIntent().getSerializableExtra("books"));
        }
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

    public void onBookSelected(int position){
        if(!(getResources().getBoolean(R.bool.dual_pane) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)){
            findViewById(R.id.book_list_container).setVisibility(View.GONE);
            setUpViewPager();
            pager.setCurrentItem(position);
        }
        else{
            pager.setCurrentItem(position);
            /*Intent bookEditIntent = new Intent(BookViewerActivity.this, BookEditSwipeActivity.class);
            bookEditIntent.putExtra("position", position);
            startActivityForResult(bookEditIntent, BOOK_SWIPE_EDIT_REQUEST);*/
        }
    }

}
