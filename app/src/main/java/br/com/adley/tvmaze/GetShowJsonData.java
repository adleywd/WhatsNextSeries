package br.com.adley.tvmaze;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.adley.library.DownloadStatus;
import br.com.adley.library.GetRawData;
import br.com.adley.library.TVShow;

/**
 * Created by Adley.Damaceno on 09/03/2016.
 * Download data from TvShow.
 */

public class GetShowJsonData extends GetRawData {

    private String LOG_TAG = GetShowJsonData.class.getSimpleName();
    private List<TVShow> tvshows;
    private Uri destinationUri;

    public GetShowJsonData(String searchCriteria) {
        super(null);
        createAndUpdateUri(searchCriteria);
    }

    public  void execute(){
        super.setRawUrl(destinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built Uri = "+ destinationUri.toString());
        //downloadJsonData.execute(destinationUri.toString());
    }
    public boolean createAndUpdateUri(String searchCriteria) {
        final String TVMAZE_API_BASE_URL = "http://api.tvmaze.com/search/shows";

        destinationUri = Uri.parse(TVMAZE_API_BASE_URL).buildUpon()
                .appendQueryParameter("q", searchCriteria).build();
        return destinationUri != null;
    }

    public void processResult() {
        if (getDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Error download raw file");
            return;
        }

        final String ID_TVSHOW = "id";
        final String URL_TVSHOW = "url";
        final String NAME_TVSHOW = "name";
        final String TYPE_TVSHOW = "type";
        final String LANGUAGE_TVSHOW = "language";
        final String STATUS_TVSHOW = "status";
        final String SUMMARY_TVSHOW = "summary";
        final String IMAGE_TVSHOW = "image";
        final String IMAGE_MEDIUM_TVSHOW = "medium";
        final String IMAGE_ORIGINAL_TVSHOW = "original";
        final String LINKS_TVSHOW = "_links";
        final String PREVIOUS_EPISODE_TVSHOW = "previousepisode";
        final String NEXT_EPISODE_TVSHOW = "nextepisode";
        final String HREF_EPISODES_TVSHOW = "href";

        try {
            // Navigate and parse the JSON Data
            JSONArray jsonArray = new JSONArray(getData());
            for (int i = 0; i < jsonArray.length(); i++) {
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
                JSONObject jsonImageData = showJSonObject.getJSONObject(IMAGE_TVSHOW);
                String imageMedium = jsonImageData.getString(IMAGE_MEDIUM_TVSHOW);
                String imageOriginal = jsonImageData.getString(IMAGE_ORIGINAL_TVSHOW);

                JSONObject linkJsonData = new JSONObject(LINKS_TVSHOW);
                // Previous Episode
                JSONObject previousEpisodeData = new JSONObject(PREVIOUS_EPISODE_TVSHOW);
                String previousEpisode = previousEpisodeData.getString(HREF_EPISODES_TVSHOW);

                // Next Episode
                JSONObject nextEpisodeData = new JSONObject(NEXT_EPISODE_TVSHOW);
                String nextEpisode = nextEpisodeData.getString(HREF_EPISODES_TVSHOW);

                // Create TVShow Object and add to the List of Shows
                TVShow TVShowObject = new TVShow(id, url, name, type, language, status, imageMedium, imageOriginal, summary, previousEpisode, nextEpisode);
                this.tvshows.add(TVShowObject);
            }
            for (TVShow singleShow: tvshows){
                Log.v(LOG_TAG, singleShow.toString());
            }

        } catch (JSONException jsonError) {
            jsonError.printStackTrace();
            Log.e(LOG_TAG, "Error processing Json data");
        }

    }

    public class DownloadJsonData extends DownloadRawData {
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();
        }

        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }
    }
}
