using Avalonia;
using Avalonia.Controls.Primitives;

namespace WhatsNextSeries.Components;

public class Navbar : TemplatedControl
{
    public static readonly StyledProperty<bool> IsDeleteShowsBtnEnabledProperty =
        AvaloniaProperty.Register<Navbar, bool>(nameof(IsDeleteShowsBtnEnabled), false);

    public bool IsDeleteShowsBtnEnabled
    {
        get => GetValue(IsDeleteShowsBtnEnabledProperty);
        set => SetValue(IsDeleteShowsBtnEnabledProperty, value);
    }
}