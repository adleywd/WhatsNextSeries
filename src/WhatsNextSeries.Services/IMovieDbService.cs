using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

public interface IMovieDbService
{
    public Task<IEnumerable<TvShow>> GetPopularShows(int page, CancellationToken cancellationToken = default);
    
    public Task<IEnumerable<TvShow>> GetAiringTodayShows(int page, CancellationToken cancellationToken = default);
}