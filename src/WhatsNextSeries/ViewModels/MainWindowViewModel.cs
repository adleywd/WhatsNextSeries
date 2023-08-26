using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading;
using System.Threading.Tasks;
using CommunityToolkit.Mvvm.ComponentModel;
using WhatsNextSeries.Models;
using WhatsNextSeries.Services;
using WhatsNextSeries.Views.UserControlViews;

namespace WhatsNextSeries.ViewModels;

public partial class MainWindowViewModel : ViewModelBase
{
    private readonly IMovieDbService _movieDbService;

    [ObservableProperty] private ObservableCollection<TvShow> _popularShows = new();

    [ObservableProperty] private ObservableCollection<TvShow> _airingToday = new();

    public MainWindowViewModel(IMovieDbService movieDbService)
    {
        _movieDbService = movieDbService;
        var cancellationToken = new CancellationTokenSource(120000).Token; // 2 minutes timeout
        Task.Run(async () =>
        {
            await LoadPopularShows(cancellationToken).ConfigureAwait(true);
            await LoadAiringTodayShows(cancellationToken).ConfigureAwait(true);
        });
    }

    private async Task LoadAiringTodayShows(CancellationToken cancellationToken)
    {
        try
        {
            AiringToday =
                new ObservableCollection<TvShow>(await _movieDbService.GetAiringTodayShows(cancellationToken).ConfigureAwait(true));
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw;
        }
    }

    private async Task LoadPopularShows(CancellationToken cancellationToken)
    {
        try
        {
            PopularShows =
                new ObservableCollection<TvShow>(await _movieDbService.GetPopularShows(cancellationToken).ConfigureAwait(true));
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw;
        }
    }

    public MainWindowViewModel()
    {
        if (Avalonia.Controls.Design.IsDesignMode == false)
        {
            throw new InvalidOperationException("This empty constructor should only be used in design mode.");
        }

        _movieDbService = new DummyMovieDbService();
        Task.Run(async () =>
        {
            await LoadPopularShows(CancellationToken.None).ConfigureAwait(true);
            await LoadAiringTodayShows(CancellationToken.None).ConfigureAwait(true);
        });
    }
}