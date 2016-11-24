package io.github.joshkergan.giftr;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactImportActivity extends GiftrActivity
        implements AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private ListView contactList;
    private SimpleCursorAdapter cursorAdapter;
    private Button importButton;

    private Map<Long, String> selectedContacts = new HashMap<Long, String>();

    private final static String[] FROM_COLUMNS = {
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };

    private final static int[] TO_IDS = {
            R.id.contact_list_item
    };

    private final static String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };

    private final static int ID_INDEX = 0;
    private final static int NAME_INDEX = 1;
    private final static String SELECTION =
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";
    private String[] selectionArgs = { "%" };

    private final static int PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_import);

        contactList = (ListView) findViewById(R.id.contact_list);
        importButton = (Button) findViewById(R.id.contact_import_button);
        importButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                for(String contactName : selectedContacts.values()) {
                    pDbHelper.createPerson(
                            pDbHelper.getWritableDatabase(),
                            contactName,
                            BitmapFactory.decodeResource(getResources(), R.drawable.default_person)
                    );
                }
                onBackPressed();
            }
        });
        cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.contact_list_item,
                null,
                FROM_COLUMNS,
                TO_IDS,
                0
        );
        contactList.setAdapter(cursorAdapter);
        contactList.setOnItemClickListener(this);
        askPermission();
    }

    void askPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] { Manifest.permission.READ_CONTACTS },
                    PERMISSION_REQUEST
            );
        } else {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLoaderManager().initLoader(0, null, this);
            } else {
                onBackPressed();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cursorAdapter.getCursor().moveToPosition(position);
        if (selectedContacts.get(id) != null) {
            selectedContacts.remove(id);
        } else {
            selectedContacts.put(id, cursorAdapter.getCursor().getString(NAME_INDEX));
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                SELECTION,
                selectionArgs,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
        selectedContacts.clear();
    }
}
