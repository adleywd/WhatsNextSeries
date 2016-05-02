package br.com.adley.myseriesproject.library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.view.View;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static String getApiKey(Context context) {
        return context.getString(R.string.api_key);
    }

    public static boolean checkAppConnectionStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static String convertStringDateToPtBr(String date) throws ParseException {
        SimpleDateFormat fromApi = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
        return newFormat.format(fromApi.parse(date));
    }

    public static void setLayoutInvisible(View viewName) {
        if (viewName.getVisibility() == View.VISIBLE) {
            viewName.setVisibility(View.GONE);
        }
    }

    public static void setLayoutVisible(View viewName) {
        if (viewName.getVisibility() == View.GONE) {
            viewName.setVisibility(View.VISIBLE);
        }
    }

    public static String getDateTimeNow(boolean withTime) {
        DateFormat df = withTime ? new SimpleDateFormat("yyyy-MM-dd HH:mm") : new SimpleDateFormat("yyyy-MM-dd");
        return df.format(Calendar.getInstance().getTime());
    }

    public static String getDateTimeNowPtBr(boolean withTime) {
        DateFormat df = withTime ? new SimpleDateFormat("dd/MM/yyyy HH:mm") : new SimpleDateFormat("dd/MM/yyyy");
        return df.format(Calendar.getInstance().getTime());
    }
}
