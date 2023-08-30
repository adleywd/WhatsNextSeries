﻿using System;
using System.Collections.ObjectModel;
using System.Threading;
using System.Threading.Tasks;
using CommunityToolkit.Mvvm.ComponentModel;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel
{
    private int _popularLastPageLoaded = 0;
    
    private int _currentPageForPopularShows = 1;


    [ObservableProperty]
    private bool _isPopularInitialLoading = true;
    
    [ObservableProperty]
    private bool _isPopularLoadingMoreItems = false;

    public ObservableCollection<TvShowViewModel> PopularShows { get; } = new();
    
    public async Task LoadNextPageForPopularShows()
    {
        if (IsPopularLoadingMoreItems || _currentPageForPopularShows > 1000)
        {
            return;
        }
        
        if (_currentPageForPopularShows <= _popularLastPageLoaded)
        {
            return;
        }
        
        IsPopularLoadingMoreItems = true;
        await LoadPopularShows().ConfigureAwait(false);
        SetFalsePopularLoading();
    }
    
    private async Task LoadPopularShows()
    {
        try
        {
            var cancellationToken = new CancellationTokenSource(120000).Token; // 2 minutes timeout
            var popularShows = await _movieDbService.GetPopularShows(_currentPageForPopularShows, cancellationToken).ConfigureAwait(false);
            foreach (var popularShow in popularShows)
            {
                PopularShows.Add(new TvShowViewModel(popularShow, MainViewModel));
            }
            _popularLastPageLoaded = _currentPageForPopularShows;
            _currentPageForPopularShows++;
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
    
    private void SetFalsePopularLoading()
    {
        IsPopularInitialLoading = false;
        IsPopularLoadingMoreItems = false;
    }
}