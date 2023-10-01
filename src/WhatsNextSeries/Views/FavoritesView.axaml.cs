using Avalonia;
using Avalonia.Controls;
using Avalonia.Controls.Primitives;
using Avalonia.Input;
using Avalonia.Threading;
using WhatsNextSeries.Components;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Views;

public partial class FavoritesView : UserControl
{
    public FavoritesView()
    {
        InitializeComponent();
        Loaded += async (sender, args) =>
        {
            if (DataContext is TabbedViewModel dataContext)
            {
                Dispatcher
                    .UIThread
                    .Post(() =>
                        dataContext.LoadTvShowsFromFavorites().ConfigureAwait(true), DispatcherPriority.Background);
            }
        };
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