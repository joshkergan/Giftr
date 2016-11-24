package io.github.joshkergan.giftr.items;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.joshkergan.giftr.GiftrActivity;
import io.github.joshkergan.giftr.PersonActivity;
import io.github.joshkergan.giftr.R;
import io.github.joshkergan.giftr.db.GiftrDbHelper;

/**
 * Created by balaji on 11/23/2016.
 */

public class IndividualItemActivity extends GiftrActivity {

    private long itemId;
    private GiftrDbHelper pDbHelper;
    private IndividualItemAdapter peopleAdapter;

    // credit goes to http://stackoverflow.com/questions/8992964/android-load-from-url-to-bitmap
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent infoIntent = getIntent();

        itemId = infoIntent.getLongExtra("ItemId", -1);

        pDbHelper = GiftrDbHelper.getDbInstance(this);
        final View activityView = attachView(R.layout.individual_item, this);

        TextView nameView = (TextView) findViewById(R.id.item_name);
        Button goToAmazon = (Button) findViewById(R.id.btn_go_to_amazon);
        final CircleImageView imageView = (CircleImageView) findViewById(R.id.item_image);
        final RecyclerView peopleView = (RecyclerView) findViewById(R.id.item_owners);


        // We deal with a person not being passed in by loading the last item that got saved
        if (itemId == -1 && savedInstanceState != null){
            itemId = savedInstanceState.getLong("ItemId");
        }
        final Cursor personInfo = pDbHelper.getItemInfo(pDbHelper.getReadableDatabase(), itemId);

        personInfo.moveToFirst();
        String name = personInfo.getString(personInfo.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_ITEM));
        final String amazonUrl = personInfo.getString(personInfo.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_AMAZON_URL));
        final String imageUrl = personInfo.getString(personInfo.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_PHOTO));

        nameView.setText(name);
        goToAmazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( amazonUrl ) );

                startActivity( browse );
            }
        });

        if (amazonUrl.equals("")) {
            goToAmazon.setVisibility(goToAmazon.GONE);
        }

        if (imageUrl != "") {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap image = getBitmapFromURL(imageUrl);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(image);
                        }
                    });

                }
            });

            thread.start();
        }

        peopleAdapter = new IndividualItemAdapter(
                pDbHelper.getReadableDatabase(),
                itemId,
                new IndividualItemAdapter.OnItemClickListener(){
                    @Override
                    public void onItemClick(int position) {
                        Intent personIntent = new Intent(getApplicationContext(), PersonActivity.class);
                        personIntent.putExtra("PersonID", peopleAdapter.getItemId(position));
                        startActivity(personIntent);
                    }
                }
        );
        peopleView.setAdapter(peopleAdapter);
    }

    @Override
    public void onBackPressed() {
        // Go back to the calling activity
        super.onBackPressed();
    }
}
