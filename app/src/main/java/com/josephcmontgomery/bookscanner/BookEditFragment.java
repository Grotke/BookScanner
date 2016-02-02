package com.josephcmontgomery.bookscanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.InputType;
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

public class BookEditFragment extends Fragment {
    private BookInformation book;
    private View view;

    public BookEditFragment() {
        // Required empty public constructor
    }

    public static BookEditFragment newInstance(BookInformation book, boolean editable){
        BookEditFragment bookFrag = new BookEditFragment();
        Bundle args = new Bundle();
        args.putSerializable("bookInfo", book);
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
    }

    private void setLocationEdit(){
        //XML bugged, must set InputType in code.
        EditText locationEdit = (EditText) view.findViewById(R.id.bookedit_location_edit);
        locationEdit.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        locationEdit.setText(book.location);
        locationEdit.requestFocus();
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

    public BookInformation getBook(){
        setBookFromUI();
        return book;
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
