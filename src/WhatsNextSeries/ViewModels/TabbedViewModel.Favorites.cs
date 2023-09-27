using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using CommunityToolkit.Mvvm.ComponentModel;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel
{
    private readonly List<int> _tvShowIdsAlreadyInFavorites = new();
    public ObservableCollection<TvShowViewModel> FavoritesShows { get; } = new();
    
    [ObservableProperty]
    private bool _isLoadingFavoritesShows = true;

    public async Task AddShowToFavoritesAsync(TvShowDetail tvShow)
    {
        if (IsFavoriteAlreadyExists(tvShow))
        {
            return;
        }

        if (await _favoritesDataService.SaveFavoriteTvShow(new List<TvShowDetail> { tvShow }).ConfigureAwait(true))
        {
            FavoritesShows.Add(new TvShowViewModel(tvShow, MainViewModel));
            _tvShowIdsAlreadyInFavorites.Add(tvShow.Id);
        }
    }

    public async Task LoadTvShowsFromFavorites()
    {
        using var cancellationToken = new CancellationTokenSource();
        cancellationToken.CancelAfter(TimeSpan.FromMinutes(2));

        var favoritesTvShows =
            await _favoritesDataService.LoadFavoritesTvShow(cancellationToken.Token).ConfigureAwait(true);
        FavoritesShows.Clear();
        foreach (var tvShow in favoritesTvShows)
        {
            FavoritesShows.Add(new TvShowViewModel(tvShow, MainViewModel));
        }

        IsLoadingFavoritesShows = false;
    }

    public async Task RemoveTvShowAsync(List<int> tvShowIdList)
    {
        if (await _favoritesDataService.RemoveFavoritesTvShow(tvShowIdList).ConfigureAwait(true))
        {
            foreach (var tvShowId in tvShowIdList)
            {
                var tvShowToRemove = FavoritesShows.FirstOrDefault(tvShowViewModel => tvShowViewModel.Id == tvShowId);
                if (tvShowToRemove != null)
                {
                    FavoritesShows.Remove(tvShowToRemove);
                    _tvShowIdsAlreadyInFavorites.Remove(tvShowId);
                }
            }
        }
    }

    private bool IsFavoriteAlreadyExists(TvShowDetail tvShow)
    {
        var isFavoriteAlreadyExists = _tvShowIdsAlreadyInFavorites.Exists(id => id == tvShow.Id);
        return isFavoriteAlreadyExists;
    }
}