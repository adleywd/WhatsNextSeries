package br.com.adley.whatsnextseries.library;

/**
 * Created by Adley on 07/05/2016.
 * This class should have all constants from this project.
 */
public class AppConsts {
    // Notification configs
    public final static String NOTIFICATION_HOUR_KEY = "notification_hours";
    public final static String NOTIFICATION_MINUTE_KEY = "notification_minutes";
    public final static String NOTIFICATION_ENABLED_KEY = "notification_enabled";

    // About app config
    public final static String[] CONTACT_EMAILS = {"whatsnextseries@gmail.com"};
    public final static String SITE_WHATSNEXT = "http://adley.com.br";
    public final static String SITE_THEMOVIEDB = "https://www.themoviedb.org";
    public final static String SITE_GITHUB_WHATSNEXT = "https://github.com/adleywd/WhatsNextSeries";
    public final static String LOCALE_PT_BR = "pt_BR";


    // Search Configs
    public final static String QUERY_NAME_LABEL = "query";
    public final static String LANGUAGE_DEFAULT_VALUE="pt-br";
    public final static String LANGUAGE_LABEL = "language";
    public final static String API_KEY_LABEL = "api_key";
    public final static String TIMEZONE_LABEL = "timezone";
    public final static String TIMEZONE_AMERICA_SP = "America/Sao_Paulo";
    public final static String TOOLBAR_SEARCH_QUERY = "toolbar_search_query";

    //Configs (Preferences)
    public final static String LANGUAGE_USE_PTBR = "language_use_ptbr";
    public final static String AUTO_LOAD_AIR_TODAY = "auto_load_air_today";

    //Home
    public final static String PREFIX_IMG_DIMENSION_FAVORITES = "w92";
    public final static String FAVORITES_SHAREDPREFERENCES_KEY = "favorites";
    public final static String FAVORITES_SHAREDPREFERENCES_DELIMITER = "&";
    public final static int TIME_INTERVAL_CLOSE_APP = 2000;

    //BaseActivity
    public final static String TVSHOW_TRANSFER = "TVSHOW_TRANSFER";
    public final static String TVSHOW_TITLE = "TVSHOW_TITLE";

    //TVShow
    public final static String PREFIX_IMG_LINK = "http://image.tmdb.org/t/p/";
    public final static String PREFIX_IMG_LINK_BACKDROP = "http://image.tmdb.org/t/p/";
    public final static String POSTER_DEFAULT_SIZE = "w342";
    public final static String BACKDROP_DEFAULT_SIZE = "w780";

    //Favorites Columns
    public final static int FAVORITES_PORTRAIT_TABLET = 2;
    public final static int FAVORITES_LANDSCAPE_TABLET = 3;
    public final static int FAVORITES_PORTRAIT_PHONE = 1;
    public final static int FAVORITES_LANDSCAPE_PHONE = 2;

    //AiringToday Config
    // Columns
    public final static int AIRTODAY_PORTRAIT_TABLET = 3;
    public final static int AIRTODAY_LANDSCAPE_TABLET = 5;
    public final static int AIRTODAY_PORTRAIT_PHONE = 2;
    public final static int AIRTODAY_LANDSCAPE_PHONE = 4;
    //Configs
    public final static String PAGE_KEY_NAME = "page";

    /***
     * GetTVShowDetailsJsonData
     */
    // Show Labels

/*  This items was already added to project.
    public final static String PAGE_SEARCH_TVSHOW = "page";
    public final static String RESULTS_SEARCH_TVSHOW = "results";
    public final static String TOTAL_RESULTS_SEARCH_TVSHOW = "total_results";
    public final static String POSTER_PATH_TVSHOW = "poster_path";
    public final static String POPULARITY_TVSHOW = "popularity";
    public final static String ID_TVSHOW = "id";
    public final static String BACKDROP_PATH_TVSHOW = "backdrop_path";
    public final static String VOTE_AVERAGE_TVSHOW = "vote_average";
    public final static String OVERVIEW_TVSHOW = "overview";
    public final static String FIRST_AIR_DATE_TVSHOW = "first_air_date";
    public final static String ORIGINAL_LANGUAGE_TVSHOW = "original_language";
    public final static String VOTE_COUNT_TVSHOW = "vote_count";
    public final static String NAME_TVSHOW = "name";
    public final static String ORIGINAL_NAME_TVSHOW = "original_name";*/

