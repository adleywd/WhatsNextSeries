using System;
using System.Collections.ObjectModel;
using System.Threading;
using System.Threading.Tasks;
using CommunityToolkit.Mvvm.ComponentModel;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel
{
    private int _airTodayLastPageLoaded = 0;
    
    private int _airTodayCurrentPage = 1;
    
    [ObservableProperty]
    private bool _isAiringTodayInitialLoading = true;
    
    [ObservableProperty]
    private bool _isAiringTodayLoadingMoreItems;

    public ObservableCollection<TvShowViewModel> AiringToday { get; } = new();
    
    public async Task LoadNextPageForAirTodayShows()
    {
        if (IsAiringTodayLoadingMoreItems || _airTodayCurrentPage > 1000)
        {
            return;
        }

        if (_airTodayCurrentPage <= _airTodayLastPageLoaded)
        {
            return;
        }
        
        IsAiringTodayLoadingMoreItems = true;
        await GetAiringTodayShows().ConfigureAwait(false);
        DisableProgressBarForAiringToday();

    }
    
    private async Task GetAiringTodayShows()
    {
        try
        {
            var cancellationToken = new CancellationTokenSource(120000).Token; // 2 minutes timeout
            var airingTodayShows =
                await _movieDbService.GetAiringTodayShows(_airTodayCurrentPage, cancellationToken).ConfigureAwait(false);
            foreach (var todayShow in airingTodayShows)
            {
                AiringToday.Add(new TvShowViewModel(todayShow, MainViewModel));
            }
            _airTodayLastPageLoaded = _currentPageForPopularShows;
            _airTodayCurrentPage++;

        }
        catch (OperationCanceledException e)
        {
            // Timeout error display
        }
        catch (Exception e)
        {
            // ignored
        }
    }
    
    private void DisableProgressBarForAiringToday()
    {
        IsAiringTodayInitialLoading = false;
        IsAiringTodayLoadingMoreItems = false;
    }
}