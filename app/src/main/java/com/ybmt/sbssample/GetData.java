package com.ybmt.sbssample;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by YBMT on 5/24/2018.
 */

public class GetData extends AsyncTask<String, Void, String> {
    String server_response;
    MainActivity mCallingActivity;

    public GetData(MainActivity activity){
        super();
        mCallingActivity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) server_response = readStream(new BufferedInputStream(urlConnection.getInputStream()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("respnses", "s:"+server_response);
        try {
            JSONObject reader = new JSONObject(server_response);
            mCallingActivity.onDataReceived(reader);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }



}
