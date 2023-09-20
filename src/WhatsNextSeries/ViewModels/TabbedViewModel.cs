﻿using System;
using System.Threading;
using Avalonia.Controls;
using CommunityToolkit.Mvvm.ComponentModel;
using WhatsNextSeries.DataServices;
using WhatsNextSeries.Helpers;
using WhatsNextSeries.Models;
using WhatsNextSeries.Services;
using WhatsNextSeries.Views;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel : ViewModelBase
{
    [ObservableProperty] private MainViewModel _mainViewModel;

    private readonly IMovieDbService _movieDbService;
    private readonly IWindowManager _windowManager;
    private readonly IFavoritesDataService _favoritesDataService;

    public TabbedViewModel(MainViewModel mainViewModel, 
        IMovieDbService movieDbService, 
        IWindowManager windowManager,
        IFavoritesDataService favoritesDataService)
    {
        _mainViewModel = mainViewModel;
        _movieDbService = movieDbService;
        _windowManager = windowManager;
        _favoritesDataService = favoritesDataService;
    }

    public void OpenShowDetails(TvShow tvShow)
    {
        var detailsViewModel = new DetailsViewModel(this, tvShow, _movieDbService, _favoritesDataService);
        if (Helper.IsMobile())
        {
            detailsViewModel.ShowBackButton = true;
            MainViewModel.ContentViewModel = detailsViewModel;
        }
        else
        {
            detailsViewModel.ShowBackButton = false;
            _windowManager.ShowWindow<DetailsWindow>(detailsViewModel, nameof(MainWindow), false);
        }
    }

    public TabbedViewModel()
    {
        Helper.ThrowIfNotDesignMode();

        _movieDbService = new DummyMovieDbService();
        _windowManager = new WindowManager();
        _mainViewModel = new MainViewModel(_movieDbService, _windowManager, new FavoritesFileManagerDataService());
    }
}