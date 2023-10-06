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
    public ObservableCollection<TvShowViewModel> FavoritesShows { get; } = new();
    
    [ObservableProperty]
    private bool _isLoadingFavoritesShows = true;

    public async Task AddShowToFavoritesAsync(TvShowViewModel tvShowViewModel)
    {
        if (tvShowViewModel.IsFavorite)
        {
            return;
        }

        if (await _favoritesDataService.SaveFavoriteTvShow(new List<TvShowDetail> { tvShowViewModel.TvShow }).ConfigureAwait(true))
        {
            FavoritesShows.Add(tvShowViewModel);
            UpdateFavoriteInTabbedLists(tvShowViewModel.TvShow.Id, true);
        }
    }

    public async Task LoadShowsFromFavorites()
    {
        using var cancellationToken = new CancellationTokenSource();
        cancellationToken.CancelAfter(TimeSpan.FromMinutes(2));

        var favoritesTvShows =
            await _favoritesDataService.LoadFavoritesTvShow(cancellationToken.Token).ConfigureAwait(true);
        FavoritesShows.Clear();
        foreach (var tvShow in favoritesTvShows)
        {
            FavoritesShows.Add(new TvShowViewModel(tvShow, MainViewModel, true));
        }

        IsLoadingFavoritesShows = false;
    }

    public async Task RemoveShowFromFavoritesAsync(List<int> tvShowIdList)
    {
        if (await _favoritesDataService.RemoveFavoritesTvShow(tvShowIdList).ConfigureAwait(true))
        {
            foreach (var tvShowId in tvShowIdList)
            {
                var tvShowToRemove = FavoritesShows.FirstOrDefault(tvShowViewModel => tvShowViewModel.TvShow.Id == tvShowId);
                if (tvShowToRemove != null)
                {
                    FavoritesShows.Remove(tvShowToRemove);
                    UpdateFavoriteInTabbedLists(tvShowId, false);
                }
            }
        }
    }
    
    private void UpdateFavoriteInTabbedLists(int tvShowId, bool isFavorite)
    {
        var tvShowInPopularList = PopularShows.FirstOrDefault(tvVm => tvVm.TvShow.Id == tvShowId);
        if (tvShowInPopularList != null)
        {
            tvShowInPopularList.IsFavorite = isFavorite;
        }

        var tvShowInAiringTodayList = AiringToday.FirstOrDefault(tvVm => tvVm.TvShow.Id == tvShowId);
        if (tvShowInAiringTodayList != null)
        {
            tvShowInAiringTodayList.IsFavorite = isFavorite;
        }
    }
}