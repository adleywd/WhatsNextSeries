using System;
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

    private bool _isAiringTodayInitialLoading;
    public bool IsAiringTodayInitialLoading 
    {
        get => _isAiringTodayInitialLoading;
        set => this.RaiseAndSetIfChanged(ref _isAiringTodayInitialLoading, value);
    }

    private bool _isPopularInitialLoading;
    public bool IsPopularInitialLoading 
    {
        get => _isPopularInitialLoading;
        set => this.RaiseAndSetIfChanged(ref _isPopularInitialLoading, value);
    }
    
    private bool _isAiringTodayLoadingMoreItems;
    public bool IsAiringTodayLoadingMoreItems 
    {
        get => _isAiringTodayLoadingMoreItems;
        set => this.RaiseAndSetIfChanged(ref _isAiringTodayLoadingMoreItems, value);
    }
    
    private bool _isPopularLoadingMoreItems;
    public bool IsPopularLoadingMoreItems 
    {
        get => _isPopularLoadingMoreItems;
        set => this.RaiseAndSetIfChanged(ref _isPopularLoadingMoreItems, value);
    }

    public ObservableCollection<PopularViewModel> PopularShows { get; } = new();

    public ObservableCollection<AiringTodayViewModel> AiringToday { get; } = new();

    public MainViewViewModel(IMovieDbService movieDbService)
    {
        _movieDbService = movieDbService;
        GetDataForPopularShows();
        GetDataForAirTodayShows();
    }

    public void LoadNextPageForPopularShows()
    {
        IsPopularLoadingMoreItems = true;
        LoadPopularShows(++_currentPageForPopularShows);
    }

    public void LoadNextPageForAirTodayShows()
    {
        IsAiringTodayLoadingMoreItems = true;
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
        
        SetFalseAiringTodayLoading();
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

        SetFalsePopularLoading();
    }
    
    private void GetDataForPopularShows()
    {
        IsPopularInitialLoading = true;
        RxApp.MainThreadScheduler.Schedule(() => LoadPopularShows(1));
    }

    private void GetDataForAirTodayShows()
    {
        IsAiringTodayInitialLoading = true;
        RxApp.MainThreadScheduler.Schedule(() => LoadAiringTodayShows(1));
    }

    private void SetFalseAiringTodayLoading()
    {
        IsAiringTodayInitialLoading = false;
        IsAiringTodayLoadingMoreItems = false;
    }

    private void SetFalsePopularLoading()
    {
        IsPopularInitialLoading = false;
        IsPopularLoadingMoreItems = false;
    }
    
    public MainViewViewModel()
    {
        if (Avalonia.Controls.Design.IsDesignMode == false)
        {
            throw new InvalidOperationException("This empty constructor should only be used in design mode.");
        }

        _movieDbService = new DummyMovieDbService();
        GetDataForPopularShows();
        GetDataForAirTodayShows();
    }
}