package mx.axtel.connectedcar;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.gc.materialdesign.views.ButtonFlat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;
import mx.axtel.connectedcar.fragments.MainFragment;
import mx.axtel.connectedcar.helpers.Session;
import mx.axtel.connectedcar.models.Device;
import mx.axtel.connectedcar.models.User;


public class MainActivity extends MaterialNavigationDrawer {


    @Override
    public void init(Bundle bundle) {
        //Account SetUp
        Session ss = new Session(getApplicationContext());
        final User userSession = ss.getUserSession();
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

        //First Section SetUp
        final MaterialSection section = newSection("Devices", new MainFragment());
        section.setNotifications(99);
        addSection(section);

        //Divisor Element
        addDivisor();

        //Prepare Requets
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


        String URL =  getResources().getString(R.string.device);

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        Gson gson  = new Gson();
                        try {
                           Device[] devices =  gson.fromJson(response.getString("devices"), Device[].class);
                            section.setNotifications(devices.length);
                          for(final Device device : devices){
                               MaterialSection section1 = newSection(device.getDisplayName(),R.drawable.ic_account, new MaterialSectionListener() {
                                   @Override
                                   public void onClick(MaterialSection materialSection) {
                                       Toast.makeText(getApplicationContext(), device.getDisplayName(), Toast.LENGTH_SHORT).show();
                                   }
                               });
                               addSection(section1);
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
        Toast.makeText(getApplicationContext(), userSession.getToken(), Toast.LENGTH_SHORT).show();
        queue.add(req);
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
}
