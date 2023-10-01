using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Controls.Primitives;
using Avalonia.Input;
using Avalonia.Interactivity;
using Avalonia.Styling;
using Avalonia.VisualTree;
using WhatsNextSeries.Components;
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

    private void TvShowCard_OnTapped(object? sender, TappedEventArgs e)
    {
        if (sender is not TvShowCard tvShowCard)
        {
            return;
        }
        
        if (tvShowCard.DataContext is TvShowViewModel tvShowViewModel)
        {
            tvShowViewModel.OpenShowDetailsCommand.Execute(null);
        }
    }

    private void TvShowCard_OnPointerMoved(object? sender, PointerEventArgs e)
    {
        if (sender is not TvShowCard tvShowCard)
        {
            return;
        }
        
        tvShowCard.Cursor = new Cursor(StandardCursorType.Hand);
    }
    
    private void TvShowCard_OnHolding(object? sender, HoldingRoutedEventArgs e)
    {
        if (sender is not TvShowCard tvShowCard)
        {
            return;
        }

        FlyoutBase.ShowAttachedFlyout(tvShowCard);
    }
}