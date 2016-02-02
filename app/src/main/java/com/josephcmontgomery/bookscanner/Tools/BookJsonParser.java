package com.josephcmontgomery.bookscanner.Tools;

import android.util.JsonReader;

public class BookJsonParser {
    private BookJsonParser(){}

    public static BookInformation processSearchResult(JsonReader reader, String isbn) throws Exception{
        BookInformation book = new BookInformation();
        book.isbn = isbn;

        startReadingResult(reader, book);
        return book;
    }

    private static void startReadingResult(JsonReader reader, BookInformation book) throws Exception{
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("items")) {
                processItemArray(reader, book);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    private static void processItemArray(JsonReader reader, BookInformation book) throws Exception{
            //Only read first item.
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
            switch (name) {
                case "title":
                    book.title = reader.nextString();
                    break;
                case "subtitle":
                    book.title +=  ": " +reader.nextString();
                    break;
                case "description":
                    book.description = reader.nextString();
                    break;
                case "pageCount":
                    book.pageCount = reader.nextInt();
                    break;
                case "averageRating":
                    book.averageRating = reader.nextDouble();
                    break;
                case "ratingsCount":
                    book.ratingsCount = reader.nextInt();
                    break;
                case "imageLinks":
                    processImageLinks(reader, book);
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
    }

    private static void processImageLinks(JsonReader reader, BookInformation book) throws Exception {
        reader.beginObject();
        String thumbnail = "";
        while (reader.hasNext()) {
            String imageName = reader.nextName();
            //Get first image, replace with thumbnail if available.
            if (thumbnail.equals("")) {
                thumbnail = reader.nextString();
            } else if (imageName.equals("thumbnail")) {
                thumbnail = reader.nextString();
            }
        }
        book.imageURL = thumbnail;
        reader.endObject();
    }
}
