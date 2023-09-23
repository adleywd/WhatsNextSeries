using WhatsNextSeries.Models;

namespace WhatsNextSeries.DataServices;

public class DummyFavoritesDataService : IFavoritesDataService
{
    public Task<bool> SaveFavoriteTvShow(List<TvShowDetail> tvShowDetailList, CancellationToken cancellationToken = default)
    {
        return Task.FromResult(true);
    }

    public async Task<List<TvShowDetail>> LoadFavoritesTvShow(CancellationToken cancellationToken = default)
    {
        var tvShows = new List<TvShowDetail>();
        tvShows.Add(new ()
        {
            Id = 1,
            Name = "The Mandalorian",
            BackdropPath = "/9ijMGlJKqcslswWUzTEwScm82Gs.jpg",
            PosterPath = "/sWgBv7LV2PRoQgkxwlibdGXKz1S.jpg"
        });
        tvShows.Add(new ()
        {
            Id = 2,
            Name = "The Witcher",
            BackdropPath = "/9ijMGlJKqcslswWUzTEwScm82Gs.jpg",
            PosterPath = "/sWgBv7LV2PRoQgkxwlibdGXKz1S.jpg"
        });
        tvShows.Add(new ()
        {
            Id = 3,
            Name = "Game of Thrones",
            BackdropPath = "/9ijMGlJKqcslswWUzTEwScm82Gs.jpg",
            PosterPath = "/sWgBv7LV2PRoQgkxwlibdGXKz1S.jpg"
        });
        
        return await Task.FromResult(tvShows).ConfigureAwait(false);

}

    public Task<bool> RemoveFavoritesTvShow(IList<int> tvShowId, CancellationToken cancellationToken = default)
    {
        return Task.FromResult(true);
    }
}