using System.Collections.Generic;
using System.Linq;
using System.Threading;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using WhatsNextSeries.Helpers;
using WhatsNextSeries.Models;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public partial class DetailsViewModel : ViewModelBase
{
    [ObservableProperty]
    private TvShow _tvShow;

    [ObservableProperty]
    private bool _showBackButton;
    
    private readonly TabbedViewModel _ancestorViewModel;
    
    private readonly IMovieDbService _movieDbService;
    
    public string PosterImageLink => $"{TvShow.PrefixPosterLink}{TvShow.PosterSize}{TvShow.PosterPath}";
    public string BackdropImageLink => $"{TvShow.PrefixBackDropLink}{TvShow.BackDropSize}{TvShow.BackdropPath}";
    public bool HasPosterImage => !string.IsNullOrEmpty(TvShow.PosterPath);
    public bool HasBackdropImage => !string.IsNullOrEmpty(TvShow.BackdropPath);
    
    
    public DetailsViewModel(TabbedViewModel ancestorViewModel, TvShow show, IMovieDbService movieDbService)
    {
        _ancestorViewModel = ancestorViewModel;
        _tvShow = show;
        _movieDbService = movieDbService;
    }

    [RelayCommand]
    private void GoBack()
    {
        _ancestorViewModel.MainViewModel.ContentViewModel = _ancestorViewModel;
    }
    
    public DetailsViewModel()
    {
        Helper.ThrowIfNotDesignMode();

        _ancestorViewModel = new TabbedViewModel();
        _movieDbService = new DummyMovieDbService();
        _tvShow = _movieDbService.GetPopularShows(1, CancellationToken.None).Result.First();
        _showBackButton = true;
    }
    
}