    // TV static Show Details Labels and Consts
    public final static String HOMEPAGE_TVSHOWSDETAILS = "homepage";
    public final static String INPRODUCTION_TVSHOWDETAILS = "in_production";
    public final static String NUMBEROFEPISODES_TVSHOWDETAILS = "number_of_episodes";
    public final static String NUMBEROFSEASONS_TVSHOWDETAILS = "number_of_seasons";
    public final static String TYPE_TVSHOWDETAILS = "type";
    public final static String MAX_VALUE_RATING_TVSHOWDETAILS = "10.0";
    public final static String SEASON_TVSHOWDETAILS = "seasons";
    public final static String SEASON_NUMBER_TVSHOWDETAILS = "season_number";
    public final static String SHOW_ID_INTENT = "show_id";
    public final static String SEASON_NUMBER_INTENT = "season_number";

    /***
     * GetTVShowJsonData
     */

    public final static String PAGE_SEARCH_TVSHOW = "page";
    public final static String RESULTS_SEARCH_TVSHOW = "results";
    public final static String TOTAL_RESULTS_SEARCH_TVSHOW = "total_results";
    public final static String POSTER_PATH_TVSHOW = "poster_path";
    public final static String POPULARITY_TVSHOW = "popularity";
    public final static String ID_TVSHOW = "id";
    public final static String BACKDROP_PATH_TVSHOW = "backdrop_path";
    public final static String VOTE_AVERAGE_TVSHOW = "vote_average";
    public final static String OVERVIEW_TVSHOW = "overview";
    public final static String FIRST_AIR_DATE_TVSHOW = "first_air_date";
    public final static String ORIGINAL_LANGUAGE_TVSHOW = "original_language";
    public final static String VOTE_COUNT_TVSHOW = "vote_count";
    public final static String NAME_TVSHOW = "name";
    public final static String ORIGINAL_NAME_TVSHOW = "original_name";
    public final static String ORIGIN_COUNTRY_TVSHOW = "origin_country";
    public final static String GENRES_IDS = "genre_ids";
    public final static String PAGE_NUMBER = "page";
    public final static String TOTAL_SHOWS_NUMBER = "total_results";
    public final static String TOTAL_PAGES_NUMBER = "total_pages";

    /**
     * GetTVShowSeasonJsonData
     */

     /* Season Labels */
    public final static String AIR_DATE_SEASON = "air_date";
    public final static String NAME_SEASON = "name";
    public final static String OVERVIEW_SEASON = "overview";
    public final static String ID_SEASON = "id";
    public final static String NUMBER_SEASON = "season_number";
    public final static String EPISODES_SEASON = "episodes";
    // Im staticage from season
    public final static String POSTER_PATH_SEASON = "poster_path";
    /* Ep staticisode Labels */
    public final static String AIR_DATE_EPISODE = "air_date";
    //fin statical String CREW_EPISODE = "crew";
    //fin statical String GUEST_STARTS_EPISODE = "guest_Stars";
    public final static String NUMBER_EPISODE = "episode_number";
    public final static String NAME_EPISODE = "name";
    public final static String OVERVIEW_EPISODE = "overview";
    public final static String ID_EPISODE = "id";
    public final static String PRODUCTION_CODE_EPISODE = "production_code";
    public final static String SEASON_NUMBER_EPISODE = "season_number";
    public final static String VOTE_AVERAGE_EPISODE = "vote_average";
    public final static String VOTE_COUNT_EPISODE = "vote_count";
    // Im staticage from episode
    public final static String STILL_PATH_EPISODE = "still_path";

    public final static String POSTER_KEY_NAME = "poster";
    public final static String BACKDROP_KEY_NAME = "backdrop";
}
