package com.josephcmontgomery.bookscanner.Tools;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.net.URL;
import java.util.HashMap;

public class ImageFetcher {
    private static HashMap<String, Drawable> cache = new HashMap<>();
    private static final int MAX_CACHE_SIZE = 200;

    private ImageFetcher(){}

    public static void loadImage(final String imageUrl, final ImageView image){
        if(imageUrl.isEmpty()){
            return;
        }
        if(cache.size() >= MAX_CACHE_SIZE){
            cache.clear();
        }
        if(cache.containsKey(imageUrl)){
            image.setImageDrawable(getDrawable(imageUrl));
        }
        else {
            setImageFromUrl(imageUrl, image);
        }
    }

    private static void setImageFromUrl(final String imageUrl, final ImageView image){
        new AsyncTask<Void, Void, Void>() {
            Drawable drawThumb;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL urlThumb = new URL(imageUrl);
                    drawThumb = Drawable.createFromStream(urlThumb.openStream(), "src");
                } catch (Exception e) {
                    if(e.getMessage() != null) {
                        Log.d("EXCEPTION", e.getMessage());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (drawThumb != null) {
                    ImageFetcher.insertDrawable(imageUrl, drawThumb);
                    image.setImageDrawable(drawThumb);
                }
            }

        }.execute();
    }

    private static void insertDrawable(String imageUrl, Drawable image){
        cache.put(imageUrl, image);
    }

    private static Drawable getDrawable(String imageUrl){
        return cache.get(imageUrl);
    }
}
