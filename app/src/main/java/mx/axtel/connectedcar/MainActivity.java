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
import com.gc.materialdesign.views.ButtonFlat;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;
import mx.axtel.connectedcar.fragments.MainFragment;
import mx.axtel.connectedcar.helpers.Session;
import mx.axtel.connectedcar.models.User;


public class MainActivity extends MaterialNavigationDrawer {


    @Override
    public void init(Bundle bundle) {
        //Account SetUp
        Session ss = new Session(getApplicationContext());
        User userSession = ss.getUserSession();
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

        //First Section SetUp
        MaterialSection section = newSection("Devices", new MainFragment());
        section.setNotifications(99);
        addSection(section);



        addDivisor();

        MaterialSection section1 = newSection("Device 1",R.drawable.ic_account, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Toast.makeText(getApplicationContext(), "Device 1", Toast.LENGTH_SHORT).show();
            }
        });
        addSection(section1);

        MaterialSection section2 = newSection("Device 2", new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Toast.makeText(getApplicationContext(), "Device 2", Toast.LENGTH_SHORT).show();
            }
        });
        addSection(section2);

        MaterialSection section3 = newSection("Device 3", new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Toast.makeText(getApplicationContext(), "Device 3", Toast.LENGTH_SHORT).show();
            }
        });
        addSection(section3);

        MaterialSection section4 = newSection("Device 4", new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Toast.makeText(getApplicationContext(), "Device 4", Toast.LENGTH_SHORT).show();
            }
        });
        addSection(section4);

        MaterialSection section5 = newSection("Device 5", new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Toast.makeText(getApplicationContext(), "Device 5", Toast.LENGTH_SHORT).show();
            }
        });
        addSection(section5);

        MaterialSection section6 = newSection("Device 6", new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Toast.makeText(getApplicationContext(), "Device 6", Toast.LENGTH_SHORT).show();
            }
        });
        addSection(section6);

        MaterialSection section7 = newSection("Device 7", new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Toast.makeText(getApplicationContext(), "Device 7", Toast.LENGTH_SHORT).show();
            }
        });
        addSection(section7);

        MaterialSection section8 = newSection("Device 8", new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Toast.makeText(getApplicationContext(), "Device 8", Toast.LENGTH_SHORT).show();
            }
        });
        addSection(section8);

        MaterialSection section9 = newSection("Device 9", new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Toast.makeText(getApplicationContext(), "Device 9", Toast.LENGTH_SHORT).show();
            }
        });
        addSection(section9);

        MaterialSection section10 = newSection("Device 10", new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Toast.makeText(getApplicationContext(), "Device 10", Toast.LENGTH_SHORT).show();
            }
        });
        addSection(section10);


        MaterialSection section11 = newSection("Device11", new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Toast.makeText(getApplicationContext(), "Device 11", Toast.LENGTH_SHORT).show();
            }
        });
        addSection(section11);


        setDrawerHeaderCustom(view);
        closeDrawer();
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
