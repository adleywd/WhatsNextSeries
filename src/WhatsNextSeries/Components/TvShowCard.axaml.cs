using System;
using Avalonia;
using Avalonia.Controls.Primitives;

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