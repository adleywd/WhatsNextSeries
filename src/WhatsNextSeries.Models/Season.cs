using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class Season
{
    [JsonProperty("air_date")]
    public string AirDate { get; set; } = string.Empty;

    [JsonProperty("episode_count")]
    public int EpisodeCount { get; set; }

    [JsonProperty("id")]
    public int Id { get; set; }

    [JsonProperty("name")]
    public string Name { get; set; } = string.Empty;

    [JsonProperty("overview")]
    public string Overview { get; set; } = string.Empty;

    [JsonProperty("poster_path")]
    public string PosterPath { get; set; } = string.Empty;

    [JsonProperty("season_number")]
    public int SeasonNumber { get; set; }

    [JsonProperty("vote_average")]
    public double VoteAverage { get; set; }
}