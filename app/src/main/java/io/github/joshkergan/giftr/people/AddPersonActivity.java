package io.github.joshkergan.giftr.people;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.joshkergan.giftr.GiftrActivity;
import io.github.joshkergan.giftr.R;

/**
 * Created by Josh on 23/11/2016.
 * Activity to add a new person to the database
 */

public final class AddPersonActivity extends GiftrActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View addPerson = attachView(R.layout.add_person);

        final Button personCreateButton = (Button) findViewById(R.id.person_create_button);
        personCreateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // save person into db
                TextView nameView = (TextView) addPerson.findViewById(R.id.add_person_name);
                CircleImageView personImageView = (CircleImageView) addPerson.findViewById(R.id.friend_image);
                pDbHelper.createPerson(
                        pDbHelper.getWritableDatabase(),
                        nameView.getText().toString(),
                        ((BitmapDrawable) personImageView.getDrawable()).getBitmap()
                );
                onBackPressed();
            }
        });
    }
}
