using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class SeasonEpisodes
{
    [JsonProperty("episode_id")]
    public int EpisodeId { get; set; }

    [JsonProperty("air_date")]
    public string AirDate { get; set; } = default!;

    [JsonProperty("episode_number")]
    public int EpisodeNumber { get; set; }

    [JsonProperty("episode_name")]
    public string EpisodeName { get; set; } = default!;

    [JsonProperty("episode_overview")]
    public string EpisodeOverview { get; set; } = default!;

    [JsonProperty("episode_production_code")]
    public string EpisodeProductionCode { get; set; } = default!;

    [JsonProperty("episode_season_number")]
    public int EpisodeSeasonNumber { get; set; }

    [JsonProperty("episode_still_path")]
    public string EpisodeStillPath { get; set; } = default!;

    [JsonProperty("episode_vote_average")]
    public float EpisodeVoteAverage { get; set; }

    [JsonProperty("episode_vote_count")]
    public float EpisodeVoteCount { get; set; }
}