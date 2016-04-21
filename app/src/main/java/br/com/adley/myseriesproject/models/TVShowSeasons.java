package br.com.adley.myseriesproject.models;

import java.io.Serializable;

/**
 * Created by adley on 16/04/16.
 * Base model for seasons
 */
public class TVShowSeasons extends TVShowDetails implements Serializable{

    private static final long serialVersionUID = 1L;

    private int mIdSeason;
    private int  mEpisodeCount;
    private String mAirDate;
    private int mSeasonNumber;

    //Images
    private String mSeasonPosterPath;

    public TVShowSeasons(TVShowDetails tvShowDetailed, int id, int episodeCount, String airDate, int seasonNumber, String seasonPosterPath) {
        super(tvShowDetailed);
        mIdSeason = id;
        mEpisodeCount = episodeCount;
        mAirDate = airDate;
        mSeasonNumber = seasonNumber;
        mSeasonPosterPath = seasonPosterPath;
    }


    public int getIdSeason() {
        return mIdSeason;
    }

    public void setId(int idSeason) {
        mIdSeason = idSeason;
    }

    public int getEpisodeCount() {
        return mEpisodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        mEpisodeCount = episodeCount;
    }

    public String getAirDate() {
        return mAirDate;
    }

    public void setAirDate(String airDate) {
        mAirDate = airDate;
    }

    public int getSeasonNumber() {
        return mSeasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        mSeasonNumber = seasonNumber;
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
                ", mEpisodeCount=" + mEpisodeCount +
                ", mAirDate='" + mAirDate + '\'' +
                ", mSeasonNumber=" + mSeasonNumber +
                ", mSeasonPosterPath='" + mSeasonPosterPath + '\'' +
                '}';
    }
}
