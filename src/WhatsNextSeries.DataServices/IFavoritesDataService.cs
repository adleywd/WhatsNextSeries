using WhatsNextSeries.Models;

namespace WhatsNextSeries.DataServices;

/// <summary>
/// Tv show file manager
/// </summary>
public interface IFavoritesDataService
{
    /// <summary>
    /// Save favorites tv shows
    /// </summary>
    /// <param name="tvShowDetailList"></param>
    /// <param name="cancellationToken"></param>
    /// <returns></returns>
    public Task<bool> SaveFavoriteTvShow(List<TvShowDetail> tvShowDetailList, CancellationToken cancellationToken = default);
    
    /// <summary>
    /// Load favorites tv shows
    /// </summary>
    /// <param name="cancellationToken"></param>
    /// <returns></returns>
    public Task<List<TvShowDetail>> LoadFavoritesTvShow(CancellationToken cancellationToken = default);
    
    /// <summary>
    /// Remove a list of favorites tv shows by id
    /// </summary>
    /// <param name="tvShowId"></param>
    /// <param name="cancellationToken"></param>
    /// <returns></returns>
    public Task<bool> RemoveFavoritesTvShow(IList<int> tvShowId, CancellationToken cancellationToken = default);
}