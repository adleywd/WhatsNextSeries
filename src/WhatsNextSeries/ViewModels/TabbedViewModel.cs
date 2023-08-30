using System;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using WhatsNextSeries.Helpers;
using WhatsNextSeries.Models;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel : ViewModelBase
{
    [ObservableProperty]
    private MainViewModel _mainViewModel;
    
    private readonly IMovieDbService _movieDbService;
    
    public TabbedViewModel(MainViewModel mainViewModel, IMovieDbService movieDbService)
    {
        _mainViewModel = mainViewModel;
        _movieDbService = movieDbService;
    }

    public void OpenShowDetails(TvShow tvShow)
    {
        var detailsViewModel = new DetailsViewModel(this, tvShow, _movieDbService);
        MainViewModel.ContentViewModel = detailsViewModel;
    }
    
    public TabbedViewModel()
    {
        Helper.ThrowIfNotDesignMode();

        _movieDbService = new DummyMovieDbService();
        _mainViewModel = new MainViewModel(_movieDbService);
    }
}