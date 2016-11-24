package io.github.joshkergan.giftr;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.joshkergan.giftr.db.GiftrDbHelper;
import io.github.joshkergan.giftr.items.AddItemActivity;
import io.github.joshkergan.giftr.people.PeopleContract;
import io.github.joshkergan.giftr.people.PersonAdapter;

/**
 * Created by Josh on 20/11/2016.
 * Activity to present the interests of a single person
 */

public final class PersonActivity extends GiftrActivity{

    private long personId;
    private RecyclerView itemsView;
    private GiftrDbHelper pDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent infoIntent = getIntent();
        // -1 means we got here from a back most likely
        personId = infoIntent.getLongExtra("PersonID", -1);
        Log.d("PersonActivity", "onCreate: PersonID:" + personId);

        pDbHelper = GiftrDbHelper.getDbInstance(this);
        attachView(R.layout.person_activity, this);

        itemsView = (RecyclerView) findViewById(R.id.person_interests);
        TextView nameView = (TextView) findViewById(R.id.person_name);
        CircleImageView imageView = (CircleImageView) findViewById(R.id.person_image);
        // We deal with a person not being passed in by loading the last person that got saved
        if (personId == -1 && savedInstanceState != null){
            personId = savedInstanceState.getLong("PersonID");
        }

        Cursor personInfo = pDbHelper.getPersonInfo(pDbHelper.getReadableDatabase(), personId);

        personInfo.moveToFirst();
        String name = personInfo.getString(personInfo.getColumnIndex(PeopleContract.PeopleEntry.COLUMN_NAME_PERSON));
        byte[] image = personInfo.getBlob(personInfo.getColumnIndex(PeopleContract.PeopleEntry.COLUMN_NAME_PHOTO));

        nameView.setText(name);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent addItemIntent = new Intent(getApplicationContext(), AddItemActivity.class);
                addItemIntent.putExtra("PersonID", personId);
                startActivity(addItemIntent);
            }
        });

        itemsView.setAdapter(new PersonAdapter(pDbHelper.getReadableDatabase(), personId, new PersonAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(int position) {
                Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong("PersonID", personId);
        super.onSaveInstanceState(outState);
    }
}
