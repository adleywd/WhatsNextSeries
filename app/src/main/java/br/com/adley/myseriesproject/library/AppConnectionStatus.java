package br.com.adley.myseriesproject.library;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Adley on 09/04/2016.
 */
public class AppConnectionStatus {
    public boolean isInternetConnection() {
        return internetConnection;
    }

    private boolean internetConnection;
    public AppConnectionStatus(Context context){
        internetConnection = checkInternetConnection(context);
    }
    private boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }else{
            return false;
        }
    }
}
