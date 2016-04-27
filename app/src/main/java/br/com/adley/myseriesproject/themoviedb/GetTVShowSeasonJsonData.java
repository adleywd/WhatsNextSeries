package br.com.adley.myseriesproject.themoviedb;

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
import br.com.adley.myseriesproject.library.GetRawData;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.library.enums.DownloadStatus;
import br.com.adley.myseriesproject.models.TVShowSeasonEpisodes;
import br.com.adley.myseriesproject.models.TVShowSeasons;

/**
 * Created by Adley on 25/04/2016.
 * Get TVShow Detailed JSON
 * Download, parse, get items from json and convert to Java Object.
 */
public class GetTVShowSeasonJsonData extends GetRawData {

    private String LOG_TAG = GetTVShowSeasonJsonData.class.getSimpleName();
    private Uri mDestinationUri;
    private Context mContext;
    private int mIdTVSshow;
    private int mSeasonNumberTVShow;
    private TVShowSeasons mTVShowSeasons;
    private List<TVShowSeasonEpisodes> mEpisodes;

    public List<TVShowSeasonEpisodes> getEpisodes() {
        return mEpisodes;
    }

    public TVShowSeasons getTVShowSeasons() {
        return mTVShowSeasons;
    }

    public GetTVShowSeasonJsonData(int idTVSshow, int seasonNumber, Context context) {
        super(null);
        this.mContext = context;
        this.mSeasonNumberTVShow = seasonNumber;
        this.mIdTVSshow = idTVSshow;
        this.mEpisodes = new ArrayList<>();
        createAndUpdateUri(idTVSshow, seasonNumber);
    }

    public void execute() {
        super.setRawUrl(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built Uri = " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());
    }

    public void createAndUpdateUri(int idTvShow, int seasonNumber) {
        final Uri BASE_URL_API_SEARCH = Uri.parse(mContext.getString(R.string.url_search_seasons, idTvShow, seasonNumber));
        final String API_KEY_THEMOVIEDBKEY = mContext.getString(R.string.api_key_label);
        final String API_KEY = Utils.getApiKey(mContext);

        // Create HashMap with the query and values
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put(API_KEY_THEMOVIEDBKEY, API_KEY);

        // Generate final URI to use
        mDestinationUri = Utils.appendUri(BASE_URL_API_SEARCH, queryParams);
    }

    //JSON EXAMPLE AT: http://api.themoviedb.org/3/tv/1412?api_key=###
    public void processResult() {
        if (getDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Error download raw file");
            return;
        }

        /* Season Labels */
        final String AIR_DATE_SEASON = "air_date";
        final String NAME_SEASON = "name";
        final String OVERVIEW_SEASON = "overview";
        final String ID_SEASON = "id";
        final String NUMBER_SEASON = "season_number";
        final String EPISODES_SEASON = "episodes";
        // Image from season
        final String POSTER_PATH_SEASON = "poster_path";

        /* Episode Labels */
        final String AIR_DATE_EPISODE = "air_date";
        //final String CREW_EPISODE = "crew";
        //final String GUEST_STARTS_EPISODE = "guest_Stars";
        final String NUMBER_EPISODE = "episode_number";
        final String NAME_EPISODE = "name";
        final String OVERVIEW_EPISODE = "overview";
        final String ID_EPISODE = "id";
        final String PRODUCTION_CODE_EPISODE = "production_code";
        final String SEASON_NUMBER_EPISODE = "season_number";
        final String VOTE_AVERAGE_EPISODE = "vote_average";
        final String VOTE_COUNT_EPISODE = "vote_count";
        // Image from episode
        final String STILL_PATH_EPISODE = "still_path";

        /* JSON Example */
        //http://api.themoviedb.org/3/tv/48866/season/3?api_key=###
        //TODO: Navigate in json
        try {
            // Navigate and parse the JSON Data
            JSONObject seasonJsonObject = new JSONObject(getData());
            if (seasonJsonObject.length() == 0) {
                Toast.makeText(mContext, "Nenhuma série encontrada", Toast.LENGTH_SHORT).show();
                return;
            }
            int seasonId = seasonJsonObject.getInt(ID_SEASON);
            String seasonAirDate = seasonJsonObject.getString(AIR_DATE_SEASON);
            int seasonNumber = seasonJsonObject.getInt(NUMBER_SEASON);
            String seasonName = seasonJsonObject.getString(NAME_SEASON);
            String seasonOverview = seasonJsonObject.getString(OVERVIEW_SEASON);
            //Image Poster Path
            String seasonPosterPath = seasonJsonObject.isNull(POSTER_PATH_SEASON) ?
                    null : seasonJsonObject.getString(POSTER_PATH_SEASON);

            // Get array of EPISODES and interact with it
            JSONArray resultsArray = seasonJsonObject.getJSONArray(EPISODES_SEASON);
            if (resultsArray.length() == 0) {
                Toast.makeText(mContext, "Nenhuma série encontrada", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject jsonobject = resultsArray.getJSONObject(i);
                JSONObject episodeJsonObject = new JSONObject(jsonobject.toString());
                //TODO: Lists of crew and guest stars
                //private List<Person> mCrew;
                //private List<Person> mGuestStars;
                String episodeAirDate = episodeJsonObject.getString(AIR_DATE_EPISODE);
                int episodeNumber = episodeJsonObject.getInt(NUMBER_EPISODE);
                String episodeName = episodeJsonObject.getString(NAME_EPISODE);
                String episodeOverview = episodeJsonObject.getString(OVERVIEW_EPISODE);
                int episodeId = episodeJsonObject.getInt(ID_EPISODE);
                String episodeProductionCode = episodeJsonObject.getString(PRODUCTION_CODE_EPISODE);
                int episodeSeasonNumber = episodeJsonObject.getInt(SEASON_NUMBER_EPISODE);
                float episodeVoteAverage = (float) episodeJsonObject.getDouble(VOTE_AVERAGE_EPISODE);
                float episodeVoteCount = (float) episodeJsonObject.getDouble(VOTE_COUNT_EPISODE);

                // Image for each episode
                String episodeStillPath = episodeJsonObject.isNull(STILL_PATH_EPISODE) ?
                        null : episodeJsonObject.getString(STILL_PATH_EPISODE);

                // Create TVShow Object and add to the List of Shows
                TVShowSeasonEpisodes episode = new TVShowSeasonEpisodes(episodeAirDate, episodeNumber, episodeName,
                        episodeOverview, episodeId, episodeProductionCode, episodeSeasonNumber,
                        episodeStillPath, episodeVoteAverage, episodeVoteCount);
                this.mEpisodes.add(episode);
            }

            /* Create Season*/
            mTVShowSeasons = new TVShowSeasons(mIdTVSshow, seasonId, seasonAirDate, seasonNumber, seasonName, seasonOverview, mEpisodes, seasonPosterPath);

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
