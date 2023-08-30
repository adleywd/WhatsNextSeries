using System.Collections.Generic;
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

    private readonly TabbedViewModel _ancestorViewModel;
    
    private readonly IMovieDbService _movieDbService;
    
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

        _tvShow = new TvShow
        {
            FirstAirDate = "2021-09-22",
            PrefixPosterLink = "https://image.tmdb.org/t/p/w342",
            Id = 1416,
            Name = "The Mandalorian",
            Overview = "Test overview of mandalorian is lorem ipsum dolor sit amet loire Mandalorian test overview.",
            PosterSize = "w342",
            BackDropSize = "w1280",
            VoteAverage = 8.2,
            Popularity = 3142.436,
            VoteCount = 2749,
            PosterPath = "/pZaFdrYekwC9ITq4yWrkqEwCy3E.jpg",
            BackdropPath = "/1HOBv1QxSbTwn5VyZ2vAVRhdR8e.jpg",
            GenreIds = new List<int>()
        };

        _ancestorViewModel = new TabbedViewModel();
        _movieDbService = new DummyMovieDbService();
    }
    
}