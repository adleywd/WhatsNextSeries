using System.Collections.Generic;
using Avalonia.Controls;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Services;

public interface IWindowManager
{
    public Dictionary<string, Window> OpenedWindows { get; }
    
    public void ShowWindow<T>(ViewModelBase viewModel, string? ancestorWindowName = null, bool showAsDialog = false) 
        where T : Window, new();
}