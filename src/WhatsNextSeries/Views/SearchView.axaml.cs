using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;

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
}