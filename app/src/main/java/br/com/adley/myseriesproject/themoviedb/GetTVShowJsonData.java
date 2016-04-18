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

import br.com.adley.myseriesproject.library.enums.DownloadStatus;
import br.com.adley.myseriesproject.library.GetRawData;
import br.com.adley.myseriesproject.tvmaze.TVMazeTVShowObject;
import br.com.adley.myseriesproject.library.Utils;

/**
 * Created by Adley.Damaceno on 15/04/2016.
 * TODO
 */
public class GetTVShowJsonData extends GetRawData{

    private String LOG_TAG = GetTVShowJsonData.class.getSimpleName();
    private List<TVMazeTVShowObject> tvshows;
    private Uri destinationUri;
    private Context context;

    public GetTVShowJsonData(String showToSearch, Context context) {
        super(null);
        tvshows = new ArrayList<>();
        createAndUpdateUri(showToSearch);
        this.context = context;
    }

    public void execute(){
        super.setRawUrl(destinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built Uri = "+ destinationUri.toString());
        downloadJsonData.execute(destinationUri.toString());
    }
    public boolean createAndUpdateUri(String showName) {
        final String BASE_URL_API_SEARCH = "http://api.themoviedb.org/3/search/tv";
        final String API_KEY_THEMOVIEDBKEY = Utils.getApiKey(context);
        final String API_KEY = "api_key_value";
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

    public  List<TVMazeTVShowObject> getTVShows(){
        return tvshows;
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
            if (jsonArray.length() == 0){
                Toast.makeText(context, "Nenhuma série encontrada", Toast.LENGTH_SHORT).show();
                return;
            }
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
                TVMazeTVShowObject TVMazeTvShowObjectObject = new TVMazeTVShowObject(id, url, name, type, language, status, imageMedium, imageOriginal, summary, previousEpisode, nextEpisode);
                this.tvshows.add(TVMazeTvShowObjectObject);
            }
            for (TVMazeTVShowObject singleShow: tvshows){
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
