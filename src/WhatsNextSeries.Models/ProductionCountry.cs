using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class ProductionCountry
{
    [JsonProperty("iso_3166_1")]
    public string Iso31661 { get; set; } = string.Empty;

    [JsonProperty("name")]
    public string Name { get; set; } = string.Empty;
}