package mx.axtel.connectedcar.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by marinsalinas on 3/24/15.
 */
public class NetworkHelper {

    public static  boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
