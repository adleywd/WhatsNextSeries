using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading.Tasks;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel
{
    [ObservableProperty]
    private bool _isSearching;
    
    [ObservableProperty]
    [NotifyCanExecuteChangedFor(nameof(DoSearchCommand))]
    private string _searchText = string.Empty;

    partial void OnSearchTextChanged(string? oldValue, string newValue)
    {
        if (newValue.Length > 0 && newValue != oldValue)
        {
            DoSearchCommand.ExecuteAsync(null).ConfigureAwait(true);
        }
    }

    public ObservableCollection<TvShowViewModel> SearchResults { get; } = new();
    
    [RelayCommand]
    private async Task DoSearchAsync()
    {
        SearchResults.Clear();
        
        IsSearching = true;   
        
        var tvShowList = await _movieDbService.GetTvShowsByName(SearchText).ConfigureAwait(true);
        foreach (var tvShow in tvShowList)
        {
            SearchResults.Add(new TvShowViewModel(tvShow, MainViewModel));
        }
        
        IsSearching = false;
    }

}
