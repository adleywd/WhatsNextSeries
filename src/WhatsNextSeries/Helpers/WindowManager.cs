using Avalonia.Controls;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries.Helpers;

public class WindowManager : IWindowManager
{
    public void ShowWindow<T>(ViewModelBase viewModel) 
        where T : Window, new()
    {
        T view = new()
        {
            DataContext = viewModel,
        };
        
        view.Show();
    }
    
}