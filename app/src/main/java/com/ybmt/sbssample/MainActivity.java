package com.ybmt.sbssample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


public class MainActivity extends Activity {

    private List<UserData> mUsersList = new ArrayList<>();;
    private RecyclerView mUsersRecyclerView;
    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsersRecyclerView = (RecyclerView)findViewById(R.id.users_recycler);
        mUsersRecyclerView.setHasFixedSize(false);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter = new UserAdapter(getApplicationContext(),mUsersList);
        mUsersRecyclerView.setAdapter(mUserAdapter);

        final GetData getData = new GetData(this);
        getData.execute(getString(R.string.url_users));
    }

    public void onDataReceived(JSONObject reader){
        try {
            JSONArray data  = reader.getJSONArray("data");
            for (int i=0; i<data.length(); i++){
                mUsersList.add(new UserData((JSONObject)data.get(i)));
            }
            mUserAdapter.notifyDataSetChanged();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    public void onAddUserClicked(View v){
        new UploadUser().execute(getString(R.string.url_users));
        mUserAdapter.notifyDataSetChanged();
    }


    private class UploadUser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection;
            String json = "";
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("first_name", "Doug");
                jsonObject.put("last_name", "Kenney");
                jsonObject.put("avatar", new URL("https://sites.google.com/a/fiu.edu/hangmath/home/logo_167.png?attredirects=0"));

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                JSONObject jsonUser = new JSONObject();
                jsonUser.put("data",jsonArray);

                json = jsonUser.toString();

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(json.getBytes("UTF-8"));
                out.flush();
                out.close();

                Log.d("respnseMessage",urlConnection.getResponseMessage()+"   code:"+urlConnection.getResponseCode());
                if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_CREATED){
                    json = "";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("respnse", "s:"+s);
            if (s == null || s.equals("")){
                //implement retry policy
                return;
            }
            try {
                JSONObject reader = new JSONObject(s);
                onDataReceived(reader);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

    }


}


