using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

public interface IMovieDbService
{
    /// <summary>
    /// Get popular shows
    /// </summary>
    /// <param name="page"></param>
    /// <param name="cancellationToken"></param>
    /// <returns></returns>
    public Task<IEnumerable<TvShow>> GetPopularShows(int page, CancellationToken cancellationToken = default);
    
    /// <summary>
    /// Get shows that are airing today
    /// </summary>
    /// <param name="page"></param>
    /// <param name="cancellationToken"></param>
    /// <returns></returns>
    public Task<IEnumerable<TvShow>> GetAiringTodayShows(int page, CancellationToken cancellationToken = default);
    
    /// <summary>
    /// Get a tv show details
    /// </summary>
    /// <param name="id"></param>
    /// <param name="cancellationToken"></param>
    /// <returns></returns>
    public Task<TvShowDetail> GetTvShowDetails(int id, CancellationToken cancellationToken = default);
    
    /// <summary>
    /// Get a list of tv shows by name
    /// </summary>
    /// <param name="name"></param>
    /// <param name="page"></param>
    /// <param name="cancellationToken"></param>
    /// <returns></returns>
    public Task<IEnumerable<TvShow>> GetTvShowsByName(string name, int page = 1, CancellationToken cancellationToken = default);
    
    
}