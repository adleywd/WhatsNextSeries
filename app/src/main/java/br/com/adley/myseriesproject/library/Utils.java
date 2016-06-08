package br.com.adley.myseriesproject.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
 * The methods in this class are static.
 * To call a static method declare the class first.
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
     * Convert a Date inside a String to local format, based on strings.xml file.
     *
     * @param date String date that you want to change format.
     * @return The new String formatted to Pt-Br format.
     * @throws ParseException
     */
    public static String convertStringToStringDate(String date, Context context) throws ParseException {
        SimpleDateFormat fromApi = new SimpleDateFormat(context.getString(R.string.date_format_from_api));
        SimpleDateFormat newFormat = new SimpleDateFormat(context.getString(R.string.date_format_local));
        return fromApi == newFormat ? date : newFormat.format(fromApi.parse(date));
    }

    /***
     * @param date Date to convert to DateTime.
     * @return Converted Date.
     */
    public static Date convertStringDateToDate(String date, Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.date_format_local));
        try {
            return dateFormat.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
            return new Date();
        }
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
     * Get Date Time Now in local format in strings.xml file.
     *
     * @param withTime boolean to check if you want the hours and minutes in result string.
     * @return String with date time now in pt-br format.
     */
    public static String getDateTimeNow(boolean withTime, Context context) {
        DateFormat df = withTime ? new SimpleDateFormat(context.getString(R.string.date_time_format_local))
                : new SimpleDateFormat(context.getString(R.string.date_format_local));
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
     * @param delimiter The delimiter, like: ", or @". Examples: [2,3,4] or [2@3@4]
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

    /***
     * @param delimiter The delimiter that you used in conversion List to String
     * @param values    String content converted by a list converter
     * @return Return list that contains all values formatted as String.
     */
    public static List<String> convertStringToList(String delimiter, String values) {
        return new ArrayList<>(Arrays.asList(values.split(delimiter)));
    }

    /***
     * @param delimiter The delimiter that you used in conversion List to String
     * @param values    String content converted by a list converter
     * @return Return list that contains all values formatted as Integer.
     */
    public static List<Integer> convertStringToIntegerList(String delimiter, String values) {
        if(delimiter != null && values != null){
            List<String> arrayList = new ArrayList<>(Arrays.asList(values.split(delimiter)));
            List<Integer> resultList = new ArrayList<>();
            for (String value : arrayList) {
                resultList.add(Integer.parseInt(value));
            }
            return resultList;
        }else{
            return new ArrayList<Integer>();
        }
    }

    /***
     * Check if a list contains specific value.
     *
     * @param list         List to check the value.
     * @param valueToCheck The value to check if it's in the list.
     * @return Return true if the List has the Value.
     */
    public static boolean checkItemInIntegerList(List<?> list, Object valueToCheck) {
        return list.contains(valueToCheck);
    }

    /***
     * Remove a int value from a List of Integers (List<integer>).
     *
     * @param list          The list that you will take out the int value.
     * @param valueToRemove The int value that you want to remove from the list.
     * @return Return list without the value passed.
     */
    public static List<Integer> removeIntegerItemFromList(List<Integer> list, int valueToRemove) {
        List<Integer> resultList = new ArrayList<>();
        for (int item : list) {
            if (item != valueToRemove) resultList.add(item);
        }
        return resultList;
    }

    /***
     * Set the next episode for a specific show.
     *
     * @param season        - The season of the episode( almost always the last season).
     * @param tvShowDetails The Show that contains the season.
     * @param context       The context where the method was called.
     */
    public static void setNextEpisode(TVShowSeasons season, TVShowDetails tvShowDetails, Context context) {
        if (tvShowDetails.getNumberOfSeasons() > 0) {
            try {
                TVShowSeasonEpisodes lastSeasonEpisode = null;
                SimpleDateFormat sdfPtBr = new SimpleDateFormat(context.getString(R.string.date_format_local));
                Date dateTimeNow = sdfPtBr.parse(Utils.getDateTimeNow(false, context));
                List<TVShowSeasonEpisodes> lastSeasonEpisodes = season.getEpisodes();
                for (TVShowSeasonEpisodes episode : lastSeasonEpisodes) {
                    if (episode.getAirDate() == null) {
                        continue;
                    }
                    Date episodeAirDate = sdfPtBr.parse(Utils.convertStringToStringDate(episode.getAirDate(), context));
                    if (episodeAirDate.after(dateTimeNow) || episodeAirDate.equals(dateTimeNow)) {
                        lastSeasonEpisode = episode;
                        break;
                    }
                }
                if (lastSeasonEpisode != null) {
                    if (tvShowDetails.getInProduction()) {
                        String episodeName = lastSeasonEpisode.getEpisodeName().isEmpty() ? context.getString(R.string.ep_without_name) : lastSeasonEpisode.getEpisodeName();
                        String episodeDate = Utils.convertStringToStringDate(lastSeasonEpisode.getAirDate(), context);
                        String episodeNumber = lastSeasonEpisode.getEpisodeNumber() == 0 ? "" : String.valueOf(lastSeasonEpisode.getEpisodeNumber());
                        tvShowDetails.setNextEpisode(context.getString(R.string.data_name_input_show, episodeNumber, episodeName, episodeDate));
                        tvShowDetails.setNextEpisodeDate(episodeDate);
                        tvShowDetails.setNextEpisodeName(episodeName);
                        tvShowDetails.setNextEpisodeNumber(episodeNumber);
                    } else {
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

    /***
     * Return a list of null items in the List
     *
     * @param tvShowDetailsList List of tv shows details.
     * @return Return list of null items in the List
     */
    public static List<TVShowDetails> listNullDateNextEpisode(List<TVShowDetails> tvShowDetailsList) {
        List<TVShowDetails> tvShowNullNextEpDate = new ArrayList<>();
        for (TVShowDetails show :
                tvShowDetailsList) {
            if (show.getNextEpisodeDate() == null) tvShowNullNextEpDate.add(show);
        }
        return tvShowNullNextEpDate;
    }

    /***
     * Return a list of items not null in the List
     *
     * @param tvShowDetailsList List of tv shows details.
     * @return Return a list of items not null in the List
     */
    public static List<TVShowDetails> listContainDateNextEpisode(List<TVShowDetails> tvShowDetailsList) {
        List<TVShowDetails> tvShowNullNextEpDate = new ArrayList<>();
        for (TVShowDetails show :
                tvShowDetailsList) {
            if (show.getNextEpisodeDate() != null) tvShowNullNextEpDate.add(show);
        }
        return tvShowNullNextEpDate;
    }

    /***
     * Sort a tv show list.
     *
     * @param tvShowDetails List of TvShowsDetails to sort it.
     * @return List of TVShowDetails ordered by date
     */
    public static List<TVShowDetails> orderShowByNextDate(List<TVShowDetails> tvShowDetails, final Context context) {
        List<TVShowDetails> tvShowNullList = listNullDateNextEpisode(tvShowDetails);
        List<TVShowDetails> tvShowWithDateList = listContainDateNextEpisode(tvShowDetails);

        Collections.sort(tvShowWithDateList, new Comparator<TVShowDetails>() {
            @Override
            public int compare(TVShowDetails show1, TVShowDetails show2) {
                if (show1.getNextEpisodeDate() != null && show2.getNextEpisodeDate() != null) {
                    return convertStringDateToDate(show1.getNextEpisodeDate(), context)
                            .compareTo(convertStringDateToDate(show2.getNextEpisodeDate(), context));
                } else {
                    return 0;
                }
            }
        });

        List<TVShowDetails> tvShowOrderResult = tvShowWithDateList;
        for (TVShowDetails show :
                tvShowNullList) {
            tvShowOrderResult.add(show);
        }
        return tvShowOrderResult;
    }

    /***
     * Create a snackbar based on view attribute.
     *
     * @param color Message text Color
     * @param message Text from snackbar
     * @param view View where the snackbar is showed
     */
    public static void createSnackbar(int color, String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textViewSnackbar = (TextView) snackbarView.findViewById(snackbarTextId);
        textViewSnackbar.setTextColor(color);
        snackbar.show();
    }
}
