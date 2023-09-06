using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading.Tasks;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel
{
    [ObservableProperty]
    private string _searchText = string.Empty;
    
    public ObservableCollection<string> SearchResults { get; } = new();
    
    [RelayCommand]
    private Task SearchAsync()
    {
        SearchResults.Add("test");
        return Task.CompletedTask;
    }

}
