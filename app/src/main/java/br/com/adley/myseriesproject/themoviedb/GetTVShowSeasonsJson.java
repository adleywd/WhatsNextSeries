package br.com.adley.myseriesproject.themoviedb;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.GetRawData;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.library.enums.DownloadStatus;
import br.com.adley.myseriesproject.models.TVShowSeasonEpisodes;
import br.com.adley.myseriesproject.models.TVShowSeasons;

/**
 * Created by Adley on 21/04/2016.
 * Get TVShow Season JSON
 * Download, parse, get items from json and convert to Java Object.
 */
public class GetTVShowSeasonsJson extends GetRawData {

    private String LOG_TAG = GetTVShowJsonData.class.getSimpleName();
    private Uri mDestinationUri;
    private Context mContext;
    private TVShowSeasons mTVShowSeasons;
    private List<TVShowSeasonEpisodes> mTVShowSeasonEpisodes;
    private int mNumSeasons;

    public GetTVShowSeasonsJson(int idTvShow, int numSeasons, Context context) {
        super(null);
        this.mContext = context;
        this.mNumSeasons = numSeasons;
        createAndUpdateUri(idTvShow, numSeasons);
    }

    public void execute() {
        super.setRawUrl(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built Uri = " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());
    }

    public boolean createAndUpdateUri(int idTvShow, int numSeasons) {
        final Uri BASE_URL_API_SEARCH = Uri.parse(mContext.getString(R.string.url_search_seasons, idTvShow, numSeasons));
        final String API_KEY_THEMOVIEDBKEY = mContext.getString(R.string.api_key_label);
        final String API_KEY = Utils.getApiKey(mContext);

        // Create HashMap with the query and values
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put(API_KEY_THEMOVIEDBKEY, API_KEY);

        // Generate final URI to use
        mDestinationUri = Utils.appendUri(BASE_URL_API_SEARCH, queryParams);
        return mDestinationUri != null;
    }

    public TVShowSeasons getTVShowSeasons() {
        return mTVShowSeasons;
    }

    //JSON EXAMPLE AT: http://api.themoviedb.org/3/tv/1412?api_key=###
    public void processResult() {
        if (getDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Error download raw file");
            return;
        }
        // Show Labels (keys)
        final String EPISODES_SEASON = "episodes";
        final String AIRDATE_DATE_SEASON = "air_date";
        final String NAME_SEASON = "name";
        final String OVERVIEW_SEASON = "overview";
        final String ID_SEASON = "id";
        final String POSTER_PATH_SEASON = "poster_path";
        final String NUMBER_SEASON = "season_number";

        // Episodes Labels (keys)
        final String AIR_DATE_EPISODE = "air_date";
        final String CREW_EPISODE = "crew";
        final String NUMBER_EPISODE = "episode_number";
        final String GUEST_STAR_EPISODE = "guest_stars";
        final String NAME_EPISODE = "name";
        final String OVERVIEW_EPISODE = "overview";
        final String ID_EPISODE = "id";
        final String PRODUCTION_CODE_EPISODE = "production_code";
        final String SEASON_NUMBER_EPISODE = "season_number";
        final String STILL_PATH_EPISODE = "still_path";
        final String VOTE_AVERAGE_EPISODE = "vote_average";
        final String VOTE_COUNT_EPISODE = "vote_count";

      /*  JSON RESULT EXAMPLE */
        //http://api.themoviedb.org/3/tv/870/season/1?api_key=###

        try {
            // Navigate and parse the JSON Data
            JSONObject jsonObject = new JSONObject(getData());
            int idSeason = jsonObject.getInt(ID_SEASON);
            String seasonAirDate = jsonObject.getString(AIRDATE_DATE_SEASON);
            int seasonNumber = jsonObject.getInt(NUMBER_SEASON);
            String seasonName = jsonObject.getString(NAME_SEASON).isEmpty() ? null : jsonObject.getString(NAME_SEASON);
            String seasonOverview = jsonObject.getString(OVERVIEW_SEASON).isEmpty() ? null : jsonObject.getString(OVERVIEW_SEASON);
            String seasonPosterPath = jsonObject.isNull(POSTER_PATH_SEASON) ? null : jsonObject.getString(POSTER_PATH_SEASON);
            JSONArray episodesArray = jsonObject.getJSONArray(EPISODES_SEASON);

            // Get Array of Episodes
            if (episodesArray.length() == 0) {
                Toast.makeText(mContext, "Nenhuma série encontrada", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < episodesArray.length(); i++) {
                JSONObject episodesArrayObject = episodesArray.getJSONObject(i);
                JSONObject episodeObject = new JSONObject(episodesArrayObject.toString());
                String airDate = episodeObject.getString(AIR_DATE_EPISODE);
                int episodeNumber = episodeObject.getInt(NUMBER_EPISODE);
                String episodeName = episodeObject.getString(NAME_EPISODE);
                String episodeOverview = episodeObject.getString(OVERVIEW_EPISODE);
                int episodeId = episodeObject.getInt(ID_EPISODE);
                int episodeProductionCode = episodeObject.getInt(PRODUCTION_CODE_EPISODE);
                int episodeSeasonNumber = episodeObject.getInt(SEASON_NUMBER_EPISODE);
                String episodeStillPath = episodeObject.getString(STILL_PATH_EPISODE);
                float episodeVoteAverage = (float) episodeObject.getDouble((VOTE_AVERAGE_EPISODE));
                float episodeVoteCount = (float) episodeObject.getDouble(VOTE_COUNT_EPISODE);

                // Create TVShow Object and add to the List of Shows
                TVShowSeasonEpisodes episode = new TVShowSeasonEpisodes(airDate, episodeNumber, episodeName,
                        episodeOverview, episodeId, episodeProductionCode, episodeSeasonNumber,
                        episodeStillPath, episodeVoteAverage, episodeVoteCount);
                this.mTVShowSeasonEpisodes.add(episode);
            }

            /*  --- LOG EACH SHOW FOUND -- */
            Log.v(LOG_TAG, mTVShowSeasons.toString());

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
