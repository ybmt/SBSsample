package com.ybmt.sbssample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by YBMT on 6/26/2018.
 */

public class UserData {

    private String first_name;
    private String last_name;
    private String img_url;
    private Bitmap img_cache;
    private ImageView destination;

    //Constructor: from server
    public UserData(JSONObject data) {
        try{
            first_name = data.getString("first_name");
            last_name = data.getString("last_name");
            img_url = data.getString("avatar");
            new DownLoadImage().execute();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getFullName(){
        return String.format("%s %s",first_name,last_name).trim();
    }

    public void getImageFor(ImageView imageView){
        if (img_cache != null) imageView.setImageBitmap(img_cache);
        else destination = imageView;
    }


    private class DownLoadImage extends AsyncTask<Void,Void,Void> {

        protected Void doInBackground(Void... foo){
            try{
                InputStream is = new URL(img_url).openStream();
                img_cache = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void bar){
            if(destination!=null)destination.setImageBitmap(img_cache);
        }
    }
}
