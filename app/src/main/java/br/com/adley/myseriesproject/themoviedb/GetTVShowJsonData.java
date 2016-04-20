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

import br.com.adley.myseriesproject.library.GetRawData;
import br.com.adley.myseriesproject.library.Utils;
import br.com.adley.myseriesproject.library.enums.DownloadStatus;
import br.com.adley.myseriesproject.models.TVShow;

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
        this.context = context;
        tvshows = new ArrayList<>();
        createAndUpdateUri(showName);
    }

    public void execute(){
        super.setRawUrl(destinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built Uri = "+ destinationUri.toString());
        downloadJsonData.execute(destinationUri.toString());
    }
    public boolean createAndUpdateUri(String showName) {
        final Uri BASE_URL_API_SEARCH = Uri.parse("http://api.themoviedb.org/3/search/tv");
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
            JSONArray resultsArray = jsonObject.getJSONArray(RESULTS_SEARCH_TVSHOW);
            if (resultsArray.length() == 0 || jsonObject.getInt(TOTAL_RESULTS_SEARCH_TVSHOW) == 0){
                Toast.makeText(context, "Nenhuma série encontrada", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject jsonobject = resultsArray.getJSONObject(i);
                JSONObject showJsonObject = new JSONObject(jsonobject.toString());
                String popularity = showJsonObject.getString(POPULARITY_TVSHOW);
                String id = showJsonObject.getString(ID_TVSHOW);
                String vote_average = showJsonObject.getString(VOTE_AVERAGE_TVSHOW);
                String overview = showJsonObject.getString(OVERVIEW_TVSHOW);
                String first_air_date = showJsonObject.getString(FIRST_AIR_DATE_TVSHOW);
                String original_language = showJsonObject.getString(ORIGINAL_LANGUAGE_TVSHOW);
                String vote_count = showJsonObject.getString(VOTE_COUNT_TVSHOW);
                String name = showJsonObject.getString(NAME_TVSHOW);
                String original_name = showJsonObject.getString(ORIGINAL_NAME_TVSHOW);

                // Images - Posters
                String poster_path = showJsonObject.getString(POSTER_PATH_TVSHOW);
                String backdrop_path = showJsonObject.getString(BACKDROP_PATH_TVSHOW);

                // Create TVShow Object and add to the List of Shows
                TVShow tvShow = new TVShow(popularity, id, vote_average, overview, first_air_date, name,
                        original_name, original_language, vote_count, poster_path, backdrop_path);
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
