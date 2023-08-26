using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class Episodes
{
    [JsonProperty("id")]
    public int Id { get; set; }

    [JsonProperty("air_date")]
    public DateTime AirDate { get; set; }

    [JsonProperty("crew")]
    public List<Person> Crew { get; set; } = new();

    [JsonProperty("episode_number")]
    public int EpisodeNumber { get; set; }

    [JsonProperty("guest_stars")]
    public List<Person> GuestStars { get; set; } = new();

    [JsonProperty("name")]
    public string Name { get; set; } = default!;

    [JsonProperty("overview")]
    public string Overview { get; set; } = default!;

    [JsonProperty("production_code")]
    public string ProductionCode { get; set; } = default!;

    [JsonProperty("season_number")]
    public int SeasonNumber { get; set; }

    [JsonProperty("still_path")]
    public string StillPath { get; set; } = default!;

    [JsonProperty("vote_average")]
    public double VoteAverage { get; set; }

    [JsonProperty("vote_count")]
    public int VoteCount { get; set; }

    [JsonProperty("prefix_img_path")]
    public string PrefixImgPath { get; set; } = default!;
}