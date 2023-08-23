using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class Person
{
    [JsonProperty("tv_show_id")]
    public string TvShowId { get; set; } = default!;

    [JsonProperty("id")]
    public string Id { get; set; } = default!;

    [JsonProperty("name")]
    public string Name { get; set; } = default!;

    [JsonProperty("credit_id")]
    public string CreditId { get; set; } = default!;

    [JsonProperty("character")]
    public string Character { get; set; } = default!;

    [JsonProperty("order")]
    public string Order { get; set; } = default!;

    [JsonProperty("department")]
    public string Department { get; set; } = default!;

    [JsonProperty("job")]
    public string Job { get; set; } = default!;

    [JsonProperty("profile_img_path")]
    public string ProfileImgPath { get; set; } = default!;
}