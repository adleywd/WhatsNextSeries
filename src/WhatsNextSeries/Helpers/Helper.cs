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
}