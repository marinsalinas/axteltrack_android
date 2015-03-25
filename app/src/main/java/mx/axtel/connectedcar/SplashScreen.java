package mx.axtel.connectedcar;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mx.axtel.connectedcar.helpers.NetworkHelper;
import mx.axtel.connectedcar.helpers.Session;
import mx.axtel.connectedcar.models.User;


public class SplashScreen extends ActionBarActivity {
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //setToolbar();

       if(session == null){
           session = new Session(getApplicationContext());
       }


       if(session.isLoggedIn() && NetworkHelper.isOnline(getApplicationContext())){
           RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

           //Post Params.
           Map<String, String> params = new HashMap<String, String>();
           params.put("account", session.getUserSession().getAccount());
           params.put("user", session.getUserSession().getUser());
           params.put("password", session.getUserSession().getPassword());

           String URL =  getResources().getString(R.string.auth_login);

           JsonObjectRequest req = new JsonObjectRequest(
                   Request.Method.POST,
                   URL,
                   new JSONObject(params),
                   new Response.Listener<JSONObject>() {
                       @Override
                       public void onResponse(JSONObject response) {
                           Gson gson  = new Gson();
                           session.createLoginSession(gson.fromJson(response.toString(), User.class));
                           startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                           finish();
                       }
                   },
                   new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           session.logOut();
                           startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                           finish();
                       }
                   }) {
               //Configurando Headers para que tome JSON
               @Override
               public Map<String, String> getHeaders() throws AuthFailureError {
                   HashMap<String, String> headers = new HashMap<String, String>();
                   headers.put("Content-Type", "application/json; charset=utf-8");
                   return headers;
               }
           };
           queue.add(req);
       }else{
           startActivity(new Intent(getApplicationContext(), LoginActivity.class));
           finish();
       }


    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

}
