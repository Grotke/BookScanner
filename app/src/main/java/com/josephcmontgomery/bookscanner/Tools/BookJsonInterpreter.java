package com.josephcmontgomery.bookscanner.Tools;

import android.util.JsonReader;
import android.util.Log;

import com.josephcmontgomery.bookscanner.BookInformation;

/**
 * Created by Joseph on 12/11/2015.
 */
public class BookJsonInterpreter {
    public static boolean processSearchResult(JsonReader reader, BookInformation book) throws Exception{
        int numberOfItems;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("totalItems")) {
                numberOfItems = reader.nextInt();
                if (numberOfItems < 1) {
                    Log.e("NOT FOUND", "That isbn wasn't found.");
                    return false;
                }
            }
            else if (name.equals("items")) {
                processItemArray(reader, book);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return true;
    }

    private static void processItemArray(JsonReader reader, BookInformation book) throws Exception{
            reader.beginArray();
            processItem(reader, book);
            reader.endArray();
    }

    private static void processItem(JsonReader reader, BookInformation book) throws Exception{
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("volumeInfo")) {
                processVolumeInfo(reader, book);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    private static void processVolumeInfo(JsonReader reader, BookInformation book)throws Exception{
        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("title")) {
                book.title = reader.nextString();
            } else if (name.equals("subtitle")) {
                book.subtitle = reader.nextString();
            } else if (name.equals("description")) {
                book.description = reader.nextString();
            } else if (name.equals("pageCount")) {
                book.pageCount = reader.nextInt();
            } else if (name.equals("averageRating")) {
                book.averageRating = reader.nextDouble();
            } else if (name.equals("ratingsCount")) {
                book.ratingsCount = reader.nextInt();
            } else if (name.equals("imageLinks")){
                reader.beginObject();
                    String thumbnail = "";
                    boolean seenFirst = false;
                    while (reader.hasNext()) {
                        String imageName = reader.nextName();
                        if(seenFirst == false){
                            seenFirst = true;
                            thumbnail = reader.nextString();
                        } else if(imageName.equals("thumbnail")) {
                            thumbnail = reader.nextString();
                        }
                    }
                    book.imageURL = thumbnail;
                reader.endObject();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }
}
