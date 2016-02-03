package com.josephcmontgomery.bookscanner;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.josephcmontgomery.bookscanner.Tools.BookCache;
import com.josephcmontgomery.bookscanner.Tools.BookInformation;
import com.josephcmontgomery.bookscanner.Tools.ImageFetcher;

public class BookEditFragment extends Fragment {
    private BookInformation book;
    private View view;

    public BookEditFragment() {
        // Required empty public constructor
    }

    public static BookEditFragment newInstance(BookInformation book){
        BookEditFragment bookFrag = new BookEditFragment();
        Bundle args = new Bundle();
        args.putSerializable("bookInfo", book);
        bookFrag.setArguments(args);
        return bookFrag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        setBookFromUI();
        outState.putSerializable("book", book);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bookedit_fragment, container, false);
        Bundle bundle = getArguments();
        if(bundle == null){
            return view;
        }
        if(savedInstanceState != null && savedInstanceState.containsKey("book")){
            book = (BookInformation) savedInstanceState.getSerializable("book");
        }
        else {
            book = (BookInformation) bundle.getSerializable("bookInfo");
        }
        BookCache.addBook(book);
        setUIFields();

        return view;
    }

    private void setUIFields(){
        //Image first to give more time to possibly retrieve online.
        setImage();
        setTitleEdit();
        setLocationEdit();
        setRatingBar();
        setDate();
        setISBN();
        setNumRatings();
    }

    private void setImage(){
        ImageView icon = (ImageView) view.findViewById(R.id.bookedit_book_image);
        ImageFetcher.loadImage(book.imageURL, icon);
    }

    private void setTitleEdit(){
        //XML bugged, must set InputType in code.
        EditText titleEdit = (EditText) view.findViewById(R.id.bookedit_book_title);
        titleEdit.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        titleEdit.setText(book.title);
        if(!book.title.isEmpty()){
            titleEdit.clearFocus();
        }
        setUpTitleListener(titleEdit);
    }

    private void setLocationEdit(){
        //XML bugged, must set InputType in code.
        EditText locationEdit = (EditText) view.findViewById(R.id.bookedit_location_edit);
        locationEdit.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        locationEdit.setText(book.location);
        locationEdit.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setUpLocationListener(locationEdit);
    }

    private void setUpLocationListener(EditText locationEdit){
        locationEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.bookedit_location_edit && !hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }

    private void setUpTitleListener(EditText titleEdit){
        titleEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.bookedit_book_title && !hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }

    private void setRatingBar(){
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.bookedit_book_rating_bar);
        DrawableCompat.setTint(ratingBar.getProgressDrawable(), ContextCompat.getColor(getContext(), R.color.ratingBar));
        ratingBar.setRating((float) book.averageRating);
    }

    private void setDate(){
        TextView date = (TextView) view.findViewById(R.id.bookedit_scan_date);
        date.setText("Last Updated: " + book.timeLastUpdated);
    }
    private void setISBN(){
        TextView isbn = (TextView) view.findViewById(R.id.bookedit_isbn);
        isbn.setText("ISBN: " + book.isbn);
    }

    private void setNumRatings(){
        TextView numRatings = (TextView) view.findViewById(R.id.bookedit_book_rating);
        numRatings.setText(String.valueOf(book.averageRating) + "/5.0 (" + String.valueOf(book.ratingsCount) + " reviews)");
    }

    public void setBookFromUI() {
        if (view != null) {
            EditText location = (EditText) view.findViewById(R.id.bookedit_location_edit);
            EditText title = (EditText) view.findViewById(R.id.bookedit_book_title);

            book.title = title.getText().toString().trim();
            book.location = location.getText().toString().trim();
        }
    }

    @Override
    public void onPause() {
        setBookFromUI();
        super.onPause();
    }
}
