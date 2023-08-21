package br.com.adley.whatsnextseries.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Adley on 20/04/2016.
 * Base model for TVShowDetails.
 * Creates a new TVShow, but with more fields.
 */
public class TVShowDetails extends TVShow implements Serializable {

    private static final long serialVersionUID = 1L;

    // Model from details
    private String mHomepage;
    private boolean mInProduction;
    private int mNumberOfEpisodes;
    private int mNumberOfSeasons;
    private String mType;
    private String nextEpisode;
    private String nextEpisodeDate;
    private String nextEpisodeName;
    private String nextEpisodeNumber;
    private String nextEpisodePoster;
    private ArrayList<Integer> mSeasonNumberList;

    public TVShowDetails(TVShow tvShow){
        super(tvShow);
    }

    public TVShowDetails(TVShowDetails tvShowDetails){
        super(tvShowDetails.getPopularity(), tvShowDetails.getId(), tvShowDetails.getVoteAverage(), tvShowDetails.getOverview(), tvShowDetails.getFirstAirDate(),
                tvShowDetails.getName(), tvShowDetails.getOriginalName(), tvShowDetails.getOriginalLanguage(), tvShowDetails.getVoteCount(),
                tvShowDetails.getPosterPath(), tvShowDetails.getBackdropPath(), tvShowDetails.getPosterSize(), tvShowDetails.getBackDropSize());
        mHomepage = tvShowDetails.mHomepage;
        mInProduction = tvShowDetails.mInProduction;
        mNumberOfEpisodes = tvShowDetails.mNumberOfEpisodes;
        mNumberOfSeasons = tvShowDetails.mNumberOfSeasons;
        mType = tvShowDetails.mType;
    }
    /*
    public TVShowDetails(float popularity, int id, float vote_average, String overview, String first_air_date, String name,
                         String original_name, String original_language, int vote_count, String poster_path, String backdrop_path, String homepage,
                         boolean inProduction, int numberOfEpisodes, int numberOfSeasons, String type) {
        super(popularity, id, vote_average, overview, first_air_date, name, original_name, original_language, vote_count, poster_path, backdrop_path);
        mHomepage = homepage;
        mInProduction = inProduction;
        mNumberOfEpisodes = numberOfEpisodes;
        mNumberOfSeasons = numberOfSeasons;
        mType = type;
    }*/

    public TVShowDetails(float popularity, int id, float vote_average, String overview, String first_air_date, String name,
                         String original_name, String original_language, int vote_count, String poster_path, String backdrop_path, String homepage,
                         boolean inProduction, int numberOfEpisodes, int numberOfSeasons, String type, String poster_size, String back_drop_size, ArrayList<Integer> season_number_list) {
        super(popularity, id, vote_average, overview, first_air_date, name, original_name, original_language, vote_count, poster_path, backdrop_path, poster_size, back_drop_size);
        mHomepage = homepage;
        mInProduction = inProduction;
        mNumberOfEpisodes = numberOfEpisodes;
        mNumberOfSeasons = numberOfSeasons;
        mType = type;
        mSeasonNumberList = season_number_list;
    }

    public TVShowDetails(TVShow tvShow, TVShowDetails tvShowDetails){
        super(tvShow);
        mHomepage = tvShowDetails.mHomepage;
        mInProduction = tvShowDetails.mInProduction;
        mNumberOfEpisodes = tvShowDetails.mNumberOfEpisodes;
        mNumberOfSeasons = tvShowDetails.mNumberOfSeasons;
        mType = tvShowDetails.mType;
    }

    public TVShowDetails(TVShow tvShow, String homepage, boolean inProduction, int numberOfEpisodes, int numberOfSeasons, String type, ArrayList<Integer> seasonNumberList){
        super(tvShow);
        mHomepage = homepage;
        mInProduction = inProduction;
        mNumberOfEpisodes = numberOfEpisodes;
        mNumberOfSeasons = numberOfSeasons;
        mType = type;
        mSeasonNumberList = seasonNumberList;
    }

    public boolean isInProduction() {
        return mInProduction;
    }

    public String getNextEpisodeDate() {
        return nextEpisodeDate;
    }

    public void setNextEpisodeDate(String nextEpisodeDate) {
        this.nextEpisodeDate = nextEpisodeDate;
    }

    public String getNextEpisodeName() {
        return nextEpisodeName;
    }

    public void setNextEpisodeName(String nextEpisodeName) {
        this.nextEpisodeName = nextEpisodeName;
    }

    public String getNextEpisodeNumber() {
        return nextEpisodeNumber;
    }

    public void setNextEpisodeNumber(String nextEpisodeNumber) {
        this.nextEpisodeNumber = nextEpisodeNumber;
    }

    public String getHomepage() {
        return mHomepage;
    }

    public void setHomepage(String homepage) {
        mHomepage = homepage;
    }

    public boolean getInProduction() {
        return mInProduction;
    }

    public void setInProduction(boolean inProduction) {
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

    public String getNextEpisode() {
        return nextEpisode;
    }

    public void setNextEpisode(String nextEpisode) {
        this.nextEpisode = nextEpisode;
    }

    public String getNextEpisodePoster() {
        return nextEpisodePoster;
    }

    public void setNextEpisodePoster(String nextEpisodePoster) {
        this.nextEpisodePoster = nextEpisodePoster;
    }

    public ArrayList<Integer> getSeasonNumberList() {
        return mSeasonNumberList;
    }

    public void setSeasonNumberList(ArrayList<Integer> seasonNumberList) {
        mSeasonNumberList = seasonNumberList;
    }

    @Override
    public String toString() {

        return super.toString() + "TVShowDetails{" +
                "mHomepage='" + mHomepage + '\'' +
                ", mInProduction='" + mInProduction + '\'' +
                ", mNumberOfEpisodes=" + mNumberOfEpisodes +
                ", mNumberOfSeasons=" + mNumberOfSeasons +
                ", mType='" + mType + '\'' +
                '}';
    }
}
