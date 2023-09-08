using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class SpokenLanguage
{
    [JsonProperty("english_name")]
    public string EnglishName { get; set; } = string.Empty;

    [JsonProperty("iso_639_1")]
    public string Iso6391 { get; set; } = string.Empty;

    [JsonProperty("name")]
    public string Name { get; set; } = string.Empty;
}