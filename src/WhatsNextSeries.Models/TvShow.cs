
using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class TvShow
{
    public string PosterSize { get; set; } = "w342";

    public string BackDropSize { get; set; } = "w780";

    public string PrefixPosterLink { get; init; } = "https://image.tmdb.org/t/p/";

    public string PrefixBackDropLink { get; init; } = "https://image.tmdb.org/t/p/";

    [JsonProperty("backdrop_path")]
    public string BackdropPath { get; set; } = string.Empty;

    [JsonProperty("first_air_date")]
    public string FirstAirDate { get; set; } = string.Empty;

    [JsonProperty("genre_ids")]
    public List<int> GenreIds { get; set; } = new();

    [JsonProperty("id")]
    public int Id { get; set; }

    [JsonProperty("name")]
    public string Name { get; set; } = string.Empty;

    [JsonProperty("origin_country")]
    public List<string> OriginCountry { get; set; } = new();

    [JsonProperty("original_language")]
    public string OriginalLanguage { get; set; } = string.Empty;

    [JsonProperty("original_name")]
    public string OriginalName { get; set; }  = string.Empty;

    [JsonProperty("overview")]
    public string Overview { get; set; }  = string.Empty;

    [JsonProperty("popularity")]
    public double Popularity { get; set; }

    [JsonProperty("poster_path")]
    public string PosterPath { get; set; } = string.Empty;

    [JsonProperty("vote_average")]
    public double VoteAverage { get; set; }

    [JsonProperty("vote_count")]
    public int VoteCount { get; set; }
   
}