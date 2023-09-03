using Avalonia.Controls;
using Avalonia.Input;
using WhatsNextSeries.Components;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Views;

public partial class FavoritesView : UserControl
{
    public FavoritesView()
    {
        InitializeComponent();
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
}