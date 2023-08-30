using Avalonia.Controls;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Helpers;

public interface IWindowManager
{
    public void ShowWindow<T>(ViewModelBase viewModel) where T : Window, new();
}