using System;
using System.Threading;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Markup.Xaml;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Views;

public partial class DetailsView : UserControl
{
    public DetailsView()
    {
        InitializeComponent();
        Loaded += async (sender, e) =>
        {
            if (DataContext is not DetailsViewModel detailsViewModel)
            {
                return;
            }

            using var cancellationTokenSource = new CancellationTokenSource();
            cancellationTokenSource.CancelAfter(TimeSpan.FromMinutes(2));
                
            await detailsViewModel.LoadDetailedShow(CancellationToken.None);

        };
    }

    // protected override Size MeasureCore(Size availableSize)
    // {
    //     OverViewTextBlock.Width = availableSize.Width - 50;
    //     return base.MeasureCore(availableSize);
    // }

    protected override void OnLoaded(RoutedEventArgs e)
    {
        // OverViewTextBlock.Width = 500;
        
        base.OnLoaded(e);
        
        // Set back button
        if (TopLevel.GetTopLevel(this) is { } topLevel)
        {
            topLevel.BackRequested += MainView_BackRequested;
        }
    }

    protected override void OnUnloaded(RoutedEventArgs e)
    {
        // Unset back button
        if (TopLevel.GetTopLevel(this) is { } topLevel)
        {
            topLevel.BackRequested -= MainView_BackRequested;
        }
        base.OnUnloaded(e);
    }
    
    private void MainView_BackRequested(object? sender, RoutedEventArgs e)
    {
        if (DataContext is not DetailsViewModel detailsViewModel)
        {
            return;
        }
        
        detailsViewModel.GoBackCommand.Execute(null);
        e.Handled = true;
    }
}