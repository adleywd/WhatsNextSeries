using System;
using Avalonia.Controls.Primitives;

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
    
    public static bool IsScrollableAreaFilledOrEndReached(IScrollable scrollViewer)
    {
        var contentHeight = scrollViewer.Extent.Height;
        var viewportHeight = scrollViewer.Viewport.Height;
        var verticalOffset = scrollViewer.Offset.Y;

        var isScrollable = contentHeight > viewportHeight;

        return !isScrollable || verticalOffset + viewportHeight >= contentHeight;
    }
}