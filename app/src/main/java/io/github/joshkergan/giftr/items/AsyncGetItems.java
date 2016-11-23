package io.github.joshkergan.giftr.items;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import io.github.joshkergan.giftr.items.AmazonItem;

/**
 * Created by balaji on 11/20/2016.
 */


public class AsyncGetItems extends AsyncTask<String, Void, List<AmazonItem>>{

    public AutoCompleteTextView textView = null;
    Context context = null;
    static String url = "http://10.0.2.2:8080/search/";
    // Takes in a string search parameter
    // returns array of objects
    protected List<AmazonItem> doInBackground(String... strings) {
        // it shouldn't be called with more than one thing as far as our code is concerned,
        // but I'm not sure how to make it only one input string
        String modifiedUrlStr = url + strings[0];
        List<AmazonItem> itemList = new ArrayList<AmazonItem>();
        try {
            URL urlObj = new URL(modifiedUrlStr);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlObj.openStream()));
            String curLine;
            JSONArray arr = null;
            while ((curLine = reader.readLine()) != null) {
                // hopefully one line :)
                arr = new JSONArray(curLine);
            }

            for (int i = 0; i < arr.length(); i++) {
                JSONObject c = arr.getJSONObject(i);
                AmazonItem curItem = new AmazonItem(c.getString("name"), c.getString("url"), c.getString("imageurl"), c.getDouble("price"));
                itemList.add(curItem);
            }

            return itemList;
        } catch (Exception ex) {
            System.out.println("error?");
            return null;
            // just to make the errors go away :(
            // we should maybe deal with these... hehehe
        }
    }

    @Override
    protected void onPostExecute(List<AmazonItem> items) {
        // we use this to actually see the result
        // we actually need to figure out how this will be called to use this so
        // its empty for now

        ArrayAdapter<AmazonItem> adapter = new ArrayAdapter<AmazonItem>(context, android.R.layout.simple_list_item_1, items);
        textView.setAdapter(adapter);
    }
}
