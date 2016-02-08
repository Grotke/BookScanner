package com.josephcmontgomery.bookscanner;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.josephcmontgomery.bookscanner.Database.Database;
import com.josephcmontgomery.bookscanner.Tools.BookCache;
import com.josephcmontgomery.bookscanner.Tools.BookInformation;
import com.josephcmontgomery.bookscanner.Tools.ViewMode;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BookViewerActivity extends AppCompatActivity implements BookListFragment.OnBookListListener {
    private int currentMode;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_viewer);
        if (savedInstanceState != null && savedInstanceState.containsKey("currentState")) {
            currentMode = savedInstanceState.getInt("currentState");
        } else {
            currentMode = getIntent().getIntExtra("options", ViewMode.LIST_MODE);
        }
        if(deviceUsesDualPane()){
            addMode(ViewMode.DUAL_MODE);
            if(currentModeIs(ViewMode.LIST_MODE)){
                removeMode(ViewMode.LIST_MODE);
                addMode(ViewMode.DETAIL_MODE);
            }
        }
        else{
            removeMode(ViewMode.DUAL_MODE);
        }
        setUpToolbar();
        buildViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentState", currentMode);
        super.onSaveInstanceState(outState);
    }

    private void buildViews() {
        if (currentModeIs(ViewMode.LIST_MODE) || currentModeIs(ViewMode.DUAL_MODE)) {
            attachBookListFragment();
        }
        if(!currentModeIs(ViewMode.LIST_MODE)){
            setUpViewPager(0);
        }
    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);
        setToolbarTitle();
    }


    private void setToolbarTitle(){
        ActionBar bar = getSupportActionBar();
        if(currentModeIs(ViewMode.ADD_MODE)){
            bar.setTitle("Add Books");
        }
        else if (currentModeIs(ViewMode.EDIT_MODE)){
            bar.setTitle("Edit");
        }
        else{
            bar.setTitle("My Books");
        }
    }

    private boolean currentModeIs(int modeToCheck) {
        return (currentMode & modeToCheck) != 0;
    }

    private void setMode(int mode) {
        currentMode = mode;
    }

    private void attachBookListFragment() {
        findViewById(R.id.book_list_container).setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        BookListFragment bookFrag = new BookListFragment();
        bookFrag.setArguments(getIntent().getExtras());
        ft.add(R.id.book_list_container, bookFrag);
        ft.commit();
    }

    private void detachBookListFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.book_list_container);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        findViewById(R.id.book_list_container).setVisibility(View.GONE);
    }

    private void setUpViewPager(int position) {
        BookPagerAdapter adapter = new BookPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setVisibility(View.VISIBLE);
        if (!currentModeIs(ViewMode.DETAIL_MODE)) {
            adapter.setEditable(true);
        }
        if (getIntent().getSerializableExtra("books") == null) {
            adapter.setBooks(getApplicationContext());
        } else {
            adapter.setBooks((ArrayList<BookInformation>) getIntent().getSerializableExtra("books"));
        }
        pager.setCurrentItem(position);
        setUpPagerListener();
    }

    private void setUpPagerListener() {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                BookPagerAdapter adapter = (BookPagerAdapter) pager.getAdapter();
                if (adapter != null) {
                    if (adapter.isEditable()) {
                        BookEditFragment bookFrag = (BookEditFragment) adapter.instantiateItem(pager, pager.getCurrentItem());
                        if(bookFrag != null) {
                            View view = bookFrag.getView();
                            if(view != null) {
                                EditText title = (EditText) view.findViewById(R.id.bookedit_book_title);
                                if (title != null) {
                                    EditText currentEdit;
                                    if (title.getText().toString().trim().isEmpty()) {
                                        currentEdit = title;
                                        title.requestFocus();
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.showSoftInput(currentEdit, 0);
                                    } else {
                                        title.clearFocus();
                                        currentEdit = (EditText) view.findViewById(R.id.bookedit_location_edit);
                                        currentEdit.requestFocus();
                                        if(currentEdit.getText().toString().trim().isEmpty()) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.showSoftInput(currentEdit, 0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                BookPagerAdapter adapter = (BookPagerAdapter) pager.getAdapter();
                if (adapter != null) {
                    if (adapter.isEditable()) {
                        BookEditFragment bookFrag = (BookEditFragment) adapter.instantiateItem(pager, pager.getCurrentItem());
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
    }

    public void onBookSelected(int position) {
        if(currentModeIs(ViewMode.LIST_MODE)) {
            detachBookListFragment();
            setMode(ViewMode.DETAIL_MODE);
            setUpViewPager(position);
            invalidateOptionsMenu();
        }
        else{
            pager.setCurrentItem(position);
        }
    }

    private void onSave() {
        BookPagerAdapter adapter = (BookPagerAdapter) pager.getAdapter();
        if (adapter != null) {
            if (adapter.isEditable()) {
                BookEditFragment bookFrag = (BookEditFragment) adapter.instantiateItem(pager, pager.getCurrentItem());
                if (bookFrag != null) {
                    bookFrag.setBookFromUI();
                }
            }
        }
        ArrayList<BookInformation> books = BookCache.getBooks();
        for (BookInformation book : books) {
            if (!book.title.isEmpty()) {
                Date date = new Date();
                book.timeLastUpdated = DateFormat.getDateInstance().format(date);
                Database.insertBook(book, getApplicationContext());
            }
        }
    }

    private void onDelete() {
        BookPagerAdapter adapter = (BookPagerAdapter) pager.getAdapter();
        adapter.removeBook(pager.getCurrentItem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentModeIs(ViewMode.DETAIL_MODE)) {
            getMenuInflater().inflate(R.menu.menu_detail, menu);
        } else if (currentModeIs(ViewMode.EDIT_MODE) || currentModeIs(ViewMode.ADD_MODE)) {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.save_button) {
            onSave();
            if (currentModeIs(ViewMode.ADD_MODE)) {
                finish();
            }
            if(!currentModeIs(ViewMode.DUAL_MODE)) {
                setMode(ViewMode.LIST_MODE);
                pager.setVisibility(View.GONE);
                attachBookListFragment();
                invalidateOptionsMenu();
            }
            else{
                removeMode(ViewMode.EDIT_MODE);
                addMode(ViewMode.DETAIL_MODE);
                detachBookListFragment();
                attachBookListFragment();
                setUpViewPager(pager.getCurrentItem());
                invalidateOptionsMenu();
            }
            setToolbarTitle();
        }
        if (id == R.id.delete_button) {
            int nextPosition = getPositionAfterDelete();
            onDelete();
            setUpViewPager(nextPosition);
            if(currentModeIs(ViewMode.DUAL_MODE)){
                detachBookListFragment();
                attachBookListFragment();
            }
        }
        if (id == R.id.edit_button) {
            if(currentModeIs(ViewMode.DUAL_MODE)){
                removeMode(ViewMode.DETAIL_MODE);
                addMode(ViewMode.EDIT_MODE);
            }
            else {
                setMode(ViewMode.EDIT_MODE);
            }
            setUpViewPager(pager.getCurrentItem());
            invalidateOptionsMenu();
            setToolbarTitle();
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private int getPositionAfterDelete() {
        if (pager.getCurrentItem() == pager.getChildCount() - 1) {
            return pager.getCurrentItem() - 1;
        }
        return pager.getCurrentItem();
    }

    @Override
    public void onBackPressed() {
        BookCache.clearBooks();
        if (currentModeIs(ViewMode.LIST_MODE) || currentModeIs(ViewMode.ADD_MODE) || (currentModeIs(ViewMode.DUAL_MODE) && currentModeIs(ViewMode.DETAIL_MODE))) {
            finish();
        } else if (currentModeIs(ViewMode.DUAL_MODE) && currentModeIs(ViewMode.EDIT_MODE)){
            setMode(ViewMode.DUAL_MODE | ViewMode.DETAIL_MODE);
            setUpViewPager(pager.getCurrentItem());
            invalidateOptionsMenu();
        }
        else {
            setMode(ViewMode.LIST_MODE);
            pager.setVisibility(View.GONE);
            attachBookListFragment();
            invalidateOptionsMenu();
        }
        setToolbarTitle();
    }

    private boolean deviceUsesDualPane(){
        Resources res = getResources();
        return res.getBoolean(R.bool.dual_pane) && res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void removeMode(int modeToRemove){
        currentMode ^= modeToRemove;
    }

    private void addMode(int modeToAdd){
        currentMode |= modeToAdd;
    }
}