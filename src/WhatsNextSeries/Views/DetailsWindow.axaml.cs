using Avalonia.Controls;
using Avalonia.Interactivity;

namespace WhatsNextSeries.Views;

public partial class DetailsWindow : Window
{
    public DetailsWindow()
    {
        InitializeComponent();
    }
    
    protected override void OnLoaded(RoutedEventArgs e)
    {
        base.OnLoaded(e);
        
        if (Owner is null)
        {
            return;
        }
        
        var width = Owner.ClientSize.Width / 2f;
        var height = Owner.ClientSize.Height / 2f;

        Width = width;
        Height = height;
    }
}