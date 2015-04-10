package mx.axtel.connectedcar;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

import mx.axtel.connectedcar.adapters.DrawerAdapter;
import mx.axtel.connectedcar.helpers.Session;
import mx.axtel.connectedcar.models.Device;
import mx.axtel.connectedcar.models.User;


public class MainActivity extends ActionBarActivity implements RecyclerItemClicked{


    String TITLES[] = {"Home","Events","Mail","Shop","Travel","Home","Events","Mail","Shop","Travel"};
    int ICONS[] = {R.drawable.ic_account,
            R.drawable.ic_password,R.drawable.ic_username,
            R.drawable.ic_account,R.drawable.ic_username,
            R.drawable.ic_account,R.drawable.ic_password,
            R.drawable.ic_username,R.drawable.ic_account,
            R.drawable.ic_username};
    private boolean mSlideSate = false;

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    private User userSesion;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    ArrayList<Device> devices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*Get the User by Session*/
        userSesion = new Session(this).getUserSession();

        /*Inititalize Devices Arraylist*/
        devices = new ArrayList<>();

        //Dummy
        Device dev = new Device();
        dev.setAccountID("integracion");
        dev.setActive(true);
        dev.setDescription("Vehiculo Patito");
        dev.setDeviceID("178698276921");
        dev.setDisplayName("Marin Car");
        dev.setGroupID("Grupito");
        dev.setLastGPSTimestamp(new Date());
        dev.setLastEventTimestamp(new Date());
        dev.setLastOdometerKM(113.45);
        dev.setLastUpdateTime(new Date());
        dev.setLastValidHeading(35.55);
        dev.setLastValidLatitude(25.2321);
        dev.setLastValidLongitude(-100.434);

        devices.add(dev);

        /* Assinging the toolbar object ot the view
        and setting the the Action bar to our toolbar
         */
        toolbar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        toolbar.setTitle(userSesion.getAccount());
        setSupportActionBar(toolbar);


        /*Set Account information to Navigation Drawer Header*/
        setAccountHeader();

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mAdapter = new DrawerAdapter(devices, this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        TITLES[0] = "CASA";
        mAdapter.notifyDataSetChanged();




        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSlideSate= !mSlideSate;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideSate= !mSlideSate;
            }



        }; // Drawer Toggle Object Made

        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_logout){
            new Session(this).logOut();
            startActivity(new Intent(getApplicationContext(), SplashScreen.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void rItemClicked(View view, int position) {
        toogleDrawer();
        Toast.makeText(getApplicationContext(), position + "" , Toast.LENGTH_SHORT).show();
    }


    public void setAccountHeader(){

        View headerView = findViewById(R.id.drw_header);


        TextView name = (TextView) headerView.findViewById(R.id.header_name);         // Creating Text View object from header.xml for name
        TextView email = (TextView) headerView.findViewById(R.id.header_email);       // Creating Text View object from header.xml for email
        ImageView imageProfile = (ImageView) headerView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
        TextView phone = (TextView) headerView.findViewById(R.id.header_phone);

        name.setText(userSesion.getContactName());
        email.setText(userSesion.getContactEmail());
        phone.setText(userSesion.getContactPhone());

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(getCapitals(userSesion.getContactName()), Color.RED);
        imageProfile.setImageDrawable(drawable);


    }

    private String getCapitals(String name){
        String[] names = name.split(" ");
        String capitals = null;
        if(names.length == 1 || names.length > 4){
            capitals = names[0].charAt(0) + "";
        }else if(names.length == 4){
            capitals = names[0].charAt(0) + ""+names[2].charAt(0);
        }else if(names.length == 2){
            capitals = names[0].charAt(0) + ""+names[1].charAt(0);
        }else{
            capitals = names[0].charAt(0) + "";
        }
        return capitals;
    }

    private void toogleDrawer(){
        if(mSlideSate){
            Drawer.closeDrawer(Gravity.START);
        }else{
            Drawer.openDrawer(Gravity.START);
        }
    }


}

