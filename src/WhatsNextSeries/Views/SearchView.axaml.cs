using Avalonia;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Markup.Xaml;
using WhatsNextSeries.Components;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Views;

public partial class SearchView : UserControl
{
    public SearchView()
    {
        InitializeComponent();
    }

    private void ScrollViewer_OnScrollChanged(object? sender, ScrollChangedEventArgs e)
    {
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