package com.josephcmontgomery.bookscanner;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Joseph on 12/15/2015.
 */
public class BookInformation implements Serializable{
    public String title;
    public String subtitle;
    public ArrayList<String> authors;
    public String isbn;
    public int pageCount;
    public ArrayList<String> categories;
    public double averageRating;
    public int ratingsCount;
    public String description;
    public String location;
    public String imageURL;
    public String timeLastUpdated;

    public BookInformation(){
        title = "";
        subtitle = "";
        authors = new ArrayList<String>();
        isbn = "";
        pageCount = 0;
        categories = new ArrayList<String>();
        averageRating = 0;
        ratingsCount = 0;
        description = "";
        location = "";
        imageURL = "";
        timeLastUpdated = "";
    }

    public BookInformation(String title, String subtitle, ArrayList<String> authors, String isbn, int pageCount, ArrayList<String> categories, double averageRating, int ratingsCount, String description, String location, String timeLastUpdated){
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.isbn = isbn;
        this.pageCount = pageCount;
        this.categories = categories;
        this.averageRating = averageRating;
        this.ratingsCount = ratingsCount;
        this.description = description;
        this.location = location;
        this.timeLastUpdated = timeLastUpdated;
    }

    @Override
    public String toString(){
        return "ISBN: " + isbn + "\nTITLE: " + title + "\nSUBTITLE: " + subtitle + "\nAUTHORS: " + convertListToString(authors) + "\nDESCRIPTION: " + description + "\nPAGE COUNT: "+ pageCount+ "\nCATEGORIES: " + convertListToString(categories) + "\nAVERAGE RATING: " + averageRating + "\nRATINGS COUNT: " + ratingsCount + "\nLOCATION: " + location + "\nTIME LAST UPDATED: " + timeLastUpdated;
    }

    private String convertListToString(ArrayList<String> list){
        String listString = "";

        for (String s : list)
        {
            if(listString.equals("")) {
                listString += s;
            }
            else{
                listString += ", " + s;
            }
        }
        return listString;
    }


}
