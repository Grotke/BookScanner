package com.josephcmontgomery.bookscanner;

import android.util.JsonReader;
import android.util.Log;

/**
 * Created by Joseph on 12/11/2015.
 */
public class BookJsonInterpreter {
    public static void processSearchResult(JsonReader reader, BookInformation book) throws Exception{
        int numberOfItems;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("totalItems")) {
                numberOfItems = reader.nextInt();
                if (numberOfItems < 1) {
                    Log.e("NOT FOUND", "That isbn wasn't found.");
                }
            }
            else if (name.equals("items")) {
                processItemArray(reader, book);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
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
                //Log.e("TITLE", reader.nextString());
                book.title = reader.nextString();
            } else if (name.equals("subtitle")) {
                //Log.e("SUBTITLE", reader.nextString());
                book.subtitle = reader.nextString();
            } else if (name.equals("authors")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    //Log.e("AUTHOR NAME", reader.nextString());
                    book.authors.add(reader.nextString());
                }
                reader.endArray();
            //} else if (name.equals("publishedDate")) {
                //Log.e("PUBLISH DATE", reader.nextString());
            } else if (name.equals("description")) {
                //Log.e("DESCRIPTION", reader.nextString());
                book.description = reader.nextString();
            } else if (name.equals("pageCount")) {
                //Log.e("PAGECOUNT", String.valueOf(reader.nextInt()));
                book.pageCount = reader.nextInt();
            } else if (name.equals("categories")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    //Log.e("CATEGORY", reader.nextString());
                    book.categories.add(reader.nextString());
                }
                reader.endArray();
            } else if (name.equals("averageRating")) {
                //Log.e("AVERAGE RATING", String.valueOf(reader.nextDouble()));
                book.averageRating = reader.nextDouble();
            } else if (name.equals("ratingsCount")) {
                //Log.e("RATINGSCOUNT", String.valueOf(reader.nextInt()));
                book.ratingsCount = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }
}
