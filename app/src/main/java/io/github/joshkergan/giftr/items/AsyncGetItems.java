package io.github.joshkergan.giftr.items;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by balaji on 11/20/2016.
 */

public class AsyncGetItems extends AsyncTask<String, Void, String>{

    static String url = "http://10.0.2.2:8080/search/";
    // Takes in a string search parameter
    // returns array of objects
    protected String doInBackground(String... strings) {
        // it shouldn't be called with more than one thing as far as our code is concerned,
        // but I'm not sure how to make it only one input string
        String modifiedUrlStr = url + strings[0];
        try {
            URL urlObj = new URL(modifiedUrlStr);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlObj.openStream()));
            String curLine;
            while ((curLine = reader.readLine()) != null) {
                System.out.println(curLine);
            }
            return "It worked!!!";
        } catch (Exception ex) {
            System.out.println("error?");
            // just to make the errors go away :(
            // we should maybe deal with these... hehehe
        }
        return "It didn't work";
    }

    @Override
    protected void onPostExecute(String s) {
        // we use this to actually see the result
        // we actually need to figure out how this will be called to use this so
        // its empty for now
    }
}
