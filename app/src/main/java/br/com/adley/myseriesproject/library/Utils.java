package br.com.adley.myseriesproject.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.view.View;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.models.TVShowDetails;
import br.com.adley.myseriesproject.models.TVShowSeasonEpisodes;
import br.com.adley.myseriesproject.models.TVShowSeasons;

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
     *
     * @param uri         Base Uri to append with the params.
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
     *
     * @param context Context where you will use the method.
     * @return String containing the api key
     */
    public static String getApiKey(Context context) {
        return context.getString(R.string.api_key);
    }

    /***
     * Check if have any connection (wifi / 3g / 4g).
     *
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
     *
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
     *
     * @param view View that you want to set invisible.
     */
    public static void setLayoutInvisible(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    /***
     * Set a layout visible.
     *
     * @param view View that you want to set visible.
     */
    public static void setLayoutVisible(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /***
     * Get Date Time Now in Default(en) format.
     *
     * @param withTime boolean to check if you want the hours and minutes in result string.
     * @return String with date time now in default(en) format.
     */
    public static String getDateTimeNow(boolean withTime) {
        DateFormat df = withTime ? new SimpleDateFormat("yyyy-MM-dd HH:mm") : new SimpleDateFormat("yyyy-MM-dd");
        return df.format(Calendar.getInstance().getTime());
    }

    /***
     * Get Date Time Now in Brazilian Format.
     *
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
     * @param title           Set the Title in the progress dialog.
     * @param message         Set the Message in the progress dialog.
     * @param isIndeterminate Determine if the progress is indeterminate.
     * @param isCancelable    Determine if progress is Cancelable.
     * @param context         Context is needed to create a new progress dialog.
     * @return New ProgressDialog configured.
     */
    public static ProgressDialog configureProgressDialog(String title, String message, boolean isIndeterminate, boolean isCancelable, Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(isIndeterminate);
        progressDialog.setCancelable(isCancelable);
        return progressDialog;
    }

    /***
     * Convert a list to string
     *
     * @param delimiter The delimiter, like: [2,3,4] or [2@3@4]
     * @param values    The list you want to convert
     * @return String that contains all objects inside the list separated by the delimiter.
     */
    public static String convertListToString(String delimiter, List<?> values) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object value : values) {
            stringBuilder.append(value);
            stringBuilder.append(delimiter);
        }

        String result = stringBuilder.toString();
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    public static List<String> convertStringToList(String delimiter, String values) {
        return new ArrayList<>(Arrays.asList(values.split(delimiter)));
    }

    public static List<Integer> convertStringToIntegerList(String delimiter, String values) {
        List<String> arrayList = new ArrayList<>(Arrays.asList(values.split(delimiter)));
        List<Integer> resultList = new ArrayList<>();
        for (String value : arrayList) {
            resultList.add(Integer.parseInt(value));
        }
        return resultList;
    }

    public static boolean checkItemInIntegerList(List<?> list, Object valueToCheck) {
        return list.contains(valueToCheck);
    }

    public static List<Integer> removeIntegerItemFromList(List<Integer> list, int valueToRemove) {
        List<Integer> resultList = new ArrayList<>();
        for (int item : list) {
            if (item != valueToRemove) resultList.add(item);
        }
        return resultList;
    }

    public static void setNextEpisode(TVShowSeasons season, TVShowDetails tvShowDetails, Context context) {
        if (tvShowDetails.getNumberOfSeasons() > 0) {
            try {
                TVShowSeasonEpisodes lastSeasonEpisode = null;
                SimpleDateFormat sdfPtBr = new SimpleDateFormat("dd/MM/yyyy");
                Date dateTimeNow = sdfPtBr.parse(Utils.getDateTimeNowPtBr(false));
                List<TVShowSeasonEpisodes> lastSeasonEpisodes = season.getEpisodes();
                for (TVShowSeasonEpisodes episode : lastSeasonEpisodes) {
                    if (episode.getAirDate() == null) {
                        continue;
                    }
                    Date episodeAirDate = sdfPtBr.parse(Utils.convertStringDateToPtBr(episode.getAirDate()));
                    if (episodeAirDate.after(dateTimeNow) || episodeAirDate.equals(dateTimeNow)) {
                        lastSeasonEpisode = episode;
                        break;
                    }
                }
                if (lastSeasonEpisode != null) {
                    if (tvShowDetails.getInProduction()) {
                        String episodeName = lastSeasonEpisode.getEpisodeName().isEmpty()? "Não sabemos o nome":lastSeasonEpisode.getEpisodeName();
                        String episodeDate = Utils.convertStringDateToPtBr(lastSeasonEpisode.getAirDate());
                        tvShowDetails.setNextEpisode(context.getString(R.string.data_name_input_show, episodeName, episodeDate));
                    }else{
                        // Informa que não está mais em produção. getInProduction = false
                        tvShowDetails.setNextEpisode(context.getString(R.string.no_more_in_production));
                    }
                } else {
                    if (tvShowDetails.getInProduction()) {
                        tvShowDetails.setNextEpisode(context.getString(R.string.no_next_episode_info));
                    } else {
                        // Informa que não está mais em produção. getInProduction = false
                        tvShowDetails.setNextEpisode(context.getString(R.string.no_more_in_production));
                    }
                }
            } catch (ParseException e) {
                tvShowDetails.setNextEpisode(context.getString(R.string.error_generic_message));
            }
        } else {
            tvShowDetails.setNextEpisode(context.getString(R.string.warning_no_next_episode));
        }
    }
}
