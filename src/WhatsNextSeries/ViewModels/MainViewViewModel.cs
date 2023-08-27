using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Reactive.Concurrency;
using System.Threading;
using ReactiveUI;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public class MainViewViewModel : ViewModelBase
{
    private readonly IMovieDbService _movieDbService;

    private int _currentPageForPopularShows = 1;
    private int _currentPageForAirTodayShows = 1;

    public ObservableCollection<PopularViewModel> PopularShows { get; } = new();

    public ObservableCollection<AiringTodayViewModel> AiringToday { get; } = new();

    public MainViewViewModel(IMovieDbService movieDbService)
    {
        _movieDbService = movieDbService;
        StartPopulationForPopularShows();
        StartPopulationForAirTodayShows();
    }

    public void LoadNextPageForPopularShows()
    {
        LoadPopularShows(++_currentPageForPopularShows);
    }

    public void LoadNextPageForAirTodayShows()
    {
        LoadAiringTodayShows(++_currentPageForAirTodayShows);
    }

    private async void LoadAiringTodayShows(int page)
    {
        try
        {
            var cancellationToken = new CancellationTokenSource(120000).Token; // 2 minutes timeout
            var airingTodayShows =
                await _movieDbService.GetAiringTodayShows(page, cancellationToken).ConfigureAwait(false);
            foreach (var todayShow in airingTodayShows)
            {
                AiringToday.Add(new AiringTodayViewModel(todayShow));
            }
        }
        catch (Exception e)
        {
            // ignored
        }
    }

    private async void LoadPopularShows(int page)
    {
        try
        {
            var cancellationToken = new CancellationTokenSource(120000).Token; // 2 minutes timeout
            var popularShows = await _movieDbService.GetPopularShows(page, cancellationToken).ConfigureAwait(false);
            foreach (var popularShow in popularShows)
            {
                PopularShows.Add(new PopularViewModel(popularShow));
            }
        }
        catch (Exception e)
        {
            // ignored
        }
    }

    private void StartPopulationForPopularShows()
    {
        RxApp.MainThreadScheduler.Schedule(() => LoadPopularShows(1));
    }

    private void StartPopulationForAirTodayShows()
    {
        RxApp.MainThreadScheduler.Schedule(() => LoadAiringTodayShows(1));
    }

    public MainViewViewModel()
    {
        if (Avalonia.Controls.Design.IsDesignMode == false)
        {
            throw new InvalidOperationException("This empty constructor should only be used in design mode.");
        }

        _movieDbService = new DummyMovieDbService();
        StartPopulationForPopularShows();
        StartPopulationForAirTodayShows();
    }
}