using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel
{
    private readonly List<int> _tvShowIdsAlreadyInFavorites = new();
    public ObservableCollection<TvShowViewModel> FavoritesShows { get; } = new();

    public void AddShowToFavorites(TvShow tvShow)
    {
        var isFavoriteAlreadyExists = _tvShowIdsAlreadyInFavorites.Exists(id => id == tvShow.Id);
        if (isFavoriteAlreadyExists)
        {
            return;
        }

        FavoritesShows.Add(new TvShowViewModel(tvShow, MainViewModel));
        _tvShowIdsAlreadyInFavorites.Add(tvShow.Id);
    }
}