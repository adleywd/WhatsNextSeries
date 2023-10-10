using Avalonia;
using Avalonia.Controls;
using Avalonia.Input;

namespace WhatsNextSeries.Components;

public partial class Navbar : UserControl
{
    public static readonly StyledProperty<bool> IsDeleteShowsEnabledProperty =
        AvaloniaProperty.Register<TvShowCard, bool>(nameof(IsDeleteShowsEnabled), false);

    public bool IsDeleteShowsEnabled
    {
        get => GetValue(IsDeleteShowsEnabledProperty);
        set => SetValue(IsDeleteShowsEnabledProperty, value);
    }
    
    public Navbar()
    {
        InitializeComponent();
    }
    
    private void DeleteShowsBtn_OnTapped(object? sender, TappedEventArgs e)
    {
        if (sender is Control control)
        {
            
        }
    }
}