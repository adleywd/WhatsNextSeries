using System;
using System.Collections.ObjectModel;
using System.Reactive.Concurrency;
using System.Threading;
using System.Threading.Tasks;
using ReactiveUI;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public class MainViewViewModel : ViewModelBase
{
    private readonly IMovieDbService _movieDbService;

    private int _currentPageForPopularShows = 1;
    private int _currentPageForAirTodayShows = 1;

    private bool _isAiringTodayInitialLoading = true;
    public bool IsAiringTodayInitialLoading 
    {
        get => _isAiringTodayInitialLoading;
        set => this.RaiseAndSetIfChanged(ref _isAiringTodayInitialLoading, value);
    }

    private bool _isPopularInitialLoading = true;
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
    }

    public async Task LoadNextPageForPopularShows()
    {
        if (IsPopularLoadingMoreItems || _currentPageForPopularShows > 1000)
        {
            return;
        }
        
        IsPopularLoadingMoreItems = true;
        await LoadPopularShows(_currentPageForPopularShows);
        _currentPageForPopularShows++;
    }

    public async Task LoadNextPageForAirTodayShows()
    {
        if (IsAiringTodayLoadingMoreItems || _currentPageForAirTodayShows > 1000)
        {
            return;
        }
        
        IsAiringTodayLoadingMoreItems = true;
        await LoadAiringTodayShows(_currentPageForAirTodayShows);
        _currentPageForAirTodayShows++;
    }

    private async Task LoadAiringTodayShows(int page)
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
        catch (OperationCanceledException e)
        {
            // Timeout error display
        }
        catch (Exception e)
        {
            // ignored
        }
        
        SetFalseAiringTodayLoading();
    }

    private async Task LoadPopularShows(int page)
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
        catch (OperationCanceledException e)
        {
            // Timeout error display
        }
        catch (Exception e)
        {
            // ignored
        }

        SetFalsePopularLoading();
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
    }
}