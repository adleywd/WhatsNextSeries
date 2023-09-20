using WhatsNextSeries.Models;

namespace WhatsNextSeries.DataServices;

/// <summary>
/// Tv show file manager
/// </summary>
public interface IFavoritesDataService
{
    /// <summary>
    /// Save a favorite tv show
    /// </summary>
    /// <param name="tvShowDetail"></param>
    /// <param name="cancellationToken"></param>
    /// <returns></returns>
    public Task<bool> SaveFavoriteTvShow(TvShowDetail tvShowDetail, CancellationToken cancellationToken = default);
    
    /// <summary>
    /// Load favorites tv shows
    /// </summary>
    /// <param name="cancellationToken"></param>
    /// <returns></returns>
    public Task<List<TvShowDetail>> LoadFavoritesTvShow(CancellationToken cancellationToken = default);
}