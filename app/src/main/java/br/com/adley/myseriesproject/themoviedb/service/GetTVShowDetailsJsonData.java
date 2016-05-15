package br.com.adley.myseriesproject.themoviedb.service;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.AppConsts;
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

    private String LOG_TAG = GetTVShowDetailsJsonData.class.getSimpleName();
    private TVShowDetails mTVShowsDetails;
    private Uri mDestinationUri;
    private Context mContext;
    private TVShow mTVShow;
    private String mNewPrefixImg;

    public GetTVShowDetailsJsonData(TVShow tvShow, Context context) {
        super(null);
        this.mContext = context;
        this.mTVShow = tvShow;
        this.mNewPrefixImg = null;
        createAndUpdateUri(tvShow.getId());
    }

    public GetTVShowDetailsJsonData(int idShow, Context context) {
        super(null);
        this.mContext = context;
        this.mTVShow = null;
        this.mNewPrefixImg = null;
        createAndUpdateUri(idShow);
    }

    public GetTVShowDetailsJsonData(int idShow, String newPrefix, Context context) {
        super(null);
        this.mContext = context;
        this.mTVShow = null;
        this.mNewPrefixImg = newPrefix;

        createAndUpdateUri(idShow);
    }

    public void execute() {
        super.setRawUrl(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built Uri = " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());
    }

    public boolean createAndUpdateUri(int idTvShow) {
        final Uri BASE_URL_API_TVSHOWDETAILS = Uri.parse(mContext.getString(R.string.url_search_detailed, idTvShow));
        final String API_KEY_THEMOVIEDBKEY = mContext.getString(R.string.api_key_label);
        final String API_KEY = Utils.getApiKey(mContext);

        // Create HashMap with the query and values
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put(API_KEY_THEMOVIEDBKEY, API_KEY);

        // Generate final URI to use
        mDestinationUri = Utils.appendUri(BASE_URL_API_TVSHOWDETAILS, queryParams);
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



        // TODO: Lists of origin and genres.
        //final String ORIGIN_COUNTRY_TVSHOW = "origin_country";
        //final String GENRES_IDS = "genre_ids";

      /*  JSON RESULT EXAMPLE */
        //http://api.themoviedb.org/3/tv/1412?api_key=###

        try {
            // Navigate and parse the JSON Data
            JSONObject showJsonObject = new JSONObject(getData());

            if (mTVShow != null) {
                if (showJsonObject.length() == 0) {
                    Toast.makeText(mContext, "Nenhuma série encontrada", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if some field is empty and try to fill.
                if (mTVShow.getPopularity() == 0)
                    mTVShow.setPopularity((float) showJsonObject.getDouble(AppConsts.POPULARITY_TVSHOW));
                if (mTVShow.getVoteAverage() == 0)
                    mTVShow.setVoteAverage((float) showJsonObject.getDouble(AppConsts.VOTE_AVERAGE_TVSHOW));
                if (mTVShow.getOverview().isEmpty())
                    mTVShow.setOverview(showJsonObject.getString(AppConsts.OVERVIEW_TVSHOW));
                if (mTVShow.getFirstAirDate().isEmpty())
                    mTVShow.setFirstAirDate(showJsonObject.getString(AppConsts.FIRST_AIR_DATE_TVSHOW));
                if (mTVShow.getOriginalLanguage().isEmpty())
                    mTVShow.setOriginalLanguage(showJsonObject.getString(AppConsts.ORIGINAL_LANGUAGE_TVSHOW));
                if (mTVShow.getVoteCount() == 0)
                    mTVShow.setVoteCount(showJsonObject.getInt(AppConsts.VOTE_COUNT_TVSHOW));
                if (mTVShow.getName().isEmpty())
                    mTVShow.setName(showJsonObject.getString(AppConsts.NAME_TVSHOW));
                if (mTVShow.getOriginalName().isEmpty())
                    mTVShow.setOriginalName(showJsonObject.getString(AppConsts.ORIGINAL_NAME_TVSHOW));

                // Images - Posters
                if (mTVShow.getPosterPath() == null && (showJsonObject.get(AppConsts.POSTER_PATH_TVSHOW) != null && !showJsonObject.isNull(AppConsts.POSTER_PATH_TVSHOW)))
                    mTVShow.setPosterPath(showJsonObject.getString(AppConsts.POSTER_PATH_TVSHOW));
                if (mTVShow.getBackdropPath() == null && (showJsonObject.get(AppConsts.BACKDROP_PATH_TVSHOW) != null && !showJsonObject.isNull(AppConsts.BACKDROP_PATH_TVSHOW))) {
                    mTVShow.setBackdropPath(showJsonObject.getString(AppConsts.BACKDROP_PATH_TVSHOW));
                }

                // Create items for TVShowDetails
                String homepage = showJsonObject.getString(AppConsts.HOMEPAGE_TVSHOWSDETAILS);
                boolean inProduction = showJsonObject.getBoolean(AppConsts.INPRODUCTION_TVSHOWDETAILS);
                int numberOfEpisodes = showJsonObject.getInt(AppConsts.NUMBEROFEPISODES_TVSHOWDETAILS);
                int numberOfSeasons = showJsonObject.getInt(AppConsts.NUMBEROFSEASONS_TVSHOWDETAILS);
                String type_tvshow = showJsonObject.getString(AppConsts.TYPE_TVSHOWDETAILS);

                // Create TVShowDetails Object and add to the List of Shows
                mTVShowsDetails = new TVShowDetails(mTVShow, homepage, inProduction, numberOfEpisodes, numberOfSeasons, type_tvshow);

                /*  --- LOG TVSHOW -- */
                Log.v(LOG_TAG, mTVShowsDetails.toString());
            } else {
                if (showJsonObject.length() == 0) {
                    Toast.makeText(mContext, "Nenhuma série encontrada", Toast.LENGTH_SHORT).show();
                    return;
                }
                int id = showJsonObject.getInt(AppConsts.ID_TVSHOW);
                // Check if some field is empty and try to fill.
                float popularity = (float) showJsonObject.getDouble(AppConsts.POPULARITY_TVSHOW);
                float vote_average = (float) showJsonObject.getDouble(AppConsts.VOTE_AVERAGE_TVSHOW);
                int vote_count = showJsonObject.getInt(AppConsts.VOTE_COUNT_TVSHOW);
                String overview = showJsonObject.getString(AppConsts.OVERVIEW_TVSHOW);
                String first_air_date = showJsonObject.getString(AppConsts.FIRST_AIR_DATE_TVSHOW);
                String original_language = showJsonObject.getString(AppConsts.ORIGINAL_LANGUAGE_TVSHOW);
                String name = showJsonObject.getString(AppConsts.NAME_TVSHOW);
                String original_name = showJsonObject.getString(AppConsts.ORIGINAL_NAME_TVSHOW);

                // Images - Posters
                String poster_path = !showJsonObject.isNull(AppConsts.POSTER_PATH_TVSHOW) ? showJsonObject.getString(AppConsts.POSTER_PATH_TVSHOW) : null;
                String backdrop_path = !showJsonObject.isNull(AppConsts.BACKDROP_PATH_TVSHOW) ? showJsonObject.getString(AppConsts.BACKDROP_PATH_TVSHOW) : null;


                // Create items for TVShowDetails
                String homepage = showJsonObject.getString(AppConsts.HOMEPAGE_TVSHOWSDETAILS);
                boolean inProduction = showJsonObject.getBoolean(AppConsts.INPRODUCTION_TVSHOWDETAILS);
                int numberOfEpisodes = showJsonObject.getInt(AppConsts.NUMBEROFEPISODES_TVSHOWDETAILS);
                int numberOfSeasons = showJsonObject.getInt(AppConsts.NUMBEROFSEASONS_TVSHOWDETAILS);
                String type = showJsonObject.getString(AppConsts.TYPE_TVSHOWDETAILS);

                // Create TVShowDetails Object and add to the List of Shows
                if (mNewPrefixImg != null) {
                    mTVShowsDetails = new TVShowDetails(popularity, id, vote_average, overview, first_air_date, name,
                            original_name, original_language, vote_count, poster_path, backdrop_path, homepage,
                            inProduction, numberOfEpisodes, numberOfSeasons, type, mNewPrefixImg);
                } else {
                    mTVShowsDetails = new TVShowDetails(popularity, id, vote_average, overview, first_air_date, name,
                            original_name, original_language, vote_count, poster_path, backdrop_path, homepage,
                            inProduction, numberOfEpisodes, numberOfSeasons, type);
                }


                /*  --- LOG TVSHOW -- */
                Log.v(LOG_TAG, mTVShowsDetails.toString());
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