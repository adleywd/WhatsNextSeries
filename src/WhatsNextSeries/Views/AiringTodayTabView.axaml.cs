using Avalonia.Controls;
using Avalonia.Controls.Primitives;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Views;

public partial class AiringTodayTabView : UserControl
{
    public AiringTodayTabView()
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
        
        if(DataContext is TabbedViewModel tabbedViewModel)
        {
            await tabbedViewModel.LoadNextPageForAirTodayShows();
        }
    }
    
    private static bool IsTheEndOfScrollViewer(IScrollable scrollViewer)
    {
        return scrollViewer.Offset.Y + scrollViewer.Viewport.Height >= scrollViewer.Extent.Height;
    }

}