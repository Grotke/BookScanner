package com.josephcmontgomery.bookscanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);
        if (savedInstanceState != null && savedInstanceState.containsKey("currentState")) {
            currentMode = savedInstanceState.getInt("currentState");
        } else {
            currentMode = getIntent().getIntExtra("options", ViewMode.LIST_MODE);
        }
        buildViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentState", currentMode);
        super.onSaveInstanceState(outState);
    }

    private void buildViews() {
        if (currentModeIs(ViewMode.LIST_MODE)) {
            attachBookListFragment();
        } else {
            setUpViewPager(0);
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
            public void onPageSelected(int state) {
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
        detachBookListFragment();
        setMode(ViewMode.DETAIL_MODE);
        setUpViewPager(position);
        invalidateOptionsMenu();
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
            setMode(ViewMode.LIST_MODE);
            pager.setVisibility(View.GONE);
            attachBookListFragment();
            invalidateOptionsMenu();
        }
        if (id == R.id.delete_button) {
            int nextPosition = getPositionAfterDelete();
            onDelete();
            setUpViewPager(nextPosition);
        }
        if (id == R.id.edit_button) {
            setMode(ViewMode.EDIT_MODE);
            setUpViewPager(pager.getCurrentItem());
            invalidateOptionsMenu();
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
        if (currentModeIs(ViewMode.LIST_MODE) || currentModeIs(ViewMode.ADD_MODE)) {
            finish();
        } else {
            setMode(ViewMode.LIST_MODE);
            pager.setVisibility(View.GONE);
            attachBookListFragment();
            invalidateOptionsMenu();
        }
    }
}
   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);
        currentMode = getIntent().getIntExtra("options", ViewMode.LIST_MODE | ViewMode.DETAIL_MODE);
        buildView();
    }

    private void buildView(){
        if(currentModeIs(ViewMode.LIST_MODE)) {
            attachBookListFragment();
        }
        if(deviceUsesDualPane() || currentModeIs(ViewMode.ADD_MODE)){
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
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setVisibility(View.VISIBLE);
        if(currentModeIs(ViewMode.EDIT_MODE) || currentModeIs(ViewMode.ADD_MODE)){
            adapter.setEditable(true);
        }

        if(getIntent().getSerializableExtra("books") == null){
            adapter.setBooks(Database.getAllBooks(getApplicationContext()));
        } else {
            adapter.setBooks((ArrayList<BookInformation>) getIntent().getSerializableExtra("books"));
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                BookPagerAdapter adapter = (BookPagerAdapter) pager.getAdapter();
                if (adapter != null) {
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
    }

    private void updateViewPager(){
        BookPagerAdapter adapter = (BookPagerAdapter) pager.getAdapter();
        if(currentModeIs(ViewMode.EDIT_MODE) || currentModeIs(ViewMode.ADD_MODE)){
            adapter.setEditable(true);
        }
        else{
            adapter.setEditable(false);
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
            //BookCache.clearBooks();
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

    private boolean currentModeIs(int modeToCheck){
        return (currentMode & modeToCheck) != 0;
    }

    private void removeMode(int modeToRemove){
        currentMode ^= modeToRemove;
    }

    private void addMode(int modeToRemove){
        currentMode |= modeToRemove;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(currentModeIs(ViewMode.DETAIL_MODE)){
            getMenuInflater().inflate(R.menu.menu_detail, menu);
        }
        else if(currentModeIs(ViewMode.EDIT_MODE)){
            getMenuInflater().inflate(R.menu.menu_edit, menu);
        }
        else {
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
        if(id == R.id.save_button){
            Log.e("SAVE PRESSED", "Pressed save button");
            removeMode(ViewMode.EDIT_MODE);
            addMode(ViewMode.DETAIL_MODE);
            setUpViewPager();
            invalidateOptionsMenu();
        }
        if(id == R.id.delete_button){
            Log.e("DELETE PRESSED", "Pressed delete button");
        }
        if(id == R.id.edit_button){
            Log.e("EDIT PRESSED", "Pressed edit button");
            removeMode(ViewMode.DETAIL_MODE);
            addMode(ViewMode.EDIT_MODE);
            setUpViewPager();
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }*/

