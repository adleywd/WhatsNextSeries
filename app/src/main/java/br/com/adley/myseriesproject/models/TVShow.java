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
        this.vote_average = tvShow.vote_average;
        this.overview = tvShow.overview;
        this.first_air_date = tvShow.first_air_date;
        this.name = tvShow.name;
        this.original_name = tvShow.original_name;
        this.original_language = tvShow.original_language;
        this.vote_count = tvShow.vote_count;
        this.poster_path = tvShow.poster_path;
        this.backdrop_path = tvShow.backdrop_path;
    }

    public TVShow(String popularity, String id, String vote_average, String overview, String first_air_date,
                  String name, String original_name, String original_language, String vote_count, String poster_path, String backdrop_path) {
        this.popularity = popularity;
        this.id = id;
        this.vote_average = vote_average;
        this.overview = overview;
        this.first_air_date = first_air_date;
        this.name = name;
        this.original_name = original_name;
        this.original_language = original_language;
        this.vote_count = vote_count;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
    }

    // Model From Search
    private String prefixImgLink = "http://image.tmdb.org/t/p/original";
    private String popularity;
    private String id;
    private String vote_average;
    private String overview;
    private String first_air_date;
    private String name;
    private String original_name;
    private String original_language;
    private String vote_count;

    // Images
    private String poster_path;
    private String backdrop_path;

    public String getPrefixImgLink() {
        return prefixImgLink;
    }

    public void setPrefixImgLink(String prefixImgLink) {
        this.prefixImgLink = prefixImgLink;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getPoster_path() {
        return prefixImgLink+poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return prefixImgLink+backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    @Override
    public String toString() {
        return "TVShow{" +
                "prefixImgLink='" + prefixImgLink + '\'' +
                ", popularity='" + popularity + '\'' +
                ", id='" + id + '\'' +
                ", vote_average='" + vote_average + '\'' +
                ", overview='" + overview + '\'' +
                ", first_air_date='" + first_air_date + '\'' +
                ", name='" + name + '\'' +
                ", original_name='" + original_name + '\'' +
                ", original_language='" + original_language + '\'' +
                ", vote_count='" + vote_count + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                '}';
    }
}
