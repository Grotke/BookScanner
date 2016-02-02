package com.josephcmontgomery.bookscanner;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.josephcmontgomery.bookscanner.Tools.BookCache;
import com.josephcmontgomery.bookscanner.Tools.BookInformation;
import com.josephcmontgomery.bookscanner.Tools.ImageFetcher;

import java.text.DateFormat;
import java.util.Date;

public class BookEditFragment extends Fragment {
    private OnBookEditListener callback;
    private BookInformation book;
    private View view;

    public interface OnBookEditListener {
        void onSave();
        void onCancel();
        void onDelete(BookInformation book);
    }

    public BookEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (OnBookEditListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnBookEditListener");
        }
    }

    public static BookEditFragment newInstance(BookInformation book, boolean editable){
        BookEditFragment bookFrag = new BookEditFragment();
        Bundle args = new Bundle();
        args.putSerializable("bookInfo", book);
        args.putBoolean("deleteEnabled", editable);
        bookFrag.setArguments(args);
        return bookFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bookedit_fragment, container, false);
        Bundle bundle = getArguments();
        if(bundle == null){
            return view;
        }
        book = (BookInformation) bundle.getSerializable("bookInfo");
        BookCache.addBook(book);
        boolean editable = false;
        if(book.title.isEmpty()){
            editable = true;
        }
        if(bundle.getBoolean("deleteEnabled")){
            //setUpDeleteButton();
            editable = true;
        }
        //setBookUpdateTime();
        setUIFields(view, editable);

        //setUpSaveButton();
        //setUpCancelButton();
        return view;
    }

    private void setBookUpdateTime(){
        Date date = new Date();
        book.timeLastUpdated = DateFormat.getDateInstance().format(date);
    }

    private void setUIFields(View parentView, boolean editable){
        //Image first to give more time to possibly retrieve online.
        setImage(parentView);
        setTitleEdit(parentView, editable);
        setLocationEdit(parentView);
        setRatingBar(parentView);
        setDate(parentView);
        setISBN(parentView);
        setNumRatings(parentView);
    }

    private void setImage(View parentView){
        ImageView icon = (ImageView) parentView.findViewById(R.id.bookedit_book_image);
        ImageFetcher.loadImage(book.imageURL, icon);
    }

    private void setTitleEdit(View parentView, boolean editable){
        //XML bugged, must set InputType in code.
        EditText titleEdit = (EditText) parentView.findViewById(R.id.bookedit_book_title);
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

    private void setLocationEdit(View parentView){
        //XML bugged, must set InputType in code.
        EditText locationEdit = (EditText) parentView.findViewById(R.id.bookedit_location_edit);
        locationEdit.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        locationEdit.setText(book.location);
    }

    private void setRatingBar(View parentView){
        RatingBar ratingBar = (RatingBar) parentView.findViewById(R.id.bookedit_book_rating_bar);
        DrawableCompat.setTint(ratingBar.getProgressDrawable(), ContextCompat.getColor(getContext(), R.color.ratingBar));
        ratingBar.setRating((float) book.averageRating);
    }

    private void setDate(View parentView){
        TextView date = (TextView) parentView.findViewById(R.id.bookedit_scan_date);
        date.setText("Last Updated: " + book.timeLastUpdated);
    }
    private void setISBN(View parentView){
        TextView isbn = (TextView) parentView.findViewById(R.id.bookedit_isbn);
        isbn.setText("ISBN: " + book.isbn);
    }

    private void setNumRatings(View parentView){
        TextView numRatings = (TextView) parentView.findViewById(R.id.bookedit_book_rating);
        numRatings.setText(String.valueOf(book.averageRating) + "/5.0 (" + String.valueOf(book.ratingsCount) + " reviews)");
    }

   /* private void setUpSaveButton(){
        Button saveBtn = (Button) getActivity().findViewById(R.id.bookeditsave_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.bookedit_save_button) {
                    EditText location = (EditText) parentView.findViewById(R.id.bookedit_location_edit);
                    EditText title = (EditText) parentView.findViewById(R.id.bookedit_book_title);
                    String titleStr = title.getText().toString().trim();
                    if (titleStr.isEmpty()) {
                        callback.onCancel();
                    } else {
                        book.title = titleStr;
                        book.location = location.getText().toString().trim();
                        Database.insertBook(book, getContext());
                        callback.onSave();
                    }
                    setBookFromUI();
                    callback.onSave();
                }
            }
        });
    }

    private void setUpCancelButton(){
        Button cancelBtn = (Button) getActivity().findViewById(R.id.bookedit_cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.bookedit_cancel_button) {
                    callback.onCancel();
                }
            }
        });
    }

    private void setUpDeleteButton(){
        Button deleteBtn = (Button) getActivity().findViewById(R.id.bookedit_delete_button);
        deleteBtn.setVisibility(View.VISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.bookedit_delete_button) {
                    callback.onDelete(book);
                }
            }
        });
    } */

    public BookInformation getBook(){
        setBookFromUI();
        return book;
    }

    public void setBookFromUI() {
        if (view != null) {

            EditText location = (EditText) view.findViewById(R.id.bookedit_location_edit);
            EditText title = (EditText) view.findViewById(R.id.bookedit_book_title);
            String titleStr = title.getText().toString().trim();

            book.title = titleStr;
            book.location = location.getText().toString().trim();
            Log.e("TITLE", titleStr);
            Log.e("LOCATION", book.location);
        }
    }

    @Override
    public void onPause() {
        setBookFromUI();
        super.onPause();
    }
}
