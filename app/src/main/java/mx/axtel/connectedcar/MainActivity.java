package mx.axtel.connectedcar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;
import mx.axtel.connectedcar.fragments.MainFragment;
import mx.axtel.connectedcar.helpers.Session;
import mx.axtel.connectedcar.models.Device;
import mx.axtel.connectedcar.models.User;


public class MainActivity extends MaterialNavigationDrawer implements OnMapReadyCallback {
    private Device[] devices;
    private MaterialSection deviceCountSection;
    private GoogleMap googleMap;
    private User userSession;
    @Override
    public void init(Bundle bundle) {
        //Account SetUp
        Session ss = new Session(getApplicationContext());
        userSession = ss.getUserSession();
        View view = LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        TextView tvHeaderName = (TextView)view.findViewById(R.id.header_name);
        TextView tvHeaderEmail = (TextView) view.findViewById(R.id.header_email);
        TextView tvHeaderPhone = (TextView) view.findViewById(R.id.header_phone);
        ImageView iViewHeader = (ImageView) view.findViewById(R.id.circleView);
        tvHeaderName.setText(userSession.getContactName());
        tvHeaderEmail.setText(userSession.getContactEmail());
        tvHeaderPhone.setText(userSession.getContactPhone());


        //Build a text drawable
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(getCapitals(userSession.getContactName()), Color.RED);

        iViewHeader.setImageDrawable(drawable);

        //Enable Arrow Animation
        allowArrowAnimation();
        setDrawerHeaderCustom(view);

       MapFragment mapFragment = new MapFragment();
        mapFragment.getMapAsync(this);

        //First Section SetUp
       deviceCountSection = newSection(getResources().getString(R.string.devices),mapFragment);
        addSection(deviceCountSection);

        //Divisor Element
        addDivisor();
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

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(25.6667, -100.3167)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));


        //Prepare Requests
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                getResources().getString(R.string.device),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson  = new Gson();
                        try {
                            devices =  gson.fromJson(response.getString("devices"), Device[].class);
                            deviceCountSection.setNotifications(devices.length);
                            for(final Device device : devices){
                                if(device.isActive()){
                                    MaterialSection section1 = newSection(device.getDeviceID(),R.drawable.circle_green, new MaterialSectionListener() {
                                        @Override
                                        public void onClick(MaterialSection materialSection) {
                                            Toast.makeText(getApplicationContext(), device.getDeviceID(), Toast.LENGTH_SHORT).show();
                                            LatLng latLng = new LatLng(device.getLastValidLatitude(), device.getLastValidLongitude());
                                            googleMap.setMyLocationEnabled(true);
                                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                                            googleMap.addMarker(new MarkerOptions()
                                                            .title(device.getDeviceID())
                                                            .snippet(device.getDescription())
                                                            .position(latLng)
                                            );

                                        }
                                    });
                                    ImageView sectionImageView =  ((ImageView)section1.getView().findViewById(it.neokree.materialnavigationdrawer.R.id.section_icon));
                                    sectionImageView.setColorFilter(R.color.green);
                                    sectionImageView.setAlpha(1.0F);
                                    addSection(section1);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
}
