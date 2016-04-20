package br.com.adley.myseriesproject.models;

import java.io.Serializable;

/**
 * Created by adley on 16/04/16.
 * Base Model for TV Show
 * Without genres
 */
public class TVShow implements Serializable {

    private static final long serialVersionUID = 1L;

    public TVShow(TVShow tvShow) {
        this.prefixImgLink = tvShow.prefixImgLink;
        this.popularity = tvShow.popularity;
        this.id = tvShow.id;
        this.voteAverage = tvShow.voteAverage;
        this.overview = tvShow.overview;
        this.firstAirDate = tvShow.firstAirDate;
        this.name = tvShow.name;
        this.originalName = tvShow.originalName;
        this.originalLanguage = tvShow.originalLanguage;
        this.voteCount = tvShow.voteCount;
        this.posterPath = tvShow.posterPath;
        this.backdropPath = tvShow.backdropPath;
    }

    public TVShow(float popularity, int id, float vote_average, String overview, String first_air_date,
                  String name, String original_name, String original_language, int vote_count, String poster_path, String backdrop_path) {
        this.popularity = popularity;
        this.id = id;
        this.voteAverage = vote_average;
        this.overview = overview;
        this.firstAirDate = first_air_date;
        this.name = name;
        this.originalName = original_name;
        this.originalLanguage = original_language;
        this.voteCount = vote_count;
        this.posterPath = poster_path;
        this.backdropPath = backdrop_path;
    }

    // Model From Search
    private String prefixImgLink = "http://image.tmdb.org/t/p/original";
    private float popularity;
    private int id;
    private float voteAverage;
    private String overview;
    private String firstAirDate;
    private String name;
    private String originalName;
    private String originalLanguage;
    private int voteCount;

    // Images
    private String posterPath;
    private String backdropPath;

    public String getPrefixImgLink() {
        return prefixImgLink;
    }

    public void setPrefixImgLink(String prefixImgLink) {
        this.prefixImgLink = prefixImgLink;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getPosterPath() {
        return prefixImgLink+posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return prefixImgLink+backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Override
    public String toString() {
        return "TVShow{" +
                "prefixImgLink='" + prefixImgLink + '\'' +
                ", popularity=" + popularity +
                ", id=" + id +
                ", voteAverage=" + voteAverage +
                ", overview='" + overview + '\'' +
                ", firstAirDate='" + firstAirDate + '\'' +
                ", name='" + name + '\'' +
                ", originalName='" + originalName + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", voteCount=" + voteCount +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                '}';
    }
}
