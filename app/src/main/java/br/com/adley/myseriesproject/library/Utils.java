package br.com.adley.myseriesproject.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import br.com.adley.myseriesproject.R;

/**
 * Created by adley on 16/04/16.
 * Class with utilities methods.
 * Most of the methods in this class was static.
 * To call a method call the class first.
 * Example: Utils.getApiKey(MyContext);
 */
public class Utils {

    /***
     * Append query parameters to a base uri.
     * @param uri Base Uri to append with the params.
     * @param queryParams HashMap containing params. HashMap< query_name, value >
     * @return New Uri with baseUri + parameters
     */
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

    /***
     * Return the api key from strings xml called apiconfig_strings.xml
     * To konw how to configure this file, check README file.
     * @param context Context where you will use the method.
     * @return String containing the api key
     */
    public static String getApiKey(Context context) {
        return context.getString(R.string.api_key);
    }

    /***
     * Check if have any connection (wifi / 3g / 4g).
     * @param context Context where you will use the method.
     * @return Boolean telling if has or not any connection.
     */
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

    /***
     * Convert a Date inside a String to Brazilian(pt-br) format.
     * @param date String date that you want to change format.
     * @return The new String formatted to Pt-Br format.
     * @throws ParseException
     */
    public static String convertStringDateToPtBr(String date) throws ParseException {
        SimpleDateFormat fromApi = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
        return newFormat.format(fromApi.parse(date));
    }

    /***
     * Set a layout invisible.
     * @param view View that you want to set invisible.
     */
    public static void setLayoutInvisible(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    /***
     * Set a layout visible.
     * @param view View that you want to set visible.
     */
    public static void setLayoutVisible(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /***
     * Get Date Time Now in Default(en) format.
     * @param withTime boolean to check if you want the hours and minutes in result string.
     * @return String with date time now in default(en) format.
     */
    public static String getDateTimeNow(boolean withTime) {
        DateFormat df = withTime ? new SimpleDateFormat("yyyy-MM-dd HH:mm") : new SimpleDateFormat("yyyy-MM-dd");
        return df.format(Calendar.getInstance().getTime());
    }

    /***
     * Get Date Time Now in Brazilian Format.
     * @param withTime boolean to check if you want the hours and minutes in result string.
     * @return String with date time now in pt-br format.
     */
    public static String getDateTimeNowPtBr(boolean withTime) {
        DateFormat df = withTime ? new SimpleDateFormat("dd/MM/yyyy HH:mm") : new SimpleDateFormat("dd/MM/yyyy");
        return df.format(Calendar.getInstance().getTime());
    }

    /***
     * This Method create and configure a new Progress Dialog.
     *
     * @param title Set the Title in the progress dialog.
     * @param message Set the Message in the progress dialog.
     * @param isIndeterminate Determine if the progress is indeterminate.
     * @param isCancelable Determine if progress is Cancelable.
     * @param context Context is needed to create a new progress dialog.
     * @return New ProgressDialog configured.
     */
    public static ProgressDialog configureProgressDialog(String title, String message, boolean isIndeterminate, boolean isCancelable, Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(isIndeterminate);
        progressDialog.setCancelable(isCancelable);
        return progressDialog;
    }

    public static String serialize(Serializable obj) throws IOException {
        if (obj == null) return "";
        try {
            ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
            ObjectOutputStream objStream = new ObjectOutputStream(serialObj);
            objStream.writeObject(obj);
            objStream.close();
            return encodeBytes(serialObj.toByteArray());
        } catch (Exception e) {
            //throw WrappedIOException.wrap("Serialization error: " + e.getMessage(), e);
            return null;
        }
    }

    public static Object deserialize(String str) throws IOException {
        if (str == null || str.length() == 0) return null;
        try {
            ByteArrayInputStream serialObj = new ByteArrayInputStream(decodeBytes(str));
            ObjectInputStream objStream = new ObjectInputStream(serialObj);
            return objStream.readObject();
        } catch (Exception e) {
            //throw WrappedIOException.wrap("Deserialization error: " + e.getMessage(), e);
            return null;
        }
    }

    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }

    public static byte[] decodeBytes(String str) {
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length(); i+=2) {
            char c = str.charAt(i);
            bytes[i/2] = (byte) ((c - 'a') << 4);
            c = str.charAt(i+1);
            bytes[i/2] += (c - 'a');
        }
        return bytes;
    }
}
