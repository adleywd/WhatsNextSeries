using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

public interface ITheMovieDbService
{
    public Task<List<TvShow>> GetPopularShows();
}