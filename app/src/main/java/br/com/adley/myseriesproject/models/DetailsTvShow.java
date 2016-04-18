package br.com.adley.myseriesproject.models;

/**
 * Created by Adley on 17/04/2016.
 */
public class DetailsTvShow extends TVShow {

    public DetailsTvShow(TVShow tvShow) {
        super(tvShow);
    }

    // Model from details
    private String homepage;
    private String in_production;
    private String number_of_episodes;
    private String number_of_seasons;
    private String type;

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getIn_production() {
        return in_production;
    }

    public void setIn_production(String in_production) {
        this.in_production = in_production;
    }

    public String getNumber_of_episodes() {
        return number_of_episodes;
    }

    public void setNumber_of_episodes(String number_of_episodes) {
        this.number_of_episodes = number_of_episodes;
    }

    public String getNumber_of_seasons() {
        return number_of_seasons;
    }

    public void setNumber_of_seasons(String number_of_seasons) {
        this.number_of_seasons = number_of_seasons;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DetailsTvShow{" +
                "homepage='" + homepage + '\'' +
                ", in_production='" + in_production + '\'' +
                ", number_of_episodes='" + number_of_episodes + '\'' +
                ", number_of_seasons='" + number_of_seasons + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
