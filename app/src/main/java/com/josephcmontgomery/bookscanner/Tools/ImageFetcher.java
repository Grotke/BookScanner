package com.josephcmontgomery.bookscanner.Tools;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by Joseph on 1/10/2016.
 */
public class ImageFetcher {
    private static HashMap<String, Drawable> cache = new HashMap<String, Drawable>();

    private static void insertDrawable(String imageUrl, Drawable image){
        cache.put(imageUrl, image);
    }

    private static Drawable getDrawable(String imageUrl){
        return cache.get(imageUrl);
    }

    public static void loadImage(final String imageUrl, final ImageView image){
        if(cache.containsKey(imageUrl)){
            image.setImageDrawable(getDrawable(imageUrl));
        }
        else {
            new AsyncTask<Void, Void, Drawable>() {
                Drawable thumb_d;

                @Override
                protected Drawable doInBackground(Void... params) {
                    try {
                        URL thumb_u = new URL(imageUrl);
                        thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
                    } catch (Exception e) {
                        // log error
                    }
                    return thumb_d;
                }

                @Override
                protected void onPostExecute(Drawable result) {
                    if (thumb_d != null) {
                        ImageFetcher.insertDrawable(imageUrl, thumb_d);
                        image.setImageDrawable(thumb_d);
                    }
                }

            }.execute();
        }
    }
}
