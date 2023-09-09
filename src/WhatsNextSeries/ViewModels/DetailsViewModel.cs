using System;
using System.Diagnostics;
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
    [ObservableProperty]
    private TvShowDetail _tvShow;

    [ObservableProperty]
    private bool _showBackButton;

    private readonly TabbedViewModel _ancestorViewModel;

    private readonly IMovieDbService _movieDbService;
    private readonly ITvShowFileManager _tvShowFileManager;

    public string PosterImageLink => $"{TvShow.PrefixPosterLink}{TvShow.PosterSize}{TvShow.PosterPath}";
    public string BackdropImageLink => $"{TvShow.PrefixBackDropLink}{TvShow.BackDropSize}{TvShow.BackdropPath}";
    public bool HasPosterImage => !string.IsNullOrEmpty(TvShow.PosterPath);
    public bool HasBackdropImage => !string.IsNullOrEmpty(TvShow.BackdropPath);


    public DetailsViewModel(TabbedViewModel ancestorViewModel, TvShow show, IMovieDbService movieDbService, ITvShowFileManager tvShowFileManager)
    {
        _ancestorViewModel = ancestorViewModel;
        _tvShow = new TvShowDetail(show);
        _movieDbService = movieDbService;
        _tvShowFileManager = tvShowFileManager;
    }

    public async Task LoadDetailedShow(CancellationToken cancellationToken)
    {
        var show = await _movieDbService.GetTvShowDetails(TvShow.Id, cancellationToken).ConfigureAwait(false);

        // No tv show found
        if (show.Id == 0)
        {
            return;
        }
        
        TvShow = show;
    }

    [RelayCommand]
    private void GoBack()
    {
        _ancestorViewModel.MainViewModel.ContentViewModel = _ancestorViewModel;
    }

    [RelayCommand]
    private async Task AddToFavoritesAsync()
    {
        await _ancestorViewModel.AddShowToFavoritesAsync(TvShow).ConfigureAwait(false);
    }

    public DetailsViewModel()
    {
        Helper.ThrowIfNotDesignMode();

        _ancestorViewModel = new TabbedViewModel();
        _movieDbService = new DummyMovieDbService();
        _tvShowFileManager = new TvShowFileManager();
        _tvShow = _movieDbService.GetTvShowDetails(1, CancellationToken.None).Result;
        _showBackButton = true;
    }
}