package br.com.adley.myseriesproject.models;

import java.io.Serializable;

/**
 * Created by adley on 16/04/16.
 * Base Model for TV Show.
 * Without genres.
 */
public class TVShow implements Serializable {

    private static final long serialVersionUID = 1L;

    // Model From Search
    private String mPrefixImgLink = "http://image.tmdb.org/t/p/w500";
    private String mPrefixImgLinkBackDrop = "http://image.tmdb.org/t/p/w780";
    private float mPopularity;
    private int mId;
    private float mVoteAverage;
    private String mOverview;
    private String mFirstAirDate;
    private String mName;
    private String mOriginalName;
    private String mOriginalLanguage;
    private int mVoteCount;

    // Images
    private String mPosterPath;
    private String mBackdropPath;

    public TVShow(TVShow tvShow) {
        this.mPopularity = tvShow.mPopularity;
        this.mId = tvShow.mId;
        this.mVoteAverage = tvShow.mVoteAverage;
        this.mOverview = tvShow.mOverview;
        this.mFirstAirDate = tvShow.mFirstAirDate;
        this.mName = tvShow.mName;
        this.mOriginalName = tvShow.mOriginalName;
        this.mOriginalLanguage = tvShow.mOriginalLanguage;
        this.mVoteCount = tvShow.mVoteCount;
        this.mPosterPath = tvShow.mPosterPath;
        this.mBackdropPath = tvShow.mBackdropPath;
    }

    public TVShow(float popularity, int id, float voteAverage, String overview, String firstAirDate,
                  String name, String originalName, String originalLanguage, int voteCount, String posterPath, String backdropPath) {
        this.mPopularity = popularity;
        this.mId = id;
        this.mVoteAverage = voteAverage;
        this.mOverview = overview;
        this.mFirstAirDate = firstAirDate;
        this.mName = name;
        this.mOriginalName = originalName;
        this.mOriginalLanguage = originalLanguage;
        this.mVoteCount = voteCount;
        this.mPosterPath = posterPath;
        this.mBackdropPath = backdropPath;
    }


    public String getPrefixImgLink() {
        return mPrefixImgLink;
    }

    public void setPrefixImgDimension(String newDimension) {
        mPrefixImgLink = mPrefixImgLink.replace("original",newDimension);
    }

    public float getPopularity() {
        return mPopularity;
    }

    public void setPopularity(float popularity) {
        mPopularity = popularity;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public float getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getFirstAirDate() {
        return mFirstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        mFirstAirDate = firstAirDate;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getOriginalName() {
        return mOriginalName;
    }

    public void setOriginalName(String originalName) {
        mOriginalName = originalName;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        mOriginalLanguage = originalLanguage;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(int voteCount) {
        mVoteCount = voteCount;
    }

    public String getPosterPath() {
        return mPosterPath != null ? mPrefixImgLink + mPosterPath : null;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getBackdropPath() {
        return mBackdropPath != null ? mPrefixImgLinkBackDrop + mBackdropPath : null;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    @Override
    public String toString() {
        return "TVShow{" +
                "mPrefixImgLink='" + mPrefixImgLink + '\'' +
                ", mPopularity=" + mPopularity +
                ", mId=" + mId +
                ", mVoteAverage=" + mVoteAverage +
                ", mOverview='" + mOverview + '\'' +
                ", mFirstAirDate='" + mFirstAirDate + '\'' +
                ", mName='" + mName + '\'' +
                ", mOriginalName='" + mOriginalName + '\'' +
                ", mOriginalLanguage='" + mOriginalLanguage + '\'' +
                ", mVoteCount=" + mVoteCount +
                ", mPosterPath='" + mPosterPath + '\'' +
                ", mBackdropPath='" + mBackdropPath + '\'' +
                '}';
    }
}
