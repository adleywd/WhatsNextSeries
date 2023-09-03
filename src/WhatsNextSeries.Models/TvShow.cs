
using Newtonsoft.Json;

namespace WhatsNextSeries.Models;

public class TvShow
{
    
    [JsonProperty("poster_size")] 
    public string PosterSize { get; set; } = "w342";

    [JsonProperty("back_drop_size")] 
    public string BackDropSize { get; set; } = "w780";

    public string PrefixPosterLink { get; set; } = "https://image.tmdb.org/t/p/";

    public string PrefixBackDropLink { get; set; } = "https://image.tmdb.org/t/p/";

    [JsonProperty("popularity")] 
    public double Popularity { get; set; }

    [JsonProperty("id")] 
    public int Id { get; set; }

    [JsonProperty("vote_average")] 
    public double VoteAverage { get; set; }

    [JsonProperty("overview")] 
    public string Overview { get; set; } = default!;

    [JsonProperty("first_air_date")] 
    public string FirstAirDate { get; set; } = default!;

    [JsonProperty("name")] 
    public string Name { get; set; } = default!;

    [JsonProperty("original_name")] 
    public string OriginalName { get; set; } = default!;

    [JsonProperty("original_language")]
    public string OriginalLanguage { get; set; } = default!;

    [JsonProperty("vote_count")] 
    public int VoteCount { get; set; }

    [JsonProperty("genre_ids")] 
    public List<int> GenreIds { get; set; } = new();

    [JsonProperty("origin_country")] 
    public List<string> OriginCountry { get; set; } = new();

    [JsonProperty("poster_path")] 
    public string PosterPath { get; set; } = default!;
    
    [JsonProperty("backdrop_path")]
    public string BackdropPath { get; set; } = default!;
   
}