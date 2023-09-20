using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel
{
    private readonly List<int> _tvShowIdsAlreadyInFavorites = new();
    public ObservableCollection<TvShowViewModel> FavoritesShows { get; } = new();

    public async Task AddShowToFavoritesAsync(TvShowDetail tvShow)
    {
        var isFavoriteAlreadyExists = _tvShowIdsAlreadyInFavorites.Exists(id => id == tvShow.Id);
        if (isFavoriteAlreadyExists)
        {
            return;
        }

        if (await _favoritesDataService.SaveFavoriteTvShow(tvShow).ConfigureAwait(true))
        {
            FavoritesShows.Add(new TvShowViewModel(tvShow, MainViewModel));
            _tvShowIdsAlreadyInFavorites.Add(tvShow.Id);
        }
    }
    
    public async Task LoadTvShowsFromFavorites()
    {
        using var cancellationToken = new CancellationTokenSource();
        cancellationToken.CancelAfter(TimeSpan.FromMinutes(2));
        
        var favoritesTvShows = await _favoritesDataService.LoadFavoritesTvShow(cancellationToken.Token).ConfigureAwait(true);
        FavoritesShows.Clear();
        foreach (var tvShow in favoritesTvShows)
        {
            FavoritesShows.Add(new TvShowViewModel(tvShow, MainViewModel));
        }
    }
}