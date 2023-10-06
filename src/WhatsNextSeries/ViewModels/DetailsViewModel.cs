using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Diagnostics.CodeAnalysis;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using WhatsNextSeries.DataServices;
using WhatsNextSeries.Helpers;
using WhatsNextSeries.Models;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public partial class DetailsViewModel : ViewModelBase
{
    [ObservableProperty] [SuppressMessage("ReSharper", "InconsistentNaming")]
    private TvShowViewModel _tvShowVM;

    [ObservableProperty] private bool _showBackButton;

    private readonly TabbedViewModel _tabbedViewModel;

    private readonly IMovieDbService _movieDbService;

    public DetailsViewModel(
        TabbedViewModel tabbedViewModel,
        TvShowViewModel tvShowVm,
        IMovieDbService movieDbService)
    {
        _tabbedViewModel = tabbedViewModel;
        _tvShowVM = tvShowVm;
        _movieDbService = movieDbService;
    }

    public async Task LoadDetailedShow(CancellationToken cancellationToken)
    {
        var tvShowDetails = await _movieDbService.GetTvShowDetails(TvShowVM.TvShow.Id, cancellationToken).ConfigureAwait(false);

        // No tv show found
        if (tvShowDetails.Id == 0)
        {
            return;
        }

        TvShowVM.UpdateTvShowDetails(tvShowDetails);
    }

    [RelayCommand]
    private void GoBack()
    {
        _tabbedViewModel.MainViewModel.ContentViewModel = _tabbedViewModel;
    }

    [RelayCommand]
    private async Task AddToFavoritesAsync()
    {
        await _tabbedViewModel.AddShowToFavoritesAsync(TvShowVM).ConfigureAwait(false);
        TvShowVM.IsFavorite = true;
    }

    [RelayCommand]
    private async Task RemoveFromFavoritesAsync()
    {
        await _tabbedViewModel.RemoveShowFromFavoritesAsync(new List<int> { TvShowVM.TvShow.Id }).ConfigureAwait(false);
        TvShowVM.IsFavorite = false;
    }

    public DetailsViewModel()
    {
        Helper.ThrowIfNotDesignMode();

        _tabbedViewModel = new TabbedViewModel();
        _tabbedViewModel.MainViewModel = new MainViewModel();
        _movieDbService = new DummyMovieDbService();
        var tvShow = _movieDbService.GetTvShowDetails(1, CancellationToken.None).Result;
        TvShowVM = new TvShowViewModel(tvShow, _tabbedViewModel.MainViewModel, true);
        _showBackButton = true;
    }
}