package mx.axtel.connectedcar.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonFlat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.axtel.connectedcar.HistoryActivity;
import mx.axtel.connectedcar.R;
import mx.axtel.connectedcar.models.Device;
import mx.axtel.connectedcar.models.User;

/**
 * Created by marinsalinas on 4/24/15.
 */
public class InfoDialogFragment extends DialogFragment implements View.OnClickListener {
    private TextView mTextView;
    private ImageButton mImageButton;
    private ButtonFlat mbuttonMobilize;
    private ButtonFlat mbuttonImmobilize;
    private TextView mTextDevId;
    private TextView mTextLReport;
    private TextView mTextPosition;
    private TextView mTextSpped;
    private User userSession;
    private Device device;

    enum Command {
        MOBILIZE,
        IMMOBILIZE
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Build the dialog and set up the button click handlers
        this.device = (Device) getArguments().getSerializable("DEVICE");
        this.userSession = (User) getArguments().getSerializable("USER");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.device_info_dialog, null);
        mTextView = (TextView) view.findViewById(R.id.dialog_header);
        mImageButton = (ImageButton) view.findViewById(R.id.eventdata_button);
        mTextDevId = (TextView) view.findViewById(R.id.dialog_devID);
        mTextLReport = (TextView) view.findViewById(R.id.dialog_lastGPS);
        mTextPosition = (TextView) view.findViewById(R.id.dialog_position);
        mTextSpped = (TextView) view.findViewById(R.id.dialog_speed);
        mbuttonMobilize = (ButtonFlat) view.findViewById(R.id.mobilize);
        mbuttonImmobilize = (ButtonFlat) view.findViewById(R.id.immobilize);

        mbuttonMobilize.setOnClickListener(this);
        mbuttonImmobilize.setOnClickListener(this);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                intent.putExtra("Device", device);
                getActivity().startActivity(intent);
            }
        });

        String title = null;
        if (this.device.getDescription() != null && this.device.getDescription() != "") {
            title = this.device.getDescription();
        } else
        if (this.device.getDisplayName() != null && this.device.getDisplayName() != "") {
            title = this.device.getDisplayName();
        } else {
            title = this.device.getDeviceID();
        }

        mTextView.setText(title);
        mTextDevId.setText(device.getDeviceID());
        mTextLReport.setText(device.getLastGPSTimestamp().toString());
        mTextPosition.setText(device.getLastValidLatitude()+" / "+device.getLastValidLongitude());
        mTextSpped.setText(device.getLastValidSpeedKPH() + "KPH");

        builder.setView(view);
               /* .setPositiveButton(R.string.mobilize, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        Toast.makeText(getActivity(), "POSITIVO", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.immobilize, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        Toast.makeText(getActivity(), "POSITIVO", Toast.LENGTH_SHORT).show();

                    }
                });*/

        final AlertDialog dialog = builder.create();


        return dialog;
    }


    @Override
    public void onClick(View view) {

        final Command command;
        String title = null;

        if (view.getId() == R.id.mobilize) {
            command = Command.MOBILIZE;
            title = getString(R.string.mobilize);
        } else if (view.getId() == R.id.immobilize) {
            command = Command.IMMOBILIZE;
            title = getString(R.string.immobilize);
        }else{
            return;
        }

        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(getResources().getString(R.string.sure_command_send))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendCommand(command);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }



    public void sendCommand(Command cmd){

        String URL = null;
        if(cmd == Command.MOBILIZE){
            URL = getResources().getString(R.string.mobilizeURL);
        }else {
            URL = getResources().getString(R.string.immobilizeURL);
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                URL + device.getDeviceID(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.command_sent), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.networkResponse.data + "", Toast.LENGTH_SHORT).show();
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

