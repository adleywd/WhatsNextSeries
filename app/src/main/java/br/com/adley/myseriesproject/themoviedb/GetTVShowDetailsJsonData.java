package br.com.adley.myseriesproject.themoviedb;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.GetRawData;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.library.enums.DownloadStatus;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.models.TVShowDetails;

/**
 * Created by Adley on 20/04/2016.
 * Get TVShow Detailed JSON
 * Download, parse, get items from json and convert to Java Object.
 */
public class GetTVShowDetailsJsonData extends GetRawData {

    private String LOG_TAG = GetTVShowJsonData.class.getSimpleName();
    private TVShowDetails mTVShowsDetails;
    private Uri mDestinationUri;
    private Context mContext;
    private TVShow mTVShow;

    public GetTVShowDetailsJsonData(int idTvShow, TVShow tvShow, Context context) {
        super(null);
        this.mContext = context;
        this.mTVShow = tvShow;
        createAndUpdateUri(idTvShow);
    }

    public void execute() {
        super.setRawUrl(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built Uri = " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());
    }

    public boolean createAndUpdateUri(int idTvShow) {
        final Uri BASE_URL_API_SEARCH = Uri.parse(mContext.getString(R.string.url_search_detailed) + idTvShow);
        final String API_KEY_THEMOVIEDBKEY = mContext.getString(R.string.api_key_label);
        final String API_KEY = Utils.getApiKey(mContext);

        // Create HashMap with the query and values
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put(API_KEY_THEMOVIEDBKEY, API_KEY);

        // Generate final URI to use
        mDestinationUri = Utils.appendUri(BASE_URL_API_SEARCH, queryParams);
        return mDestinationUri != null;
    }

    public TVShowDetails getTVShowsDetails() {
        return mTVShowsDetails;
    }

    //JSON EXAMPLE AT: http://api.themoviedb.org/3/tv/1412?api_key=###
    public void processResult() {
        if (getDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Error download raw file");
            return;
        }
        // Show Labels
        final String PAGE_SEARCH_TVSHOW = "page";
        final String RESULTS_SEARCH_TVSHOW = "results";
        final String TOTAL_RESULTS_SEARCH_TVSHOW = "total_results";
        final String POSTER_PATH_TVSHOW = "poster_path";
        final String POPULARITY_TVSHOW = "popularity";
        final String ID_TVSHOW = "id";
        final String BACKDROP_PATH_TVSHOW = "backdrop_path";
        final String VOTE_AVERAGE_TVSHOW = "vote_average";
        final String OVERVIEW_TVSHOW = "overview";
        final String FIRST_AIR_DATE_TVSHOW = "first_air_date";
        final String ORIGINAL_LANGUAGE_TVSHOW = "original_language";
        final String VOTE_COUNT_TVSHOW = "vote_count";
        final String NAME_TVSHOW = "name";
        final String ORIGINAL_NAME_TVSHOW = "original_name";

        // TV Show Details Labels
        final String HOMEPAGE_TVSHOWSDETAILS = "homepage";
        final String INPRODUCTION_TVSHOWDETAILS = "in_production";
        final String NUMBEROFEPISODES_TVSHOWDETAILS = "number_of_episodes";
        final String NUMBEROFSEASONS_TVSHOWDETAILS = "number_of_seasons";
        final String TYPE_TVSHOWDETAILS = "type";


        // TODO: Lists of origin and genres.
        //final String ORIGIN_COUNTRY_TVSHOW = "origin_country";
        //final String GENRES_IDS = "genre_ids";

      /*  JSON RESULT EXAMPLE */
        //http://api.themoviedb.org/3/tv/1412?api_key=###

        try {
            // Navigate and parse the JSON Data
            JSONObject showJsonObject = new JSONObject(getData());
            if (showJsonObject.length() == 0) {
                Toast.makeText(mContext, "Nenhuma série encontrada", Toast.LENGTH_SHORT).show();
                return;
            }
            // Check if some field is empty and try to fill.
            if (mTVShow.getPopularity() == 0)
                mTVShow.setPopularity((float) showJsonObject.getDouble(POPULARITY_TVSHOW));
            if (mTVShow.getVoteAverage() == 0)
                mTVShow.setVoteAverage((float) showJsonObject.getDouble(VOTE_AVERAGE_TVSHOW));
            if (mTVShow.getOverview().isEmpty())
                mTVShow.setOverview(showJsonObject.getString(OVERVIEW_TVSHOW));
            if (mTVShow.getFirstAirDate().isEmpty())
                mTVShow.setFirstAirDate(showJsonObject.getString(FIRST_AIR_DATE_TVSHOW));
            if (mTVShow.getOriginalLanguage().isEmpty())
                mTVShow.setOriginalLanguage(showJsonObject.getString(ORIGINAL_LANGUAGE_TVSHOW));
            if (mTVShow.getVoteCount() == 0)
                mTVShow.setVoteCount(showJsonObject.getInt(VOTE_COUNT_TVSHOW));
            if (mTVShow.getName().isEmpty()) mTVShow.setName(showJsonObject.getString(NAME_TVSHOW));
            if (mTVShow.getOriginalName().isEmpty())
                mTVShow.setOriginalName(showJsonObject.getString(ORIGINAL_NAME_TVSHOW));

            // Images - Posters
            if (mTVShow.getPosterPath() == null && showJsonObject.get(POSTER_PATH_TVSHOW) != null)
                mTVShow.setPosterPath(showJsonObject.getString(POSTER_PATH_TVSHOW));
            if (mTVShow.getBackdropPath() == null && showJsonObject.get(BACKDROP_PATH_TVSHOW) != null) {
                mTVShow.setBackdropPath(showJsonObject.getString(BACKDROP_PATH_TVSHOW));
            }

            // Create items for TVShowDetails
            String homepage = showJsonObject.getString(HOMEPAGE_TVSHOWSDETAILS);
            String inProduction = showJsonObject.getString(INPRODUCTION_TVSHOWDETAILS);
            int numberOfEpisodes = showJsonObject.getInt(NUMBEROFEPISODES_TVSHOWDETAILS);
            int numberOfSeasons = showJsonObject.getInt(NUMBEROFSEASONS_TVSHOWDETAILS);
            String type_tvshow = showJsonObject.getString(TYPE_TVSHOWDETAILS);

            // Create TVShowDetails Object and add to the List of Shows
            mTVShowsDetails = new TVShowDetails(mTVShow, homepage, inProduction, numberOfEpisodes, numberOfSeasons, type_tvshow);

                /*  --- LOG TVSHOWD -- */
            Log.v(LOG_TAG, mTVShowsDetails.toString());

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