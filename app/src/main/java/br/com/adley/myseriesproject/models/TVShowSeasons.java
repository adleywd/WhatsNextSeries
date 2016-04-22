package br.com.adley.myseriesproject.models;

import java.util.List;

/**
 * Created by adley on 16/04/16.
 * Base model for seasons
 */
public class TVShowSeasons {

    //private static final long serialVersionUID = 1L;

    private int mIdSeason;
    private String mSeasonAirDate;
    private int mSeasonNumber;
    private String mSeasonName = mSeasonNumber + "ª " + "Temporada";
    private String mSeasonOverview = "Não há sinopse para essa temporada.";
    private List<TVShowSeasonEpisodes> mEpisodes;

    //Images
    private String mSeasonPosterPath;

    public TVShowSeasons(int idSeason, String seasonAirDate, int seasonNumber, String seasonName, String seasonOverview, List<TVShowSeasonEpisodes> episodes, String seasonPosterPath) {
        mIdSeason = idSeason;
        mSeasonAirDate = seasonAirDate;
        mSeasonNumber = seasonNumber;
        mSeasonName = seasonName == null ? this.mSeasonName : seasonName;
        mSeasonOverview = seasonOverview == null ? this.mSeasonOverview : seasonOverview;
        mEpisodes = episodes;
        mSeasonPosterPath = seasonPosterPath;
    }

    public int getIdSeason() {
        return mIdSeason;
    }

    public void setIdSeason(int idSeason) {
        mIdSeason = idSeason;
    }

    public String getSeasonAirDate() {
        return mSeasonAirDate;
    }

    public void setSeasonAirDate(String seasonAirDate) {
        mSeasonAirDate = seasonAirDate;
    }

    public int getSeasonNumber() {
        return mSeasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        mSeasonNumber = seasonNumber;
    }

    public String getSeasonName() {
        return mSeasonName;
    }

    public void setSeasonName(String seasonName) {
        mSeasonName = seasonName;
    }

    public String getSeasonOverview() {
        return mSeasonOverview;
    }

    public void setSeasonOverview(String seasonOverview) {
        mSeasonOverview = seasonOverview;
    }

    public List<TVShowSeasonEpisodes> getEpisodes() {
        return mEpisodes;
    }

    public void setEpisodes(List<TVShowSeasonEpisodes> episodes) {
        mEpisodes = episodes;
    }

    public String getSeasonPosterPath() {
        return mSeasonPosterPath;
    }

    public void setSeasonPosterPath(String seasonPosterPath) {
        mSeasonPosterPath = seasonPosterPath;
    }

    @Override
    public String toString() {
        return "TVShowSeasons{" +
                "mIdSeason=" + mIdSeason +
                ", mSeasonAirDate='" + mSeasonAirDate + '\'' +
                ", mSeasonNumber=" + mSeasonNumber +
                ", mSeasonName='" + mSeasonName + '\'' +
                ", mSeasonOverview='" + mSeasonOverview + '\'' +
                ", mEpisodes=" + mEpisodes +
                ", mSeasonPosterPath='" + mSeasonPosterPath + '\'' +
                '}';
    }
}
