using Avalonia.Controls;
using Avalonia.Controls.Primitives;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Views.Pages;

public partial class PopularView : UserControl
{
    public PopularView()
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
            await mainViewViewModel.LoadNextPageForPopularShows();
        }
    }
    
    private static bool IsTheEndOfScrollViewer(IScrollable scrollViewer)
    {
        return scrollViewer.Offset.Y + scrollViewer.Viewport.Height >= scrollViewer.Extent.Height;
    }
}