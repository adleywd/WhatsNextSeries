using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class TvShowDetail : TvShow
{
    public TvShowDetail()
    {
    }

    public TvShowDetail(TvShow tvShow)
    {
        BackdropPath = tvShow.BackdropPath;
        FirstAirDate = tvShow.FirstAirDate;
        GenreIds = tvShow.GenreIds;
        Id = tvShow.Id;
        Name = tvShow.Name;
        OriginCountry = tvShow.OriginCountry;
        OriginalLanguage = tvShow.OriginalLanguage;
        OriginalName = tvShow.OriginalName;
        Overview = tvShow.Overview;
        Popularity = tvShow.Popularity;
        PosterPath = tvShow.PosterPath;
        VoteAverage = tvShow.VoteAverage;
        VoteCount = tvShow.VoteCount;
    }

    [JsonProperty("adult")]
    public bool Adult { get; set; }

    [JsonProperty("created_by")]
    public List<CreatedBy> CreatedBy { get; set; } = new();

    [JsonProperty("episode_run_time")]
    public List<int> EpisodeRunTime { get; set; } = new();

    [JsonProperty("genres")]
    public List<Genre> Genres { get; set; } = new();

    [JsonProperty("homepage")]
    public string Homepage { get; set; } = string.Empty;

    [JsonProperty("in_production")]
    public bool InProduction { get; set; }

    [JsonProperty("languages")]
    public List<string> Languages { get; set; } = new();

    [JsonProperty("last_air_date")]
    public string LastAirDate { get; set; } = string.Empty;

    [JsonProperty("last_episode_to_air")]
    public AiringEpisode LastEpisodeToAir { get; set; } = new();

    [JsonProperty("next_episode_to_air")]
    public AiringEpisode NextEpisodeToAir { get; set; } = new();

    [JsonProperty("networks")]
    public List<Network> Networks { get; set; } = new();

    [JsonProperty("number_of_episodes")]
    public int NumberOfEpisodes { get; set; }

    [JsonProperty("number_of_seasons")]
    public int NumberOfSeasons { get; set; }

    [JsonProperty("production_companies")]
    public List<ProductionCompany> ProductionCompanies { get; set; } = new();

    [JsonProperty("production_countries")]
    public List<ProductionCountry> ProductionCountries { get; set; } = new();

    [JsonProperty("seasons")]
    public List<Season> Seasons { get; set; } = new();

    [JsonProperty("spoken_languages")]
    public List<SpokenLanguage> SpokenLanguages { get; set; } = new();

    [JsonProperty("status")]
    public string Status { get; set; } = string.Empty;

    [JsonProperty("tagline")]
    public string Tagline { get; set; } = string.Empty;

    [JsonProperty("type")]
    public string Type { get; set; } = string.Empty;
}