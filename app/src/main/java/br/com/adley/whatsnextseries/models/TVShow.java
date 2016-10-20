package br.com.adley.whatsnextseries.models;

import java.io.Serializable;

import br.com.adley.whatsnextseries.library.AppConsts;

/**
 * Created by adley on 16/04/16.
 * Base Model for TV Show.
 * Without genres.
 */
public class TVShow implements Serializable {

    private static final long serialVersionUID = 1L;

    // Model From Search
    private String mPosterSize = AppConsts.POSTER_DEFAULT_SIZE;
    private String mBackDropSize = AppConsts.BACKDROP_DEFAULT_SIZE;
    private String mPrefixPosterLink = AppConsts.PREFIX_IMG_LINK + mPosterSize;
    private String mPrefixBackDropLink = AppConsts.PREFIX_IMG_LINK_BACKDROP + mBackDropSize;
    private float mPopularity;
    private int mId;
    private float mVoteAverage;
    private String mOverview;
    private String mFirstAirDate;
    private String mName;
    private String mOriginalName;
    private String mOriginalLanguage;
    private int mVoteCount;
    private int mTotalPages;
    private int mTotalResults;
    private int mPage;

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
                  String name, String originalName, String originalLanguage, int voteCount, String posterPath, String backdropPath, String posterSize, String backDropSize) {
        this.mPopularity = popularity;
        this.mId = id;
        this.mVoteAverage = voteAverage;
        this.mOverview = overview;
        this.mFirstAirDate = firstAirDate;
        this.mName = name;
        this.mOriginalName = originalName;
        this.mOriginalLanguage = originalLanguage;
        this.mVoteCount = voteCount;
        this.mPosterSize = posterSize;
        this.mBackDropSize = backDropSize;
        // Set the prefix for the new size
        this.mPrefixPosterLink = posterPath == null ? mPrefixPosterLink : AppConsts.PREFIX_IMG_LINK + posterSize;
        this.mPrefixBackDropLink = backdropPath == null ? mPrefixBackDropLink : AppConsts.PREFIX_IMG_LINK_BACKDROP + backDropSize;
        // Set Images Path with the news sizes
        this.mPosterPath = posterPath == null ? null : mPrefixPosterLink + posterPath;
        this.mBackdropPath = backdropPath == null ? null : mPrefixBackDropLink + backdropPath;
    }

    public TVShow(float popularity, int id, float voteAverage, String overview, String firstAirDate,
                  String name, String originalName, String originalLanguage, int voteCount, String posterPath,
                  String backdropPath, String posterSize, String backDropSize, int page, int total_results, int total_pages) {
        this.mPopularity = popularity;
        this.mId = id;
        this.mVoteAverage = voteAverage;
        this.mOverview = overview;
        this.mFirstAirDate = firstAirDate;
        this.mName = name;
        this.mOriginalName = originalName;
        this.mOriginalLanguage = originalLanguage;
        this.mVoteCount = voteCount;
        this.mPosterSize = posterSize;
        this.mBackDropSize = backDropSize;
        // Set the prefix for the new size
        this.mPrefixPosterLink = posterPath == null ? mPrefixPosterLink : AppConsts.PREFIX_IMG_LINK + posterSize;
        this.mPrefixBackDropLink = backdropPath == null ? mPrefixBackDropLink : AppConsts.PREFIX_IMG_LINK_BACKDROP + backDropSize;
        // Set Images Path with the news sizes
        this.mPosterPath = posterPath == null ? null : mPrefixPosterLink + posterPath;
        this.mBackdropPath = backdropPath == null ? null : mPrefixBackDropLink + backdropPath;
        this.mPage = page;
        this.mTotalResults = total_results;
        this.mTotalPages = total_pages;
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
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public String getPosterSize() {
        return mPosterSize;
    }

    public void setPosterSize(String posterSize) {
        mPosterSize = posterSize;
    }

    public String getBackDropSize() {
        return mBackDropSize;
    }

    public void setBackDropSize(String backDropSize) {
        mBackDropSize = backDropSize;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(int totalPages) {
        mTotalPages = totalPages;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(int totalResults) {
        mTotalResults = totalResults;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    @Override
    public String toString() {
        return "TVShow{" +
                "mPosterSize='" + mPosterSize + '\'' +
                ", mBackDropSize='" + mBackDropSize + '\'' +
                ", mPrefixPosterLink='" + mPrefixPosterLink + '\'' +
                ", mPrefixBackDropLink='" + mPrefixBackDropLink + '\'' +
                ", mPopularity=" + mPopularity +
                ", mId=" + mId +
                ", mVoteAverage=" + mVoteAverage +
                ", mOverview='" + mOverview + '\'' +
                ", mFirstAirDate='" + mFirstAirDate + '\'' +
                ", mName='" + mName + '\'' +
                ", mOriginalName='" + mOriginalName + '\'' +
                ", mOriginalLanguage='" + mOriginalLanguage + '\'' +
                ", mVoteCount=" + mVoteCount +
                ", mTotalPages=" + mTotalPages +
                ", mTotalResults=" + mTotalResults +
                ", mPage=" + mPage +
                ", mPosterPath='" + mPosterPath + '\'' +
                ", mBackdropPath='" + mBackdropPath + '\'' +
                '}';
    }
}
