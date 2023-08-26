using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class TvShowSeasons
{
    [JsonProperty("id")]
    public int SeasonId { get; set; }

    [JsonProperty("air_date")]
    public string SeasonAirDate { get; set; } = default!;

    [JsonProperty("season_number")]
    public int SeasonNumber { get; set; }

    [JsonProperty("name")]
    public string SeasonName { get; set; } = default!;

    [JsonProperty("overview")]
    public string SeasonOverview { get; set; } = default!;

    [JsonProperty("tv_show_id")]
    public int TvShowId { get; set; }
    private List<Episodes> Episodes { get; } = new();
    // SeasonName = $"{seasonNumber}ª Temporada";
    // SeasonOverview = "Não há sinopse para essa temporada.";
}