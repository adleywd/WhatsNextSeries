using System;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Controls.Primitives;
using Avalonia.Input;
using Avalonia.Interactivity;
using Avalonia.VisualTree;
using WhatsNextSeries.Components;
using WhatsNextSeries.Helpers;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Views
{
    public partial class PopularTabView : UserControl
    {
        private bool _isFirstTimeLoadingPage = true;
        public PopularTabView()
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
            this.GetVisualChildren();
            
            if (PopularTabViewScrollViewer is not null && !_isFirstTimeLoadingPage)
            {
                await LoadNextPageForPopularShows(PopularTabViewScrollViewer).ConfigureAwait(false);
            }
        }

        private async void ScrollViewer_OnScrollChanged(object? sender, ScrollChangedEventArgs e)
        {
            if (sender is not ScrollViewer scrollViewer)
            {
                return;
            }

            await LoadNextPageForPopularShows(scrollViewer).ConfigureAwait(false);
        }

        private async Task LoadNextPageForPopularShows(IScrollable scrollViewer)
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
                    var loadedItemsCount = tabbedViewModel.PopularShows.Count;

                    // Load the next page of items
                    await tabbedViewModel.LoadNextPageForPopularShows().ConfigureAwait(true);
                    
                    // if(!WrapPanelIsFillingVisibleArea(PopularShowsWrapPanel, scrollViewer.Viewport.Height))
                    // {
                    //     // If the content is scrollable and the visible area is not filled, exit the loop
                    //     break;
                    // }
                    
                    if (tabbedViewModel.PopularShows.Count <= loadedItemsCount)
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
        // https://stackoverflow.com/questions/21002237/wrappanel-with-lastchildfill
        // private bool WrapPanelIsFillingVisibleArea(WrapPanel wrapPanel, double viewportHeight)
        // {
        //     double totalItemHeight = CalculateAverageItemHeight(wrapPanel) * wrapPanel.Children.Count;
        //     return totalItemHeight >= viewportHeight;
        // }
        //
        // private double CalculateAverageItemHeight(WrapPanel wrapPanel)
        // {
        //     double totalHeight = 0;
        //
        //     foreach (Control child in wrapPanel.Children)
        //     {
        //         totalHeight += child.Bounds.Height; // Adjust as needed
        //     }
        //
        //     double averageHeight = totalHeight / wrapPanel.Children.Count;
        //     return averageHeight;
        // }
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
}