package io.github.joshkergan.giftr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import io.github.joshkergan.giftr.db.GiftrDbHelper;

/**
 * Created by Josh on 22/11/2016.
 * Base class for all the activities. Does the common initializations and is safe to call
 * multiple times.
 * To add a view after this class is instantiated call the attachView Method.
 */

public abstract class GiftrActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    protected GiftrDbHelper pDbHelper;
    private CoordinatorLayout baseView;
    private NavigationView navView;
    @Nullable
    private View attachedView = null;
    @Nullable
    private Integer selectedItem = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        navView = (NavigationView) findViewById(R.id.nav_view);
        baseView = (CoordinatorLayout) findViewById(R.id.base_container);

        pDbHelper = GiftrDbHelper.getDbInstance(this);

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        navView.inflateHeaderView(R.layout.nav_header_people);
        navView.setNavigationItemSelectedListener(this);
    }

    protected void attachView(@NonNull View v) {
        attachView(v, null);
    }

    protected View attachView(@LayoutRes int id) {
        return attachView(id, null);
    }


    protected void attachView(
            @NonNull View v,
            @Nullable NavigationView.OnNavigationItemSelectedListener lis
    ) {
        if (attachedView != null){
            baseView.removeView(attachedView);
            navView.setNavigationItemSelectedListener(this);
        }
        if (lis != null){
            navView.setNavigationItemSelectedListener(lis);
        }
        attachedView = v;
        baseView.addView(v);
    }

    protected View attachView(
            @LayoutRes int id,
            @Nullable NavigationView.OnNavigationItemSelectedListener lis
    ) {
        if (attachedView != null){
            baseView.removeView(attachedView);
            navView.setNavigationItemSelectedListener(this);
        }
        if (lis != null){
            navView.setNavigationItemSelectedListener(lis);
        }

        attachedView = getLayoutInflater().inflate(id, baseView);
        baseView.invalidate();
        return attachedView;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_people){
            Intent peopleIntent = new Intent(this, PeopleActivity.class);
            startActivity(peopleIntent);
        }else if (id == R.id.nav_items){
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
