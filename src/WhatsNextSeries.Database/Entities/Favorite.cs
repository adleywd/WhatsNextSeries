namespace WhatsNextSeries.Database.Entities;

public class Favorite
{
    public int Id { get; set; }
    
    public int TvShowId { get; set; }

    public string Name { get; set; } = string.Empty;
    
    public string OriginalName { get; set; }  = string.Empty;
    
    public bool InProduction { get; set; }

    public DateTime NextEpisodeToAir { get; set; }

    public int NumberOfEpisodes { get; set; }

    public int NumberOfSeasons { get; set; }

    public string Status { get; set; } = string.Empty;

    public string Type { get; set; } = string.Empty;
    
    public string PosterPath { get; set; } = string.Empty;

    public string BackdropPath { get; set; } = string.Empty;

    public double Popularity { get; set; }


}