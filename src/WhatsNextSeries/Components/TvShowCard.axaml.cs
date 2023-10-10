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

    public static readonly StyledProperty<bool> IsSelectableProperty =
        AvaloniaProperty.Register<TvShowCard, bool>(nameof(IsSelectable), true);

    public static readonly StyledProperty<bool> IsSelectedProperty =
        AvaloniaProperty.Register<TvShowCard, bool>(nameof(IsSelected), true);

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

    public bool IsSelectable
    {
        get => GetValue(IsSelectableProperty);
        set => SetValue(IsSelectableProperty, value);
    }

    public bool IsSelected
    {
        get => GetValue(IsSelectedProperty);
        set => SetValue(IsSelectedProperty, value);
    }
    
}