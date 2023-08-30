using System.Collections.Generic;
using System.Linq;
using Avalonia.Controls;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Services;

public class WindowManager : IWindowManager
{
    public Dictionary<string, Window> OpenedWindows { get; } = new();
    
    public void ShowWindow<T>(ViewModelBase viewModel, string? ancestorWindowName = null, bool showAsDialog = false) 
        where T : Window, new()
    {
        T view = new()
        {
            DataContext = viewModel
        };

        if (string.IsNullOrEmpty(ancestorWindowName))
        {
            view.Show();
            return;
        }

        if (!OpenedWindows.ContainsKey(ancestorWindowName))
        {
            view.Show();
            return;
        }

        var ancestorWindow = GetWindowByName(ancestorWindowName);
        if (ancestorWindow is null)
        {
            view.Show();
            return;
        }

        if (showAsDialog)
        {
            view.ShowDialog(ancestorWindow);
        }
        else
        {
            view.Show(ancestorWindow);
        }
    }

    private Window? GetWindowByName(string windowName)
    {
        return OpenedWindows.TryGetValue(windowName, out var retrievedWindow) ? retrievedWindow : null;
    }
    
}