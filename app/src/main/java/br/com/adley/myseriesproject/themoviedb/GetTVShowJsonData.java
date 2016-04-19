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
import java.util.StringTokenizer;

import br.com.adley.myseriesproject.library.enums.DownloadStatus;
import br.com.adley.myseriesproject.library.GetRawData;
import br.com.adley.myseriesproject.models.TVShow;
import br.com.adley.myseriesproject.tvmaze.TVMazeTVShowObject;
import br.com.adley.myseriesproject.library.Utils;

/**
 * Created by Adley.Damaceno on 15/04/2016.
 * TODO
 */
public class GetTVShowJsonData extends GetRawData{

    private String LOG_TAG = GetTVShowJsonData.class.getSimpleName();
    private List<TVShow> tvshows;
    private Uri destinationUri;
    private Context context;

    public GetTVShowJsonData(String showName, Context context) {
        super(null);
        tvshows = new ArrayList<>();
        createAndUpdateUri(showName);
        this.context = context;
    }

    public void execute(){
        super.setRawUrl(destinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        //Log.v(LOG_TAG, "Built Uri = "+ destinationUri.toString());
        downloadJsonData.execute(destinationUri.toString());
    }
    public boolean createAndUpdateUri(String showName) {
        final String BASE_URL_API_SEARCH = "http://api.themoviedb.org/3/search/tv";
        final String API_KEY_THEMOVIEDBKEY = "api_key";
        final String API_KEY = Utils.getApiKey(context);
        final String LANGUAGE_THEMOVIEDBKEY = "language";
        final String LANGUAGE = "pt-br";

        // Create HashMap with the query and values
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put(API_KEY_THEMOVIEDBKEY, API_KEY);
        queryParams.put(LANGUAGE_THEMOVIEDBKEY, LANGUAGE);
        queryParams.put("query", showName);

        // Generate final URI to use
        destinationUri = Utils.appendUri(BASE_URL_API_SEARCH,queryParams);
        return destinationUri != null;
    }

    public  List<TVShow> getTVShows(){
        return tvshows;
    }

    public void processResult() {
        if (getDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Error download raw file");
            return;
        }
        final String PAGE_SEARCH_TVSHOW = "page";
        final String RESULTS_SEARCH_TVSHOW = "results";
        final String TOTAL_RESULTS_SEARCH_TVSHOW = "total_results";
        final String POSTER_PATH_TVSHOW = "POSTER_PATH";
        final String POPULARITY_TVSHOW = "popu";
        final String ID_TVSHOW = "id";
        final String BACKDROP_PATH_TVSHOW = "backdrop_path";
        final String VOTE_AVERAGE_TVSHOW = "vote_average";
        final String OVERVIEW_TVSHOW = "overview";
        final String FIRST_AIR_DATE_TVSHOW = "first_air_date";
        final String ORIGINAL_LANGUAGE_TVSHOW = "original_language";
        final String VOTE_COUNT_TVSHOW = "vote_count";
        final String NAME_TVSHOW = "Arqueiro";
        final String ORIGINAL_NAME_TVSHOW = "Arrow";

        // TODO: Lists
        //final String ORIGIN_COUNTRY_TVSHOW = "origin_country";
        //final String GENRES_IDS = "genre_ids";

      /*  EXAMPLE JSON
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
            jsonObject.getString(RESULTS_SEARCH_TVSHOW);
            if (jsonObject.length() == 0){
                Toast.makeText(context, "Nenhuma série encontrada", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                JSONObject oneShowJsonObject = new JSONObject(jsonobject.toString());
                JSONObject showJSonObject = oneShowJsonObject.getJSONObject("show");
                String id = showJSonObject.getString(ID_TVSHOW);
                String url = showJSonObject.getString(URL_TVSHOW);
                String name = showJSonObject.getString(NAME_TVSHOW);
                String type = showJSonObject.getString(TYPE_TVSHOW);
                String language = showJSonObject.getString(LANGUAGE_TVSHOW);
                String status = showJSonObject.getString(STATUS_TVSHOW);
                String summary = showJSonObject.getString(SUMMARY_TVSHOW);

                // Images - Posters
                String imageMedium;
                String imageOriginal;
                try {
                    JSONObject jsonImageData = showJSonObject.getJSONObject(IMAGE_TVSHOW);
                    try {
                        imageMedium = jsonImageData.getString(IMAGE_MEDIUM_TVSHOW);
                    } catch (JSONException e) {
                        imageMedium = null;
                        Log.i(LOG_TAG, "The Show \"" + name + "\" does not have medium image. The error is: " + e.getMessage());
                    }
                    try {
                        imageOriginal = jsonImageData.getString(IMAGE_ORIGINAL_TVSHOW);
                    } catch (JSONException e) {
                        imageOriginal = null;
                        Log.i(LOG_TAG, "The Show \"" + name + "\" does not have original image. The error is: " + e.getMessage());
                    }
                }catch (JSONException e){
                    imageMedium = imageOriginal = null;
                    Log.i(LOG_TAG, "The Show \"" + name + "\" does not have any image. The error is: " + e.getMessage());
                }
                // Episodes
                JSONObject linkJsonData = showJSonObject.getJSONObject(LINKS_TVSHOW);
                String previousEpisode;
                String nextEpisode;
                try {
                    // Previous Episode
                    JSONObject previousEpisodeData = linkJsonData.getJSONObject(PREVIOUS_EPISODE_TVSHOW);
                    previousEpisode = previousEpisodeData.getString(HREF_EPISODES_TVSHOW);
                } catch (JSONException e){
                    previousEpisode = null;
                    Log.i(LOG_TAG, "The Show \""+name+"\" does not have previous episode. The error is: "+ e.getMessage());
                }
                try{
                    // Next Episode
                    JSONObject nextEpisodeData = linkJsonData.getJSONObject(NEXT_EPISODE_TVSHOW);
                    nextEpisode = nextEpisodeData.getString(HREF_EPISODES_TVSHOW);
                }catch (JSONException e){
                    nextEpisode = null;
                    Log.i(LOG_TAG, "The Show \""+name+"\" does not have next episode. The error is: "+ e.getMessage());
                }

                // Create TVMazeTVShowObject Object and add to the List of Shows
                TVShow tvShow = new TVShow();//id, url, name, type, language, status, imageMedium, imageOriginal, summary, previousEpisode, nextEpisode);
                this.tvshows.add(tvShow);
            }

            /*  --- LOG EACH SHOW FOUND -- */
            for (TVShow singleShow: tvshows){
                Log.v(LOG_TAG, singleShow.toString());
            }

        } catch (JSONException jsonError) {
            jsonError.printStackTrace();
            Log.e(LOG_TAG, "Error processing Json data");
        }

    }

    public class DownloadJsonData extends GetRawData.DownloadRawData {
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();
        }

        protected String doInBackground(String... params) {
            String[] par = {destinationUri.toString()};
            return super.doInBackground(par);
        }
    }

}
