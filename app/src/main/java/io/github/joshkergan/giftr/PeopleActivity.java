package io.github.joshkergan.giftr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import io.github.joshkergan.giftr.db.GiftrDbHelper;
import io.github.joshkergan.giftr.items.ItemActivity;
import io.github.joshkergan.giftr.people.AddPersonActivity;
import io.github.joshkergan.giftr.people.PeopleAdapter;

final public class PeopleActivity extends GiftrActivity{

    private boolean addPersonActive = false;
    private View activityView;
    private RecyclerView peopleList;
    private PeopleAdapter.OnItemClickListener peopleAction;
    private PeopleAdapter listAdapter;
    private View.OnClickListener fabListener;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityView = attachView(R.layout.people_activity, this);
        peopleList = (RecyclerView) findViewById(R.id.list_people);

        pDbHelper = GiftrDbHelper.getDbInstance(this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final Animation.AnimationListener fabRotate = new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (fab.getRotation() == 0){
                    fab.setRotation(405);
                }else{
                    fab.setRotation(0);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        fabListener = (new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final RotateAnimation rotateAnimation = new RotateAnimation(0, 405, fab.getPivotX(), fab.getPivotY());
                rotateAnimation.setDuration(500);
                rotateAnimation.setAnimationListener(fabRotate);

                // Create the new FABs and display them
                final View newContact = findViewById(R.id.add_new_person_fab);
                final View importContact = findViewById(R.id.import_person_fab);

                newContact.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent addPersonIntent = new Intent(getApplicationContext(), AddPersonActivity.class);
                        startActivity(addPersonIntent);
                    }
                });

                importContact.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent importContactIntent = new Intent(getApplicationContext(), ContactImportActivity.class);
                        startActivity(importContactIntent);
                    }
                });

                fab.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        newContact.setVisibility(View.GONE);
                        importContact.setVisibility(View.GONE);
                        v.startAnimation(rotateAnimation);
                        v.setOnClickListener(fabListener);
                    }
                });

                fab.startAnimation(rotateAnimation);
                newContact.setVisibility(View.VISIBLE);
                importContact.setVisibility(View.VISIBLE);


            }
        });

        fab.setOnClickListener(fabListener);

        peopleAction = new PeopleAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(int position) {
                Intent personIntent = new Intent(getApplicationContext(), PersonActivity.class);
                personIntent.putExtra("PersonID", listAdapter.getItemId(position));
                startActivity(personIntent);
            }
        };

        listAdapter = new PeopleAdapter(pDbHelper.getReadableDatabase(), peopleAction);
        peopleList.setHasFixedSize(false);
        peopleList.setLayoutManager(new GridLayoutManager(this, 2));
        peopleList.setAdapter(listAdapter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_people) {
            // This is the current item
        } else if (id == R.id.nav_items){
            // Handle navigating to items activity
            Intent itemsIntent = new Intent(this, ItemActivity.class);
            startActivity(itemsIntent);
        }else if (id == R.id.nav_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }else if (id == R.id.nav_send){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (addPersonActive){
            attachView(activityView);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listAdapter = new PeopleAdapter(pDbHelper.getReadableDatabase(), peopleAction);
        peopleList.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.people, menu);
        return true;
    }
}
