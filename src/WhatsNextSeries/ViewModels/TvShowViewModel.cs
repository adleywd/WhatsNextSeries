using WhatsNextSeries.Models;

namespace WhatsNextSeries.ViewModels;

public class TvShowViewModel : ViewModelBase
{
    private readonly TvShow _tvShow;
    
    public string Name => _tvShow.Name;
    public string PosterImageLink => $"{_tvShow.PrefixPosterLink}{_tvShow.PosterPath}";
    public string BackdropImageLink => $"{_tvShow.PrefixBackDropLink}{_tvShow.BackdropPath}";
    public bool HasPosterImage => !string.IsNullOrEmpty(_tvShow.PosterPath);
    public bool HasBackdropImage => !string.IsNullOrEmpty(_tvShow.BackdropPath);
    
    public TvShowViewModel(TvShow tvShow)
    {
        _tvShow = tvShow;
    }
}