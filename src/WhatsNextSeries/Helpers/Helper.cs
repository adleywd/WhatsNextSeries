using System;

namespace WhatsNextSeries.Helpers;

public static class Helper
{
    public static bool IsInDesignMode => Avalonia.Controls.Design.IsDesignMode;
    
    public static void ThrowIfNotDesignMode()
    {
        if (!IsInDesignMode)
        {
            throw new InvalidOperationException("This method should only be used in design mode.");
        }
    }

    public static bool IsMobile()
    {
        // Useful if you want to use the same code for mobile and desktop inside a browser too.
        // RuntimePlatformInfo? runtimeInfo = AvaloniaLocator.Current.GetService<IRuntimePlatform>()?.GetRuntimeInfo();
        // bool isMobile = runtimeInfo?.IsMobile ?? false;
        return OperatingSystem.IsAndroid() || OperatingSystem.IsIOS();
    }
}