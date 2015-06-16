package mx.axtel.connectedcar.helpers;

import mx.axtel.connectedcar.R;

/**
 * Created by marinsalinas on 6/15/15.
 */
public class MapHelper {
    public static int getArrowfromSpeed(double odometer){
        if(odometer >= 0 && odometer < 8){
            return R.drawable.ic_arrow_red;
        }else if(odometer >= 8 && odometer < 32){
            return R.drawable.ic_arrow_ambar;
        } else if (odometer >= 32){
            return R.drawable.ic_arrow_green;
        }else{
            return R.drawable.ic_arrow;
        }
    }
}
