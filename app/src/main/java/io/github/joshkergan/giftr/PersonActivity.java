package io.github.joshkergan.giftr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

/**
 * Created by Josh on 20/11/2016.
 * Activity to present the interests of a single person
 */

public final class PersonActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener{

    private int personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent infoIntent = getIntent();
        // -1 means an error occured
        personId = infoIntent.getIntExtra("PersonID", -1);

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_items){
            // Handle navigating to items activity
            Intent itemsIntent = new Intent(this, ItemsActivityStub.class);
            startActivity(itemsIntent);
        }else if (id == R.id.nav_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }else if (id == R.id.nav_send){
            // WE CAN ADD SOMETHING HERE
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
