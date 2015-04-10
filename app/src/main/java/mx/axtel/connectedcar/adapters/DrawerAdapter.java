package mx.axtel.connectedcar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mx.axtel.connectedcar.R;
import mx.axtel.connectedcar.RecyclerItemClicked;
import mx.axtel.connectedcar.models.Device;

/**
 * Created by marinsalinas on 4/9/15.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    private ArrayList<Device> devices;
    RecyclerItemClicked onRClicked;

    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView imageView;
        RecyclerItemClicked rClicked;



        public ViewHolder(View itemView,int ViewType, RecyclerItemClicked listener) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

           this.rClicked = listener;

            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                itemView.setOnClickListener(this);                                                               // setting holder id as 1 as the object being populated are of type item row

        }


        @Override
        public void onClick(View view) {
           rClicked.rItemClicked(view, getPosition());
        }
    }



    public DrawerAdapter(ArrayList<Device> devices,RecyclerItemClicked onRcli){ // DrawerAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        onRClicked =  onRcli;
        this.devices = devices;
        //in adapter
    }



    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v,viewType, onRClicked); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder



    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position) {
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(devices.get(position).getDeviceID()); // Setting the Text with the array of our Titles

        int resId = getResIdFromDate(devices.get(position).getLastGPSTimestamp());
            holder.imageView.setImageResource(resId);// Settimg the image with array of our icons



    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return devices.size(); // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    private int getResIdFromDate(Date date){
        if(Calendar.getInstance().getTime().getTime() - date.getTime() < 300000L){
            return  R.drawable.circle_green;
        }else if(Calendar.getInstance().getTime().getTime() - date.getTime() < 3600000L){
            return  R.drawable.circle_ambar;
        }else{
            return  R.drawable.circle_red;
        }
    }


}
