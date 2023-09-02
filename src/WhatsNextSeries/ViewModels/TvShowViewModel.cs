using Avalonia.Controls;
using CommunityToolkit.Mvvm.Input;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.ViewModels;

public partial class TvShowViewModel : ViewModelBase
{
    private readonly TvShow _tvShow;
    private readonly MainViewModel _mainViewModel;

    public string Name => _tvShow.Name;

    public string PosterImageLink => !string.IsNullOrEmpty(_tvShow.PosterPath)
        ? $"{_tvShow.PrefixPosterLink}{_tvShow.PosterSize}{_tvShow.PosterPath}"
        : string.Empty;

    public string BackdropImageLink => $"{_tvShow.PrefixBackDropLink}{_tvShow.BackDropSize}{_tvShow.BackdropPath}";
    public bool HasPosterImage => !string.IsNullOrEmpty(_tvShow.PosterPath);
    public bool HasBackdropImage => !string.IsNullOrEmpty(_tvShow.BackdropPath);

    [RelayCommand]
    public void OpenShowDetails()
    {
        _mainViewModel.TabbedContent.OpenShowDetails(_tvShow);
    }

    public TvShowViewModel(TvShow tvShow, MainViewModel mainViewModel)
    {
        _tvShow = tvShow;
        _mainViewModel = mainViewModel;
    }
}