using System;
using Avalonia;
using Avalonia.Controls.Primitives;

namespace WhatsNextSeries.Components;

public class TvShowCard : TemplatedControl
{
    public static readonly StyledProperty<string> TvShowNameProperty = 
        AvaloniaProperty.Register<TvShowCard, string>(nameof(TvShowName), "Tv Show Name");
   
    public static readonly StyledProperty<string> TvShowPosterImageProperty = 
        AvaloniaProperty.Register<TvShowCard, string>(nameof(TvShowPosterImage), "");
   
    public static readonly StyledProperty<bool> HidePosterPlaceHolderProperty = 
        AvaloniaProperty.Register<TvShowCard, bool>(nameof(HidePosterPlaceHolder), true);

    public string TvShowName
    {
        get => GetValue(TvShowNameProperty);
        set => SetValue(TvShowNameProperty, value);
    }

    public string TvShowPosterImage
    {
        get => GetValue(TvShowPosterImageProperty);
        set => SetValue(TvShowPosterImageProperty, value);
    }
    
    public bool HidePosterPlaceHolder
    {
        get => GetValue(HidePosterPlaceHolderProperty);
        set => SetValue(HidePosterPlaceHolderProperty, value);
    }
    
}