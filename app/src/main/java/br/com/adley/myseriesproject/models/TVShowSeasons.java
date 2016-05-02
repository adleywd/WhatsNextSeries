package br.com.adley.myseriesproject.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by adley on 16/04/16.
 * Base model for seasons
 */
public class TVShowSeasons implements Serializable {

    private static final long serialVersionUID = 1L;

    private int mSeasonId;
    private String mSeasonAirDate;
    private int mSeasonNumber;
    private String mSeasonName = mSeasonNumber + "ª " + "Temporada";
    private String mSeasonOverview = "Não há sinopse para essa temporada.";
    private int mTVShowId;
    private List<TVShowSeasonEpisodes> mEpisodes;

    //Images
    private String mSeasonPosterPath;

    public TVShowSeasons(int tvShowId, int seasonId, String seasonAirDate, int seasonNumber, String seasonName, String seasonOverview, List<TVShowSeasonEpisodes> episodes, String seasonPosterPath) {
        mTVShowId = tvShowId;
        mSeasonId = seasonId;
        mSeasonAirDate = seasonAirDate;
        mSeasonNumber = seasonNumber;
        mSeasonName = seasonName == null ? this.mSeasonName : seasonName;
        mSeasonOverview = seasonOverview == null ? this.mSeasonOverview : seasonOverview;
        mEpisodes = episodes;
        mSeasonPosterPath = seasonPosterPath;
    }

    public int getSeasonId() {
        return mSeasonId;
    }

    public void setSeasonId(int seasonId) {
        mSeasonId = seasonId;
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
        mSeasonName = seasonName != "pt-br" ?  seasonName: mSeasonNumber + "ª " + "Temporada";
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

    public int getTVShowId() {
        return mTVShowId;
    }

    public void setTVShowId(int TVShowId) {
        mTVShowId = TVShowId;
    }

    @Override
    public String toString() {
        return "TVShowSeasons{" +
                "mSeasonId=" + mSeasonId +
                ", mSeasonAirDate='" + mSeasonAirDate + '\'' +
                ", mSeasonNumber=" + mSeasonNumber +
                ", mSeasonName='" + mSeasonName + '\'' +
                ", mSeasonOverview='" + mSeasonOverview + '\'' +
                ", mTVShowId=" + mTVShowId +
                ", mEpisodes=" + mEpisodes +
                ", mSeasonPosterPath='" + mSeasonPosterPath + '\'' +
                '}';
    }
}
