package com.josephcmontgomery.bookscanner;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.josephcmontgomery.bookscanner.Database.Database;
import com.josephcmontgomery.bookscanner.Tools.BookCache;
import com.josephcmontgomery.bookscanner.Tools.BookInformation;
import com.josephcmontgomery.bookscanner.Tools.ViewMode;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BookViewerActivity extends AppCompatActivity implements BookEditFragment.OnBookEditListener, BookListFragment.OnBookListListener {
    private int currentMode;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currentMode = getIntent().getIntExtra("options", ViewMode.LIST_MODE | ViewMode.DETAIL_MODE);
        buildView();
    }

    private void buildView(){
        setUpButtons();
        boolean useList = (currentMode & ViewMode.LIST_MODE) != 0;
        if(useList) {
            attachBookListFragment();
        }
        if(deviceUsesDualPane() || !useList){
            setUpViewPager();
        }
    }

    private boolean deviceUsesDualPane(){
        Resources res = getResources();
        return res.getBoolean(R.bool.dual_pane) && res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void attachBookListFragment(){
        findViewById(R.id.book_list_container).setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        BookListFragment bookFrag = new BookListFragment();
        bookFrag.setArguments(getIntent().getExtras());
        ft.add(R.id.book_list_container, bookFrag);
        ft.commit();
    }

    private void setUpViewPager(){
        BookPagerAdapter adapter = new BookPagerAdapter(getSupportFragmentManager());
        //adapter.setEditable(true);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setVisibility(View.VISIBLE);
        if((currentMode & ViewMode.EDIT_MODE) != 0){
            adapter.setEditable(true);
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                BookPagerAdapter adapter = (BookPagerAdapter) pager.getAdapter();
                if (adapter != null) {
                    Log.e("FRAG POS", String.valueOf(position));
                    if (adapter.isEditable()) {
                        BookEditFragment bookFrag = (BookEditFragment) adapter.instantiateItem(pager, position);

                        if (bookFrag != null) {
                            bookFrag.setBookFromUI();
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        if(getIntent().getSerializableExtra("books") == null){
            adapter.setBooks(Database.getAllBooks(getApplicationContext()));
        } else {
            adapter.setBooks((ArrayList<BookInformation>) getIntent().getSerializableExtra("books"));
        }
    }

    public void onSave(){
        ArrayList<BookInformation> books = BookCache.getBooks();
        for(BookInformation book: books){
            if (!book.title.isEmpty()) {
                Date date = new Date();
                book.timeLastUpdated = DateFormat.getDateInstance().format(date);
                Database.insertBook(book, getApplicationContext());
            }
        }
        BookCache.clearBooks();
        updateViews();
        if(!deviceUsesDualPane()) {
            pager.setVisibility(View.GONE);
            findViewById(R.id.book_list_container).setVisibility(View.VISIBLE);
        }
    }

    public void onCancel(){
        onBackPressed();
    }

    public void onDelete(BookInformation book){
        Database.deleteBookById(book.bookId, getApplicationContext());
        BookCache.removeBook(book);
        updateViews();
    }

    public void onBookSelected(int position){
        if(!deviceUsesDualPane()){
            findViewById(R.id.book_list_container).setVisibility(View.GONE);
            setUpViewPager();
            pager.setCurrentItem(position);
        }
        else{
            pager.setCurrentItem(position);
        }
    }

    @Override
    public void onBackPressed(){
        if(pager != null && pager.getVisibility() == View.VISIBLE && !deviceUsesDualPane()){
            BookCache.clearBooks();
            pager.setVisibility(View.GONE);
            findViewById(R.id.book_list_container).setVisibility(View.VISIBLE);
        }
        else{
            super.onBackPressed();
        }
    }

    private void updateViews(){
        BookListFragment listFrag = (BookListFragment) getSupportFragmentManager().findFragmentById(R.id.book_list_container);
        if(listFrag != null){
            listFrag.onResume();
        }
    }

    private void setUpButtons(){
        setUpSaveButton();
        setUpCancelButton();
        setUpDeleteButton();
    }

    private void setUpSaveButton(){
        Button saveBtn = (Button) findViewById(R.id.save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.save_button) {
                    onSave();
                }
            }
        });
    }

    private void setUpCancelButton(){
        Button cancelBtn = (Button) findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.cancel_button) {
                    onCancel();
                }
            }
        });
    }

    private void setUpDeleteButton(){
        Button deleteBtn = (Button) findViewById(R.id.delete_button);
        deleteBtn.setVisibility(View.VISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.delete_button) {
                    //onDelete(book);
                }
            }
        });
    }

}
