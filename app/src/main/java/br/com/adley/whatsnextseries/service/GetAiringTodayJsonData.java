package br.com.adley.whatsnextseries.service;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.GetRawData;
import br.com.adley.whatsnextseries.library.Utils;
import br.com.adley.whatsnextseries.library.enums.DownloadStatus;
import br.com.adley.whatsnextseries.models.TVShow;

/**
 * Created by Adley.Damaceno on 22/07/2016.
 * Download, parse, get items from shows airing today, in json format and convert to Java Object
 */
public class GetAiringTodayJsonData extends GetRawData{

    private String LOG_TAG = GetTVShowJsonData.class.getSimpleName();
    private List<TVShow> mTVShows;
    private Uri mDestinationUri;
    private Context mContext;
    private String mPosterSize;
    private String mBackDropSize;
    private int mPage;
    private int mTotalPages;
    private int mTotalResults;

    public GetAiringTodayJsonData(Context context, boolean searchInPtBr, String posterSize, String backDropSize, int page) {
        super(null);
        this.mContext = context;
        mPosterSize = posterSize;
        mBackDropSize = backDropSize;
        mTVShows = new ArrayList<>();
        mPage = page;
        createAndUpdateUri(searchInPtBr);
    }

    public void execute(){
        super.setRawUrl(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built Uri = " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());
    }

    public boolean createAndUpdateUri(boolean searchInPtBr){
        final Uri BASE_URL_API_SEARCH = Uri.parse(mContext.getString(R.string.url_airing_today));
        final String API_KEY_THEMOVIEDBKEY = AppConsts.API_KEY_LABEL;
        final String API_KEY = Utils.getApiKey(mContext);
        final String LANGUAGE_THEMOVIEDBKEY = AppConsts.LANGUAGE_LABEL;
        final String show_language = AppConsts.LANGUAGE_DEFAULT_VALUE;
        final String PAGE_LABEL = AppConsts.PAGE_KEY_NAME;

        // Create HashMap with the query and values
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put(API_KEY_THEMOVIEDBKEY, API_KEY);
        queryParams.put(PAGE_LABEL, Integer.toString(mPage));

        // Check if locale is pt_BR, if yes, set timezone to America/São Paulo
        Locale currentLocale = mContext.getResources().getConfiguration().locale;
        if (currentLocale.toString().equals(AppConsts.LOCALE_PT_BR)) {
            queryParams.put(AppConsts.TIMEZONE_LABEL, AppConsts.TIMEZONE_AMERICA_SP);
        }

        // Check if it will search in pt-br
        if (searchInPtBr) queryParams.put(LANGUAGE_THEMOVIEDBKEY, show_language);

        // Generate final URI to use
        mDestinationUri = Utils.appendUri(BASE_URL_API_SEARCH, queryParams);
        return mDestinationUri != null;
    }

    public List<TVShow> getTVShows() {
        return mTVShows;
    }

    public int getPage() {
        return mPage;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    /* Example
        {
          "page": 1,
          "results": [
            {
              "poster_path": "/qj3m22w9IjrXzZrVMHg8QLAQP3.jpg",
              "popularity": 9.466953,
              "id": 67014,
              "backdrop_path": null,
              "vote_average": 0,
              "overview": "University students Yoon Jin-Myung, Jung Ye-Eun, Song Ji-Won, Kang Yi-Na and Yoo Eun-Jae share a house. Jin-Myung is busy supporting herself financially and studying. She suffers from lack of sleep. Ye-Eun is devoted to her boyfriend, she is clear about what she likes or not. Ji-Won has a bright personality and likes to drink. Yi-Na is popular due to her beautiful appearance. Eun-Jae is timid, but she has an unique taste for men.",
              "first_air_date": "2016-07-22",
              "origin_country": [
                "KR"
              ],
              "genre_ids": [
                35
              ],
              "original_language": "ko",
              "vote_count": 0,
              "name": "Age of Youth",
              "original_name": "청춘시대"
            },
            {}
          ]
         }
         */
    public void processResult(){
        if (getDownloadStatus() != DownloadStatus.OK){
            Log.e(LOG_TAG, "Error download raw file");
        }

        try {
            // Navigate and parse the JSON Data
            JSONObject jsonObject = new JSONObject(getData());
            JSONArray resultsArray = jsonObject.getJSONArray(AppConsts.RESULTS_SEARCH_TVSHOW);
            if (resultsArray.length() == 0 || jsonObject.getInt(AppConsts.TOTAL_RESULTS_SEARCH_TVSHOW) == 0) {
                return;
            }
            mTotalPages = jsonObject.getInt(AppConsts.TOTAL_PAGES_NUMBER);
            mTotalResults = jsonObject.getInt(AppConsts.TOTAL_RESULTS_SEARCH_TVSHOW);
            mPage = jsonObject.getInt(AppConsts.PAGE_NUMBER);
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
                        original_name, original_language, vote_count, poster_path, backdrop_path, mPosterSize, mBackDropSize, mPage, mTotalResults, mTotalPages);
                this.mTVShows.add(tvShow);
            }

            /*  --- LOG EACH SHOW FOUND -- */
            for (TVShow singleShow : mTVShows) {
                Log.v(LOG_TAG, singleShow.toString());
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class DownloadJsonData extends GetRawData.DownloadRawData {
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();
        }

        protected String doInBackground(String... params) {
            if(mDestinationUri != null) {
                String[] par = {mDestinationUri.toString()};
                return super.doInBackground(par);
            }else{
                return null;
            }
        }
    }
}
