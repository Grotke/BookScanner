package com.josephcmontgomery.bookscanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.josephcmontgomery.bookscanner.Preferences.PreferencesActivity;
import com.josephcmontgomery.bookscanner.Tools.BookInformation;
import com.josephcmontgomery.bookscanner.Tools.ViewMode;

import java.util.ArrayList;

//TODO: Finalize name and get logo/promotional material.
public class MainActivity extends AppCompatActivity implements TaskFragment.TaskListener{
    private ProgressDialog dialog;
    ArrayList<BookInformation> books;
    private final int CONTINUE_SCANNING = 1;
    private final int BACK_TO_MAIN_MENU = 2;
    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private TaskFragment taskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new ProgressDialog(MainActivity.this);
        if (savedInstanceState != null && savedInstanceState.containsKey("currentBooks")) {
            books = (ArrayList<BookInformation>) savedInstanceState.getSerializable("currentBooks");
        } else {
            books = new ArrayList<>();
        }
        setUpToolbar();
        setUpMenuButtons();
        setUpTaskFragment();
    }

    private void setUpTaskFragment(){
        FragmentManager fm = getSupportFragmentManager();
        taskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (taskFragment == null) {
            taskFragment = new TaskFragment();
            fm.beginTransaction().add(taskFragment, TAG_TASK_FRAGMENT).commit();
        }
    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpMenuButtons(){
        setUpScanButton();
        setUpViewButton();
    }

    private void setUpScanButton(){
        Button scanBtn = (Button)findViewById(R.id.scan_button);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.scan_button) {
                    startScan(MainActivity.this);
                }
            }
        });
    }

    private void setUpViewButton(){
        Button viewBtn = (Button)findViewById(R.id.view_books_button);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.view_books_button) {
                    Intent bookViewerIntent = new Intent(MainActivity.this, BookViewerActivity.class);
                    startActivity(bookViewerIntent);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null){
            processScanResult(resultCode, scanningResult);
        }
        else if (requestCode == CONTINUE_SCANNING){
            startScan(MainActivity.this);
        }
        else if (requestCode == BACK_TO_MAIN_MENU){
            books.clear();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void processScanResult(int resultCode, IntentResult scanningResult){
        if(resultCode == RESULT_OK) {
            String isbn = scanningResult.getContents();
            taskFragment.launchBookTask(isbn);
        }
        else if(resultCode == RESULT_CANCELED && !books.isEmpty()){
            Intent bookViewerIntent = new Intent(MainActivity.this, BookViewerActivity.class);
            bookViewerIntent.putExtra("books", books);
            bookViewerIntent.putExtra("options", ViewMode.ADD_MODE);
            startActivityForResult(bookViewerIntent, BACK_TO_MAIN_MENU);
        }
    }

    private void startScan(Activity launchActivity){
        boolean beepEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_beep", true);
        IntentIntegrator scanIntegrator = new IntentIntegrator(launchActivity);
        scanIntegrator.addExtra("PROMPT_MESSAGE", "Scan a book's barcode to get data. BACK to finish.");
        scanIntegrator.setBeepEnabled(beepEnabled);
        scanIntegrator.initiateScan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onPreExecute() {
        dialog.setMessage("Fetching Book Info...");
        dialog.show();
    }

    @Override
    public void onPostExecute(BookInformation book) {
        if (dialog != null && dialog.isShowing()) {
          dialog.dismiss();
        }
        if(book.title.trim().isEmpty()){
            ArrayList<BookInformation> singleBook = new ArrayList<>();
            singleBook.add(book);
            Intent bookViewerIntent = new Intent(MainActivity.this, BookViewerActivity.class);
            bookViewerIntent.putExtra("options", ViewMode.ADD_MODE);
            bookViewerIntent.putExtra("books", singleBook);
            bookViewerIntent.putExtra("notFound", true);
            startActivityForResult(bookViewerIntent, CONTINUE_SCANNING);
        }
        else{
            books.add(book);
            onActivityResult(CONTINUE_SCANNING, RESULT_OK, null);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("currentBooks", books);
        super.onSaveInstanceState(outState);
    }
}
