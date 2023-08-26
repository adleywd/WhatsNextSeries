package br.com.adley.whatsnextseries.models.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Adley on 05/11/2017.
 *
 */

public class SeasonEpisodes
{
        @SerializedName("air_date")
        @Expose
        private String airDate;
        @SerializedName("episodes")
        @Expose
        private List<Episodes> episodes = null;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("overview")
        @Expose
        private String overview;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("poster_path")
        @Expose
        private String posterPath;
        @SerializedName("season_number")
        @Expose
        private Integer seasonNumber;

        public String getAirDate() {
            return airDate;
        }

        public void setAirDate(String airDate) {
            this.airDate = airDate;
        }

        public List<Episodes> getEpisodes() {
            return episodes;
        }

        public void setEpisodes(List<Episodes> episodes) {
            this.episodes = episodes;
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

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public Integer getSeasonNumber() {
            return seasonNumber;
        }

        public void setSeasonNumber(Integer seasonNumber) {
            this.seasonNumber = seasonNumber;
        }

}
