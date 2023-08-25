using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

public interface IMovieDbService
{
    public Task<List<TvShow>> GetPopularShows();
}