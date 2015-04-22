package mx.axtel.connectedcar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mx.axtel.connectedcar.adapters.DrawerAdapter;
import mx.axtel.connectedcar.helpers.Session;
import mx.axtel.connectedcar.models.Device;
import mx.axtel.connectedcar.models.User;


public class MainActivity extends ActionBarActivity implements RecyclerItemClicked, GoogleMap.OnMarkerClickListener{

    private boolean mSlideSate = false;

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    private User userSession;
    private Gson gson;
    private PrettyTime prettyTime;
    private GoogleMap gMap;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    ArrayList<Device> devices;
    ArrayList<Marker> markers;
    private  static int itemSelected = -1;
    Handler handler;
    private Runnable runnable;
    private static boolean first = true;
    private static CameraPosition cameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*Get the User by Session*/
        userSession = new Session(this).getUserSession();

        /*Inititalize Devices Arraylist*/
        devices = new ArrayList<>();

        /*Initialize Gson From Gson Builder and PrettyTime Helper*/
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        prettyTime = new PrettyTime();


        /* Assinging the toolbar object ot the view
        and setting the the Action bar to our toolbar
         */
        toolbar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        toolbar.setTitle(userSession.getAccount());
        setSupportActionBar(toolbar);

        if(gMap == null){
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            gMap = mapFragment.getMap();
            gMap.setOnMarkerClickListener(this);
            gMap.setMyLocationEnabled(true);
            gMap.setBuildingsEnabled(true);

            //gMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(25.6667, -100.3167), 15F, 2F, 0F)));
        }

        /*Set Account information to Navigation Drawer Header*/
        setAccountHeader();

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mAdapter = new DrawerAdapter(devices, this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView


        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

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

       // getDevices();

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
        itemSelected = position;
        if(gMap !=null){

            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(markers.get(position).getPosition(), 15F, 2F, 0F)));
            markers.get(position).showInfoWindow();
            //markers.add(marker);
        }
        Toast.makeText(getApplicationContext(), position + "" , Toast.LENGTH_SHORT).show();
    }


    public void setAccountHeader(){

        View headerView = findViewById(R.id.drw_header);


        TextView name = (TextView) headerView.findViewById(R.id.header_name);         // Creating Text View object from header.xml for name
        TextView email = (TextView) headerView.findViewById(R.id.header_email);       // Creating Text View object from header.xml for email
        ImageView imageProfile = (ImageView) headerView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
        TextView phone = (TextView) headerView.findViewById(R.id.header_phone);

        name.setText(userSession.getContactName());
        email.setText(userSession.getContactEmail());
        phone.setText(userSession.getContactPhone());

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(getCapitals(userSession.getContactName()), Color.RED);
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


    private void getDevices(){
        //Prepare Requests
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                getResources().getString(R.string.device),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONArray devs = response.getJSONArray("devices");
                                    if(devices == null){
                                        devices = new ArrayList<>();
                                    }
                                    if(markers == null){
                                        markers = new ArrayList<>();
                                    }
                                    markers.clear();
                                    devices.clear();
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    for(int i = 0; i <devs.length() ; i++){
                                        final Device device = gson.fromJson(devs.getJSONObject(i).toString(), Device.class);
                                        if(device.isActive()){

                                            LatLng latLng = new LatLng(device.getLastValidLatitude(), device.getLastValidLongitude());
                                            Marker marker = gMap.addMarker(new MarkerOptions()
                                                    .title(device.getDeviceID())
                                                    .snippet(prettyTime.format(device.getLastGPSTimestamp()))
                                                    .position(latLng)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow_red))
                                                    .flat(true)
                                                    .rotation((float) device.getLastValidHeading()));

                                            devices.add(device);
                                            markers.add(marker);
                                            if(latLng.latitude != 0 && latLng.longitude != 0)
                                                builder.include(latLng);
                                        }
                                    }

                                    toolbar.setTitle("("+devices.size()+") "+userSession.getAccount());

                                    if(first) {
                                        gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 23));
                                        first = !first;
                                    }else{
                                       // gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        if(itemSelected != -1){
                                            markers.get(itemSelected).showInfoWindow();
                                            gMap.moveCamera(CameraUpdateFactory.newLatLng(markers.get(itemSelected).getPosition()));
                                        }
                                    }

                                    //Refresh RecyclerView From new DataSet
                                    mAdapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof TimeoutError){
                            Toast.makeText(getApplicationContext(), R.string.check_internet, Toast.LENGTH_LONG).show();
                            return;
                        }

                        Toast.makeText(getApplicationContext(), "ERROR" + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();

                        /*if(error.networkResponse.statusCode == 400){
                            mAccountView.setError("BAD REQUEST");
                        }else if(error.networkResponse.statusCode == 401){
                            mAccountView.setError(getResources().getString(R.string.error_unable_login));
                        }else if(error.networkResponse.statusCode == 403){
                            mAccountView.setError(getResources().getString(R.string.error_forbidden));
                        }else{
                            mAccountView.setError(getResources().getString(R.string.error_unable_login));
                        }
                        showProgress(false);
                        */

                    }
                }) {
            //Configurando Headers para que tome JSON
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", userSession.getToken());
                return headers;
            }
        };
        queue.add(req);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        itemSelected = markers.indexOf(marker);
        return false;
    }

    @Override
    public void onResume(){
     super.onResume();
        Log.e("RESUME", "RESUME");
        getDevices();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "DEVICEs", Toast.LENGTH_SHORT).show();
                getDevices();
                handler.postDelayed(this, 30000);
            }
        };
        handler.postDelayed(runnable, 30000);

    }

    @Override
    public void onStop(){
        super.onStop();
        Log.e("STOP", "STOP");
    }

    @Override
    public void onPause(){
        super.onPause();
        handler.removeCallbacks(runnable);
        Log.e("PAUSE", "PAUSE");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
         cameraPosition = gMap.getCameraPosition();
        Log.e("DESTROY", "DESTROY");
    }



}

