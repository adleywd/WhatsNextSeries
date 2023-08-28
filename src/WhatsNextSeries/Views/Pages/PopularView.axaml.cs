using Avalonia.Controls;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Views.Pages;

public partial class PopularView : UserControl
{
    public PopularView()
    {
        InitializeComponent();
    }

    private void ScrollViewer_OnScrollChanged(object? sender, ScrollChangedEventArgs e)
    {
        if (sender is not ScrollViewer scrollViewer)
        {
            return;
        }
        if (IsTheEndOfScrollViewer(scrollViewer))
        {
            if(DataContext is MainViewViewModel mainViewViewModel)
            {
                mainViewViewModel.LoadNextPageForPopularShows();
            }
        }
    }
    
    private bool IsTheEndOfScrollViewer(ScrollViewer scrollViewer)
    {
        return scrollViewer.Offset.Y + scrollViewer.Viewport.Height >= scrollViewer.Extent.Height;
    }
}