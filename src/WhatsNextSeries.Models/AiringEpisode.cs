using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class AiringEpisode
{
    
    public string StillSize { get; set; } = "w780";

    public string PrefixStillLink { get; init; } = "https://image.tmdb.org/t/p/";
    
    public string GetStillPath => $"{PrefixStillLink}{StillSize}{StillPath}";

    [JsonProperty("id")]
    public int Id { get; set; }

    [JsonProperty("name")]
    public string Name { get; set; } = string.Empty;

    [JsonProperty("overview")]
    public string Overview { get; set; } = string.Empty;

    [JsonProperty("vote_average")]
    public double VoteAverage { get; set; }

    [JsonProperty("vote_count")]
    public int VoteCount { get; set; }

    [JsonProperty("air_date")]
    public string AirDate { get; set; } = string.Empty;

    [JsonProperty("episode_number")]
    public int EpisodeNumber { get; set; }

    [JsonProperty("episode_type")]
    public string EpisodeType { get; set; } = string.Empty;

    [JsonProperty("production_code")]
    public string ProductionCode { get; set; } = string.Empty;

    [JsonProperty("runtime")]
    public int? Runtime { get; set; }

    [JsonProperty("season_number")]
    public int SeasonNumber { get; set; }

    [JsonProperty("show_id")]
    public int ShowId { get; set; }

    [JsonProperty("still_path")]
    public string StillPath { get; set; } = string.Empty;
}
