package mx.axtel.connectedcar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.SphericalUtil;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import mx.axtel.connectedcar.helpers.MapHelper;
import mx.axtel.connectedcar.helpers.NetworkHelper;
import mx.axtel.connectedcar.helpers.Session;
import mx.axtel.connectedcar.models.Device;
import mx.axtel.connectedcar.models.EventData;

import static com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;

public class HistoryActivity extends ActionBarActivity implements OnCameraChangeListener, GoogleMap.OnMapLongClickListener {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = -1;
    private GoogleMap gmap;
    private Device device;
    private Marker marker;
    private Toolbar toolbar;
    private TextView textDateFrom;
    private TextView textTimeFrom;
    private TextView textDateTo;
    private TextView textTimeTo;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    private static DatePickerDialog datePickerDialogFrom;
    private static TimePickerDialog timePickerDialogFrom;
    private static DatePickerDialog datePickerDialogTo;
    private static TimePickerDialog timePickerDialogTo;
    private Button showButton;
    private ArrayList<EventData> positions;
    private ArrayList<Marker> headings;
    protected Polyline poly;
    private  Marker fromMarker;
    private Marker toMarker;
    private View mProgressView;

    private double proviousZoomLevel = -1.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.device = (Device)getIntent().getSerializableExtra("Device");
        if (device == null) {
            finish();
        }//Tomamos la informacion del Dispositivo para poner titulo en la action Bar

        setContentView(R.layout.activity_history);
        if(checkPlayServices()) {
            mapInit();
        }

        textDateFrom = (TextView) findViewById(R.id.date_spinnerFrom);
        textTimeFrom = (TextView) findViewById(R.id.time_spinnerFrom);
        textDateTo = (TextView) findViewById(R.id.date_spinnerTo);
        textTimeTo = (TextView) findViewById(R.id.time_spinnerTo);
        mProgressView = findViewById(R.id.history_progress);

