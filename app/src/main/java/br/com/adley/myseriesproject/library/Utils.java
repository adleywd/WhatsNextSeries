package br.com.adley.myseriesproject.library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import br.com.adley.myseriesproject.R;

/**
 * Created by adley on 16/04/16.
 * Class with utilities to project.
 */
public class Utils {

    //--- Append URI ---//
    public static Uri appendUri(Uri uri, HashMap<String, String> queryParams) {
        Uri.Builder newUri = uri.buildUpon().clearQuery();
        if (!queryParams.isEmpty()) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                newUri.appendQueryParameter(key, value);
            }
            return newUri.build();
        }
        return null;
    }

    public static String getApiKey(Context context){
        return context.getString(R.string.api_key);
    }

    public static boolean checkAppConnectionStatus(Context context) {
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
