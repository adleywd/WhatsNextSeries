package br.com.adley.library;

/**
 * Created by Adley.Damaceno on 09/03/2016.
 * Define the TVShow parameters
 */
public class TVShow {
    private String id;
    private String url;
    private String name;
    private String type;
    private String language;
    private String status;
    private String imageMedium;
    private String imageOriginal;
    private String summary;
    private String previousEpisode;
    private String nextEpisode;

    public TVShow(String id, String url, String name, String type, String language, String status, String imageMedium, String imageOriginal, String summary, String previousEpisode, String nextEpisode) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.type = type;
        this.language = language;
        this.status = status;
        this.imageMedium = imageMedium;
        this.imageOriginal = imageOriginal;
        this.summary = summary;
        this.previousEpisode = previousEpisode;
        this.nextEpisode = nextEpisode;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLanguage() {
        return language;
    }

    public String getStatus() {
        return status;
    }

    public String getImageMedium() {
        return imageMedium;
    }

    public String getImageOriginal() {
        return imageOriginal;
    }

    public String getSummary() {
        return summary;
    }

    public String getPreviousEpisode() {
        return previousEpisode;
    }

    public String getNextEpisode() {
        return nextEpisode;
    }

    @Override
    public String toString() {
        return "TVShow{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", language='" + language + '\'' +
                ", status='" + status + '\'' +
                ", imageMedium='" + imageMedium + '\'' +
                ", imageOriginal='" + imageOriginal + '\'' +
                ", summary='" + summary + '\'' +
                ", previousEpisode='" + previousEpisode + '\'' +
                ", nextEpisode='" + nextEpisode + '\'' +
                '}';
    }
}
