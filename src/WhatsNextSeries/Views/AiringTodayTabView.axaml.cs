﻿using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Controls.Primitives;
using Avalonia.Interactivity;
using Avalonia.VisualTree;
using WhatsNextSeries.Helpers;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Views;

public partial class AiringTodayTabView : UserControl
{
    private bool _isFirstTimeLoadingPage = true;

    public AiringTodayTabView()
    {
        InitializeComponent();
    }

    protected override void OnLoaded(RoutedEventArgs e)
    {
        base.OnLoaded(e);
        _isFirstTimeLoadingPage = false;
    }

    protected override async void OnSizeChanged(SizeChangedEventArgs e)
    {
        base.OnSizeChanged(e);

        if (AiringTodayScrollableViewer is not null && !_isFirstTimeLoadingPage)
        {
            await LoadNextPageForAiringTodayShows(AiringTodayScrollableViewer).ConfigureAwait(false);
        }
    }

    private async void ScrollViewer_OnScrollChanged(object? sender, ScrollChangedEventArgs e)
    {
        if (sender is not ScrollViewer scrollViewer)
        {
            return;
        }

        await LoadNextPageForAiringTodayShows(scrollViewer).ConfigureAwait(false);
    }

    private async Task LoadNextPageForAiringTodayShows(IScrollable scrollViewer)
    {
        if (DataContext is TabbedViewModel tabbedViewModel)
        {
            await LoadItemsUntilVisibleAreaIsFilled(tabbedViewModel, scrollViewer).ConfigureAwait(false);
        }
    }

    private async Task LoadItemsUntilVisibleAreaIsFilled(TabbedViewModel tabbedViewModel, IScrollable scrollViewer)
    {
        while (true)
        {
            if (Helper.IsScrollableAreaFilledOrEndReached(scrollViewer))
            {
                var loadedItemsCount = tabbedViewModel.AiringToday.Count;

                // Load the next page of items
                await tabbedViewModel.LoadNextPageForAirTodayShows().ConfigureAwait(true);

                if (tabbedViewModel.AiringToday.Count <= loadedItemsCount)
                {
                    // No more items to load
                    break;
                }
            }
            else
            {
                // If the content is scrollable and the visible area is not filled, exit the loop
                break;
            }
        }
    }
}