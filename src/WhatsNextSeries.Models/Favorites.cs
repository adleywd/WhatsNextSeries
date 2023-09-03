namespace WhatsNextSeries.Models;

public class Favorites : TvShow
{
    public Favorites()
    {
        
    }

    public Favorites(TvShow tvShow)
    {
        Id = tvShow.Id;
        Name = tvShow.Name;
        PosterPath = tvShow.PosterPath;
        BackdropPath = tvShow.BackdropPath;
        Overview = tvShow.Overview;
        FirstAirDate = tvShow.FirstAirDate;
        VoteAverage = tvShow.VoteAverage;
        VoteCount = tvShow.VoteCount;
        Popularity = tvShow.Popularity;
        OriginalLanguage = tvShow.OriginalLanguage;
        GenreIds = tvShow.GenreIds;
        OriginalName = tvShow.OriginalName;
        OriginCountry = tvShow.OriginCountry;
        PrefixPosterLink = tvShow.PrefixPosterLink;
        PrefixBackDropLink = tvShow.PrefixBackDropLink;
    }
    
    public DateTime NextEpisodeAirDate { get; set; }
}