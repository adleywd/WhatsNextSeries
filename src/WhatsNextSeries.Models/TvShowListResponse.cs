using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class TvShowListResponse
{
    [JsonProperty("page")]
    public int Page { get; set; }

    [JsonProperty("results")]
    public List<TvShow> Results { get; set; } = new ();

    [JsonProperty("total_pages")]
    public int TotalPages { get; set; }

    [JsonProperty("total_results")]
    public int TotalResults { get; set; }
}