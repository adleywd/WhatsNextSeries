using System;
using System.Collections.ObjectModel;
using System.Threading;
using System.Threading.Tasks;
using CommunityToolkit.Mvvm.ComponentModel;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public partial class MainViewViewModel : ViewModelBase
{
    private readonly IMovieDbService _movieDbService;

    private int _currentPageForPopularShows = 1;
    private int _currentPageForAirTodayShows = 1;

    [ObservableProperty]
    private bool _isAiringTodayInitialLoading = true;

    [ObservableProperty]
    private bool _isPopularInitialLoading = true;
    
    [ObservableProperty]
    private bool _isAiringTodayLoadingMoreItems;
    
    [ObservableProperty]
    private bool _isPopularLoadingMoreItems;

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