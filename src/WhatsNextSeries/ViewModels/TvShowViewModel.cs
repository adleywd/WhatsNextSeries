using System.Collections.Generic;
using System.Threading.Tasks;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.ViewModels;

public partial class TvShowViewModel : ViewModelBase
{
    public TvShowDetail TvShow { get; private set; }
    
    [ObservableProperty] private bool _isFavorite;
    
    private readonly MainViewModel _mainViewModel;

    public string PosterImageLink => !string.IsNullOrEmpty(TvShow.PosterPath)
        ? $"{TvShow.PrefixPosterLink}{TvShow.PosterSize}{TvShow.PosterPath}"
        : string.Empty;

    public string BackdropImageLink => !string.IsNullOrEmpty(TvShow.BackdropPath) 
        ? $"{TvShow.PrefixBackDropLink}{TvShow.BackDropSize}{TvShow.BackdropPath}"
        : string.Empty;

    public bool HasPosterImage => !string.IsNullOrEmpty(TvShow.PosterPath);
    public bool HasBackdropImage => !string.IsNullOrEmpty(TvShow.BackdropPath);
    
    public TvShowViewModel(TvShow tvShow, MainViewModel mainViewModel, bool isFavorite = false)
    {
        TvShow = new TvShowDetail(tvShow);
        IsFavorite = isFavorite;
        _mainViewModel = mainViewModel;
    }

    [RelayCommand]
    private void OpenShowDetails()
    {
        _mainViewModel.TabbedContent.OpenShowDetails(this);
    }
    
    [RelayCommand]
    private async Task AddFavorite()
    {
        if (IsFavorite)
        {
            return;
        }
        await _mainViewModel.TabbedContent.AddShowToFavoritesAsync(this).ConfigureAwait(false);
        IsFavorite = true;
    }

    [RelayCommand]
    private async Task RemoveFavorite()
    {
        IsFavorite = false;
        await _mainViewModel.TabbedContent.RemoveShowFromFavoritesAsync(new List<int> { TvShow.Id }).ConfigureAwait(false);
    }

    public void UpdateTvShowDetails(TvShowDetail tvShowDetail)
    {
        TvShow = tvShowDetail;
    }

}