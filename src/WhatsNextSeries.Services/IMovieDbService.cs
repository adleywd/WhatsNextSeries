using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

public interface IMovieDbService
{
    public Task<IEnumerable<TvShow>> GetPopularShows(CancellationToken cancellationToken);
    
    public Task<IEnumerable<TvShow>> GetAiringTodayShows(CancellationToken cancellationToken);
}