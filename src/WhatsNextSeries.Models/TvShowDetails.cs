using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class TvShowDetails
{
    [JsonProperty("homepage")]
    public string Homepage { get; set; }

    [JsonProperty("in_production")]
    public bool InProduction { get; set; }

    [JsonProperty("number_of_episodes")]
    public int NumberOfEpisodes { get; set; }

    [JsonProperty("number_of_seasons")]
    public int NumberOfSeasons { get; set; }

    [JsonProperty("type")]
    public string Type { get; set; }

    [JsonProperty("next_episode")]
    public string NextEpisode { get; set; }

    [JsonProperty("next_episode_date")]
    public string NextEpisodeDate { get; set; }

    [JsonProperty("next_episode_name")]
    public string NextEpisodeName { get; set; }

    [JsonProperty("next_episode_number")]
    public string NextEpisodeNumber { get; set; }

    [JsonProperty("next_episode_poster")]
    public string NextEpisodePoster { get; set; }

    [JsonProperty("season_number_list")]
    public List<int> SeasonNumberList { get; set; }
}