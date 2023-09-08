using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class ProductionCompany
{
    [JsonProperty("id")]
    public int Id { get; set; }

    [JsonProperty("logo_path")]
    public string LogoPath { get; set; } = string.Empty;

    [JsonProperty("name")]
    public string Name { get; set; } = string.Empty;

    [JsonProperty("origin_country")]
    public string OriginCountry { get; set; } = string.Empty;
}