using Avalonia;
using Avalonia.Controls;
using Avalonia.Controls.Primitives;
using Avalonia.Media;

namespace WhatsNextSeries.Components;

public class TvShowCard : TemplatedControl
{
    public static readonly StyledProperty<string> TvShowNameProperty = 
        AvaloniaProperty.Register<TvShowCard, string>(nameof(TvShowName), "Tv Show Name");
   
    public string TvShowName
    {
        get => GetValue(TvShowNameProperty);
        set => SetValue(TvShowNameProperty, value);
    }
    
}