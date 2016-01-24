package com.josephcmontgomery.bookscanner.Tools;

import java.io.Serializable;

public class BookInformation implements Serializable{
    public String title;
    public String isbn;
    public int pageCount;
    public double averageRating;
    public int ratingsCount;
    public String description;
    public String location;
    public String imageURL;
    public String timeLastUpdated;

    public BookInformation(){
        title = "";
        isbn = "";
        pageCount = 0;
        averageRating = 0;
        ratingsCount = 0;
        description = "";
        location = "";
        imageURL = "";
        timeLastUpdated = "";
    }

    @Override
    public String toString(){
        return "ISBN: " + isbn +
                "\nTITLE: " + title +
                "\nDESCRIPTION: " + description +
                "\nPAGE COUNT: "+ pageCount+
                "\nAVERAGE RATING: " + averageRating +
                "\nRATINGS COUNT: " + ratingsCount +
                "\nLOCATION: " + location +
                "\nTIME LAST UPDATED: " + timeLastUpdated;
    }
}
