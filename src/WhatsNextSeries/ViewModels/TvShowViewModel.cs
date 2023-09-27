using System.Collections.Generic;
using System.Threading.Tasks;
using Avalonia.Controls;
using CommunityToolkit.Mvvm.Input;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.ViewModels;

public partial class TvShowViewModel : ViewModelBase
{
    private readonly TvShowDetail _tvShow;
    private readonly MainViewModel _mainViewModel;

    public int Id => _tvShow.Id;

    public string Name => _tvShow.Name;

    public string PosterImageLink => !string.IsNullOrEmpty(_tvShow.PosterPath)
        ? $"{_tvShow.PrefixPosterLink}{_tvShow.PosterSize}{_tvShow.PosterPath}"
        : string.Empty;

    public string BackdropImageLink => $"{_tvShow.PrefixBackDropLink}{_tvShow.BackDropSize}{_tvShow.BackdropPath}";

    public bool HasPosterImage => !string.IsNullOrEmpty(_tvShow.PosterPath);
    public bool HasBackdropImage => !string.IsNullOrEmpty(_tvShow.BackdropPath);

    [RelayCommand]
    private void OpenShowDetails()
    {
        _mainViewModel.TabbedContent.OpenShowDetails(_tvShow);
    }
    
    [RelayCommand]
    private async Task AddFavorite()
    {
        await _mainViewModel.TabbedContent.AddShowToFavoritesAsync(_tvShow).ConfigureAwait(false);
    }

    [RelayCommand]
    private async Task RemoveFavorite()
    {
        await _mainViewModel.TabbedContent.RemoveTvShowAsync(new List<int> { _tvShow.Id }).ConfigureAwait(false);
    }

    public TvShowViewModel(TvShow tvShow, MainViewModel mainViewModel)
    {
        _tvShow = new TvShowDetail(tvShow);
        _mainViewModel = mainViewModel;
    }
}