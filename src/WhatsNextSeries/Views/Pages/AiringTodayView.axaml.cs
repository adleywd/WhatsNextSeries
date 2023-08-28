using Avalonia.Controls;
using Avalonia.Controls.Primitives;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Views.Pages;

public partial class AiringTodayView : UserControl
{
    public AiringTodayView()
    {
        InitializeComponent();
    }
    
    private async void ScrollViewer_OnScrollChanged(object? sender, ScrollChangedEventArgs e)
    {
        if (sender is not ScrollViewer scrollViewer)
        {
            return;
        }

        if (!IsTheEndOfScrollViewer(scrollViewer))
        {
            return;
        }
        
        if(DataContext is MainViewViewModel mainViewViewModel)
        {
            await mainViewViewModel.LoadNextPageForAirTodayShows();
        }
    }
    
    private static bool IsTheEndOfScrollViewer(IScrollable scrollViewer)
    {
        return scrollViewer.Offset.Y + scrollViewer.Viewport.Height >= scrollViewer.Extent.Height;
    }

}