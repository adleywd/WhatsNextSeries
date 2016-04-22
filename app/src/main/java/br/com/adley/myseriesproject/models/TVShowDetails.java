package br.com.adley.myseriesproject.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Adley on 20/04/2016.
 * Base model for TVShowDetails.
 * Creates a new TVShow, but with more fields.
 */
public class TVShowDetails extends TVShow implements Serializable {

    private static final long serialVersionUID = 1L;

    // Model from details
    private String mHomepage;
    private String mInProduction;
    private int mNumberOfEpisodes;
    private int mNumberOfSeasons;
    private String mType;
    private List<TVShowSeasons> mTVShowSeasonsesList;

    public TVShowDetails(TVShow tvShow){
        super(tvShow);
    }

    public TVShowDetails(TVShowDetails tvShowDetails){
        super(tvShowDetails.getPopularity(), tvShowDetails.getId(), tvShowDetails.getVoteAverage(), tvShowDetails.getOverview(), tvShowDetails.getFirstAirDate(),
                tvShowDetails.getName(), tvShowDetails.getOriginalName(), tvShowDetails.getOriginalLanguage(), tvShowDetails.getVoteCount(),
                tvShowDetails.getPosterPath(), tvShowDetails.getBackdropPath());
        mHomepage = tvShowDetails.mHomepage;
        mInProduction = tvShowDetails.mInProduction;
        mNumberOfEpisodes = tvShowDetails.mNumberOfEpisodes;
        mNumberOfSeasons = tvShowDetails.mNumberOfSeasons;
        mType = tvShowDetails.mType;
        mTVShowSeasonsesList = tvShowDetails.mTVShowSeasonsesList;
    }

    public TVShowDetails(float popularity, int id, float vote_average, String overview, String first_air_date, String name,
                         String original_name, String original_language, int vote_count, String poster_path, String backdrop_path, String homepage,
                         String inProduction, int numberOfEpisodes, int numberOfSeasons, String type, List<TVShowSeasons> tvShowSeasonsList) {
        super(popularity, id, vote_average, overview, first_air_date, name, original_name, original_language, vote_count, poster_path, backdrop_path);
        mHomepage = homepage;
        mInProduction = inProduction;
        mNumberOfEpisodes = numberOfEpisodes;
        mNumberOfSeasons = numberOfSeasons;
        mType = type;
        mTVShowSeasonsesList = tvShowSeasonsList;
    }

    public TVShowDetails(TVShow tvShow, TVShowDetails tvShowDetails){
        super(tvShow);
        mHomepage = tvShowDetails.mHomepage;
        mInProduction = tvShowDetails.mInProduction;
        mNumberOfEpisodes = tvShowDetails.mNumberOfEpisodes;
        mNumberOfSeasons = tvShowDetails.mNumberOfSeasons;
        mType = tvShowDetails.mType;
    }

    public TVShowDetails(TVShow tvShow, String homepage, String inProduction, int numberOfEpisodes, int numberOfSeasons, String type){
        super(tvShow);
        mHomepage = homepage;
        mInProduction = inProduction;
        mNumberOfEpisodes = numberOfEpisodes;
        mNumberOfSeasons = numberOfSeasons;
        mType = type;
    }

    public String getHomepage() {
        return mHomepage;
    }

    public void setHomepage(String homepage) {
        mHomepage = homepage;
    }

    public String getInProduction() {
        return mInProduction;
    }

    public void setInProduction(String inProduction) {
        mInProduction = inProduction;
    }

    public int getNumberOfEpisodes() {
        return mNumberOfEpisodes;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        mNumberOfEpisodes = numberOfEpisodes;
    }

    public int getNumberOfSeasons() {
        return mNumberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        mNumberOfSeasons = numberOfSeasons;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public List<TVShowSeasons> getTVShowSeasonsesList() {
        return mTVShowSeasonsesList;
    }

    public void setTVShowSeasonsesList(List<TVShowSeasons> TVShowSeasonsesList) {
        mTVShowSeasonsesList = TVShowSeasonsesList;
    }
}
