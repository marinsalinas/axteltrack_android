package mx.axtel.connectedcar;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import mx.axtel.connectedcar.helpers.Session;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Session ss = new Session(getApplicationContext());



        TextView tv = (TextView)findViewById(R.id.tv_main);

        tv.setText(ss.getUserSession().getContactName());

        ButtonFlat btn = (ButtonFlat) findViewById(R.id.buton_main);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((new Session(getApplicationContext()).logOut())) {

                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        });



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
        }

        return super.onOptionsItemSelected(item);
    }
}