        final DateFormat formatDate =  DateFormat.getDateInstance(DateFormat.FULL, getResources().getConfiguration().locale);
        final DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT, getResources().getConfiguration().locale);
        textDateFrom.setText(formatDate.format(new Date((new Date().getTime())- 86400000L)));
        textTimeFrom.setText(formatTime.format(new Date(new Date().getTime() - 86400000L )));
        textDateTo.setText(formatDate.format(new Date()));
        textTimeTo.setText(formatTime.format(new Date()));

        toolbar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        toolbar.setTitle(device.getLabel());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });



        textDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textDateFrom.setEnabled(false);
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(formatDate.parse(textDateFrom.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialogFrom = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int month, int dayOnMonth) {
                        textDateFrom.setEnabled(true);
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, dayOnMonth);
                        textDateFrom.setText(formatDate.format(cal.getTime()));
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),true);
                datePickerDialogFrom.setVibrate(true);
                datePickerDialogFrom.setYearRange(Calendar.getInstance().get(Calendar.YEAR)-1, Calendar.getInstance().get(Calendar.YEAR));

                datePickerDialogFrom.show(getSupportFragmentManager(), DATEPICKER_TAG);
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100L);
                            textDateFrom.setEnabled(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.run();
            }
        });

        textTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTimeFrom.setEnabled(false);
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(formatTime.parse(textTimeFrom.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timePickerDialogFrom = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
                        textTimeFrom.setEnabled(true);
                        Calendar cal = Calendar.getInstance();
                        cal.set(0, Calendar.JANUARY, 0, hourOfDay, minute);
                        textTimeFrom.setText(formatTime.format(cal.getTime()));
                    }


                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
                timePickerDialogFrom.setVibrate(true);
                timePickerDialogFrom.show(getSupportFragmentManager(), TIMEPICKER_TAG);
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100L);
                            textTimeFrom.setEnabled(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.run();
            }
        });

        textDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textDateTo.setEnabled(false);
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(formatDate.parse(textDateTo.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialogTo = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int month, int dayOnMonth) {
                        textDateTo.setEnabled(true);
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, dayOnMonth);
                        textDateTo.setText(formatDate.format(cal.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
                datePickerDialogTo.setVibrate(true);
                datePickerDialogTo.setYearRange(Calendar.getInstance().get(Calendar.YEAR) - 1, Calendar.getInstance().get(Calendar.YEAR));
                datePickerDialogTo.show(getSupportFragmentManager(), DATEPICKER_TAG);
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100L);
                            textDateTo.setEnabled(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.run();
            }
        });

        textTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTimeTo.setEnabled(false);
                Calendar calendar =  Calendar.getInstance();
                try {
                    calendar.setTime(formatTime.parse(textTimeTo.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timePickerDialogTo = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
                        textTimeTo.setEnabled(true);
                        Calendar cal = Calendar.getInstance();
                        cal.set(0, Calendar.JANUARY, 0, hourOfDay, minute);
                        textTimeTo.setText(formatTime.format(cal.getTime()));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

                timePickerDialogTo.setVibrate(true);
                timePickerDialogTo.show(getSupportFragmentManager(), TIMEPICKER_TAG);
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100L);
                            textTimeTo.setEnabled(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.run();
            }
        });

        showButton = (Button) findViewById(R.id.history_button_run);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dfmt = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.SHORT, getResources().getConfiguration().locale);

                try {
                    final Date dateFrom = dfmt.parse(textDateFrom.getText() + " "+textTimeFrom.getText());

                    final Date dateTo = dfmt.parse(textDateTo.getText() + " "+textTimeTo.getText());
                    Log.d("DATE-FROM", dateFrom.toString()+"  "+textDateFrom.getText() + " "+textTimeFrom.getText());
                    Log.d("DATE-TO", dateTo.toString()+"  "+textDateTo.getText() + " "+textTimeTo.getText());

                    //return;

                    if(dateTo.getTime() - dateFrom.getTime() <= 0){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.history_initial_date) , Toast.LENGTH_SHORT).show();
                    }else if(dateTo.getTime() - dateFrom.getTime() > 432000000){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.history_range_date), Toast.LENGTH_SHORT).show();
                    }else{
                        //showButton.setEnabled(false);
                        showProgress(true);
                        device.getDeviceID();
                        dateTo.getTime();
                        dateFrom.getTime();
                        final String apiKey = new Session(getApplicationContext()).getUserSession().getToken();
                        if(poly != null)poly.remove();
                        if(fromMarker != null)fromMarker.remove();
                        if(toMarker != null)toMarker.remove();
                        if(headings != null) {
                            for (Marker mark : headings){
                                mark.remove();

                            }
                            headings.clear();
                        }else{
                            headings = new ArrayList<Marker>();
                        }

                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        JSONObject body = new JSONObject();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        try {
                            body.accumulate("TimeFrom", sdf.format(dateFrom));
                            body.accumulate("TimeTo", sdf.format(dateTo));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonArrayRequest req = new JsonArrayRequest(
                                Request.Method.GET,
                                getResources().getString(R.string.eventDataURL, device.getDeviceID(), URLEncoder.encode(sdf.format(dateFrom)), URLEncoder.encode(sdf.format(dateTo))),
                                body,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        Log.e("JSONDATE", response.toString());
                                        try {

                                            if(response.length() <= 0 || response != null) {
                                                JSONArray eventDats = response;
                                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                                                PrettyTime prettyTime = new PrettyTime();

                                                PolylineOptions polyOpts = new PolylineOptions();
                                                polyOpts.geodesic(true);
                                                polyOpts.color(Color.argb(90, 0, 87, 194));
                                                polyOpts.zIndex(10000000000L);


                                                for (int i = 0; i < eventDats.length(); i++) {
                                                    EventData eventData = gson.fromJson(eventDats.getJSONObject(i).toString(), EventData.class);

                                                    Log.e("EVENTDATA", eventData.getTimestamp().toString());
                                                    int iconArrow = MapHelper.getArrowfromSpeed(eventData.getSpeedKPH());
                                                    LatLng latLng = new LatLng(eventData.getLatitude(), eventData.getLongitude());
                                                    Marker marker = gmap.addMarker(new MarkerOptions()
                                                            .title("Event Data")
                                                            .icon(BitmapDescriptorFactory.fromResource(iconArrow))
                                                            .snippet(prettyTime.format(eventData.getTimestamp()))
                                                            .flat(true)
                                                            .position(latLng)
                                                            .rotation((float) eventData.getHeading()));


                                                    headings.add(marker);
                                                    if (latLng.latitude != 0 && latLng.longitude != 0) {
                                                        builder.include(latLng);
                                                        polyOpts.add(latLng);
                                                    }

                                                }


                                                poly = gmap.addPolyline(polyOpts);
                                            }else{
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.history_not_found),Toast.LENGTH_SHORT).show();
                                            }
                                            showProgress(false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("HAY ERROR", error.toString());
                                        if(error instanceof TimeoutError){
                                            Toast.makeText(getApplicationContext(), R.string.check_internet, Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        Toast.makeText(getApplicationContext(), "ERROR" + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Authorization", apiKey);
                                return headers;
                            }
                        };

                        queue.add(req);


                        //Log.e("request", dateFrom.getTime() + "\n" + dateTo.getTime() + "\n" + device.getDeviceID() + "\n" + apiKey);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.home){
            Toast.makeText(getApplicationContext(), "CLICK ATRAS", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.getItem(0).setVisible(false);
        return super.onPrepareOptionsMenu(menu);

    }

    public void mapInit(){
        this.gmap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapHistory)).getMap();
        this.gmap.setMyLocationEnabled(false);
        this.gmap.setBuildingsEnabled(false);
        this.gmap.getUiSettings().setZoomControlsEnabled(false);
        this.gmap.getUiSettings().setAllGesturesEnabled(true);
        this.gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.device.getLastValidLatitude(), this.device.getLastValidLongitude()), 15));
        PrettyTime p = new PrettyTime(getResources().getConfiguration().locale);
        this.marker = gmap.addMarker(new MarkerOptions()
                        .position(new LatLng(device.getLastValidLatitude(), device.getLastValidLongitude()))
                        .title(device.getDeviceID())
                        .snippet((device.getLastGPSTimestamp() == null) ? "No Date" : p.format(device.getLastGPSTimestamp()))
                        //.icon(BitmapDescriptorFactory.fromResource(CommonUtilities.ResIdFromCarType(device.getCarType())))
        );
        //mClusterManager = new ClusterManager<Position>(this, this.gmap);
        //this.gmap.setOnCameraChangeListener(mClusterManager);
        //this.gmap.setOnMarkerClickListener(mClusterManager);
        this.gmap.setOnCameraChangeListener(this);
        this.gmap.setOnMapLongClickListener(this);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("CHECKPLAYSERCIVES", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            showButton.setVisibility(show ? View.GONE : View.VISIBLE);
            showButton.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    showButton.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            showButton.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if(proviousZoomLevel != cameraPosition.zoom) {
            if (positions != null && headings != null) {
                drawMarkerByZoomLevel(cameraPosition.zoom);
            }
        }
        proviousZoomLevel = cameraPosition.zoom;

    }

    public void drawMarkerByZoomLevel(double zoomLevel){
       /* Log.e("ZOOM", "Zoom"+zoomLevel);

        final DateFormat formatDate =  DateFormat.getDateInstance(DateFormat.FULL, getResources().getConfiguration().locale);
        final DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT, getResources().getConfiguration().locale);
        //double distanceFLAG = (209900/19) - (9950/19)*zoomLevel;
        double distanceFLAG = 165555.8181*Math.exp((-0.2766531246)*zoomLevel);
        for(Marker mark : headings){
            mark.remove();

        }

        Log.e("DISTANCETARGT", distanceFLAG+"");
        headings.clear();
        double distance = 0.0;
        if(zoomLevel < 9){return;}
        for(int i = 0; i< positions.size(); i++){
            if( i+1 < positions.size()) {

                //Log.e("DISTANCE", distance+"");
                //Log.e("DISTANCETARGT", distanceFLAG+"");
                if (distance > distanceFLAG) {
                    headings.add(gmap.addMarker(new MarkerOptions()
                                    .position(positions.get(i).getPosition())
                                    .rotation((float) SphericalUtil.computeHeading(positions.get(i).getPosition(), positions.get(i + 1).getPosition()))
                                    .flat(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_compass))
                                    .title(formatDate.format(positions.get(i).getDate()))
                                    .snippet(formatTime.format(positions.get(i).getDate()))
                    ));
                    distance = 0.0;
                } else {
                    distance = distance + SphericalUtil.computeDistanceBetween(positions.get(i).getPosition(), positions.get(i + 1).getPosition());
                }
            }
        }*/

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Esto es para que el al cambiar de orientación haga cambie tambien la orientacòn del datePicker
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            View pickerLayout = findViewById(R.id.picker_layout);
            pickerLayout.setVisibility(View.GONE);

        }else{
            View pickerLayout = findViewById(R.id.picker_layout);
            pickerLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        this.gmap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "Title", null);


                ArrayList<Uri> imageUris = new ArrayList<Uri>();
                imageUris.add(Uri.parse(path));

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share images to.."));
            }
        });
    }
}
