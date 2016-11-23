package io.github.joshkergan.giftr;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import io.github.joshkergan.giftr.items.AsyncGetItems;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.joshkergan.giftr.db.GiftrDbHelper;
import io.github.joshkergan.giftr.items.ItemActivity;
import io.github.joshkergan.giftr.people.PeopleAdapter;

final public class PeopleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static GiftrDbHelper pDbHelper;
    private boolean addPersonActive = false;
    private View activityView;
    private PeopleAdapter.OnItemClickListener peopleAction;
    private PeopleAdapter listAdapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_activity);
        activityView = findViewById(R.id.drawer_layout);
        final DrawerLayout drawer = (DrawerLayout) activityView;
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final RecyclerView peopleList = (RecyclerView) findViewById(R.id.list_people);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        pDbHelper = GiftrDbHelper.getDbInstance(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final View addPerson = getLayoutInflater().inflate(R.layout.add_person, null);
                setContentView(addPerson);
                addPersonActive = true;
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
                        setContentView(activityView);
                        addPersonActive = false;
                        peopleList.setAdapter(new PeopleAdapter(pDbHelper.getReadableDatabase(), peopleAction));
                    }
                });
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        MenuItem peopleItem = (MenuItem) drawer.findViewById(R.id.nav_people);

        navigationView.inflateHeaderView(R.layout.nav_header_people);
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (addPersonActive){
            setContentView(activityView);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.people, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
