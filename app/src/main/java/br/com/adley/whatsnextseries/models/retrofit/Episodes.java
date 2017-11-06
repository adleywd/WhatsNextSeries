package br.com.adley.whatsnextseries.models.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.adley.whatsnextseries.library.AppConsts;

/**
 * Created by Adley on 05/11/2017.
 * Episode object Model
 */

public class Episodes {

        @SerializedName("air_date")
        @Expose
        private String airDate;
        @SerializedName("crew")
        @Expose
        private List<Object> crew = null;
        @SerializedName("episode_number")
        @Expose
        private Integer episodeNumber;
        @SerializedName("guest_stars")
        @Expose
        private List<Object> guestStars = null;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("overview")
        @Expose
        private String overview;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("production_code")
        @Expose
        private Object productionCode;
        @SerializedName("season_number")
        @Expose
        private Integer seasonNumber;
        @SerializedName("still_path")
        @Expose
        private String stillPath;
        @SerializedName("vote_average")
        @Expose
        private Double voteAverage;
        @SerializedName("vote_count")
        @Expose
        private Integer voteCount;
        private String mPrefixImgPath;

        public Episodes(){
             mPrefixImgPath = AppConsts.PREFIX_IMG_LINK + "w300";
        }

        public String getAirDate() {
            return airDate;
        }

        public void setAirDate(String airDate) {
            this.airDate = airDate;
        }

        public List<Object> getCrew() {
            return crew;
        }

        public void setCrew(List<Object> crew) {
            this.crew = crew;
        }

        public Integer getEpisodeNumber() {
            return episodeNumber;
        }

        public void setEpisodeNumber(Integer episodeNumber) {
            this.episodeNumber = episodeNumber;
        }

        public List<Object> getGuestStars() {
            return guestStars;
        }

        public void setGuestStars(List<Object> guestStars) {
            this.guestStars = guestStars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Object getProductionCode() {
            return productionCode;
        }

        public void setProductionCode(Object productionCode) {
            this.productionCode = productionCode;
        }

        public Integer getSeasonNumber() {
            return seasonNumber;
        }

        public void setSeasonNumber(Integer seasonNumber) {
            this.seasonNumber = seasonNumber;
        }

        public String getStillPath() {
            return stillPath;
        }

        public String getFullStillPath(){
            return mPrefixImgPath + stillPath;
        }

        public void setStillPath(String stillPath) {
            this.stillPath = stillPath;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

}