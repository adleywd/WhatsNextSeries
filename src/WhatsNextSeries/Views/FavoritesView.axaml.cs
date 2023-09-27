using Avalonia.Controls;
using Avalonia.Controls.Primitives;
using Avalonia.Input;
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
                await dataContext.LoadTvShowsFromFavorites().ConfigureAwait(true);
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

    private void TvShowCard_OnPointerPressed(object? sender, PointerPressedEventArgs e)
    {
        if (sender is not TvShowCard tvShowCard)
        {
            return;
        }

        var point = e.GetCurrentPoint(tvShowCard);
        if (!point.Properties.IsRightButtonPressed)
        {
            return;
        }

        FlyoutBase.ShowAttachedFlyout(tvShowCard);
    }
}