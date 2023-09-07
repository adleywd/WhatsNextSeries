using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading.Tasks;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel
{
    [ObservableProperty]
    [NotifyCanExecuteChangedFor(nameof(DoSearchCommand))]
    private string _searchText = string.Empty;

    partial void OnSearchTextChanged(string? oldValue, string newValue)
    {
        if (newValue.Length > 2 && newValue != oldValue)
        {
            DoSearchCommand.ExecuteAsync(null).ConfigureAwait(true);
        }
    }

    public ObservableCollection<string> SearchResults { get; } = new();
    
    [RelayCommand]
    private async Task DoSearchAsync()
    {
        SearchResults.Clear();
        SearchResults.Add(SearchText);
    }

}
