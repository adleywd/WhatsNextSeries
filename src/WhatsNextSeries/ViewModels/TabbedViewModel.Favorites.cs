using System.Collections.ObjectModel;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel
{
    public ObservableCollection<Favorites> FavoritesShows { get; } = new();
}