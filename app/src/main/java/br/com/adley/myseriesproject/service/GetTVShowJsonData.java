package br.com.adley.myseriesproject.service;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.AppConsts;
import br.com.adley.myseriesproject.library.GetRawData;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.library.enums.DownloadStatus;
import br.com.adley.myseriesproject.models.TVShow;

/**
 * Created by Adley.Damaceno on 15/04/2016.
 * Download, parse, get items from json and convert to Java Object.
 */
public class GetTVShowJsonData extends GetRawData {

    private String LOG_TAG = GetTVShowJsonData.class.getSimpleName();
    private List<TVShow> mTVShows;
    private Uri mDestinationUri;
    private Context mContext;
    private String mPosterSize;
    private String mBackDropSize;

    public GetTVShowJsonData(String showName, Context context, boolean searchInPtBr, String posterSize, String backDropSize) {
        super(null);
        this.mContext = context;
        mPosterSize = posterSize;
        mBackDropSize = backDropSize;
        mTVShows = new ArrayList<>();
        createAndUpdateUri(showName, searchInPtBr);
    }

    public void execute() {
        super.setRawUrl(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built Uri = " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());
    }

    public boolean createAndUpdateUri(String showName, boolean searchInPtBr) {
        final Uri BASE_URL_API_SEARCH = Uri.parse(mContext.getString(R.string.url_search));
        final String API_KEY_THEMOVIEDBKEY = AppConsts.API_KEY_LABEL;
        final String API_KEY = Utils.getApiKey(mContext);
        final String LANGUAGE_THEMOVIEDBKEY = AppConsts.LANGUAGE_LABEL;
        final String show_language = AppConsts.LANGUAGE_DEFAULT_VALUE;

        // Create HashMap with the query and values
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put(API_KEY_THEMOVIEDBKEY, API_KEY);

        // Check if it will search in pt-br
        if (searchInPtBr) queryParams.put(LANGUAGE_THEMOVIEDBKEY, show_language);
        queryParams.put(AppConsts.QUERY_NAME_LABEL, showName);

        // Generate final URI to use
        mDestinationUri = Utils.appendUri(BASE_URL_API_SEARCH, queryParams);
        return mDestinationUri != null;
    }

    public List<TVShow> getTVShows() {
        return mTVShows;
    }

    public void processResult() {
        if (getDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Error download raw file");
            return;
        }


        // TODO: Lists of origin and genres.

      /*  JSON RESULT EXAMPLE */
        /*
        {
        "page": 1,
        "results": [
            {
            "poster_path": "/thisPICTURElink312332.jpg",
            "popularity": 10.980402,
            "id": 1412,
            "backdrop_path": "/PICTURElink312332.jpg",
            "vote_average": 6.8,
            "overview": " some overview here.",
            "first_air_date": "2012-10-10",
            "original_language": "en",
            "vote_count": 287,
            "name": "Translated Name",
            "original_name": "SomeName"
            },
            {}
        }
        */

        try {
            // Navigate and parse the JSON Data
            JSONObject jsonObject = new JSONObject(getData());
            JSONArray resultsArray = jsonObject.getJSONArray(AppConsts.RESULTS_SEARCH_TVSHOW);
            if (resultsArray.length() == 0 || jsonObject.getInt(AppConsts.TOTAL_RESULTS_SEARCH_TVSHOW) == 0) {
                return;
            }
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject jsonobject = resultsArray.getJSONObject(i);
                JSONObject showJsonObject = new JSONObject(jsonobject.toString());
                float popularity = (float) showJsonObject.getDouble(AppConsts.POPULARITY_TVSHOW);
                int id = showJsonObject.getInt(AppConsts.ID_TVSHOW);
                float vote_average = (float) showJsonObject.getDouble(AppConsts.VOTE_AVERAGE_TVSHOW);
                String overview = showJsonObject.getString(AppConsts.OVERVIEW_TVSHOW);
                String first_air_date = showJsonObject.getString(AppConsts.FIRST_AIR_DATE_TVSHOW);
                String original_language = showJsonObject.getString(AppConsts.ORIGINAL_LANGUAGE_TVSHOW);
                int vote_count = showJsonObject.getInt(AppConsts.VOTE_COUNT_TVSHOW);
                String name = showJsonObject.getString(AppConsts.NAME_TVSHOW);
                String original_name = showJsonObject.getString(AppConsts.ORIGINAL_NAME_TVSHOW);

                // Images - Posters
                String poster_path = !showJsonObject.isNull(AppConsts.POSTER_PATH_TVSHOW) ? showJsonObject.getString(AppConsts.POSTER_PATH_TVSHOW) : null;
                String backdrop_path = !showJsonObject.isNull(AppConsts.BACKDROP_PATH_TVSHOW) ? showJsonObject.getString(AppConsts.BACKDROP_PATH_TVSHOW) : null;

                // Create TVShow Object and add to the List of Shows
                TVShow tvShow = new TVShow(popularity, id, vote_average, overview, first_air_date, name,
                        original_name, original_language, vote_count, poster_path, backdrop_path, mPosterSize, mBackDropSize);
                this.mTVShows.add(tvShow);
            }

            /*  --- LOG EACH SHOW FOUND -- */
            for (TVShow singleShow : mTVShows) {
                Log.v(LOG_TAG, singleShow.toString());
            }

        } catch (JSONException jsonError) {
            jsonError.printStackTrace();
            Log.e(LOG_TAG, "Error processing Json data");
            Toast.makeText(mContext, mContext.getString(R.string.error_get_json_data), Toast.LENGTH_SHORT).show();
        }

    }

    public class DownloadJsonData extends GetRawData.DownloadRawData {
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();
        }

        protected String doInBackground(String... params) {
            String[] par = {mDestinationUri.toString()};
            return super.doInBackground(par);
        }
    }

}
