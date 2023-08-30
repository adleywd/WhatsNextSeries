using CommunityToolkit.Mvvm.Input;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.ViewModels;

public partial class TvShowViewModel : ViewModelBase
{
    private readonly TvShow _tvShow;
    
    public string Name => _tvShow.Name;
    public string PosterImageLink => $"{_tvShow.PrefixPosterLink}{_tvShow.PosterSize}{_tvShow.PosterPath}";
    public string BackdropImageLink => $"{_tvShow.PrefixBackDropLink}{_tvShow.BackDropSize}{_tvShow.BackdropPath}";
    public bool HasPosterImage => !string.IsNullOrEmpty(_tvShow.PosterPath);
    public bool HasBackdropImage => !string.IsNullOrEmpty(_tvShow.BackdropPath);
    
    [RelayCommand]
    public void OpenShowDetails()
    {
        var mainViewModel = Defaults.Locator.GetService<MainViewModel>();
        mainViewModel?.TabbedContent.OpenShowDetails(_tvShow);
    }
    
    public TvShowViewModel(TvShow tvShow)
    {
        _tvShow = tvShow;
    }
}