using CommunityToolkit.Mvvm.ComponentModel;
using WhatsNextSeries.DataServices;
using WhatsNextSeries.Helpers;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public partial class MainViewModel : ViewModelBase
{
    private readonly IFavoritesDataService _favoritesDataService;

    [ObservableProperty]
    private ViewModelBase _contentViewModel = default!;
    
    public TabbedViewModel TabbedContent { get; }
    
    public MainViewModel(IMovieDbService movieDbService, IWindowManager windowManager, IFavoritesDataService favoritesDataService)
    {
        _favoritesDataService = favoritesDataService;
        TabbedContent = new TabbedViewModel(this, movieDbService, windowManager, favoritesDataService);
        ContentViewModel = TabbedContent;
    }

    public MainViewModel()
    {
        Helper.ThrowIfNotDesignMode();

        TabbedContent = new TabbedViewModel(this, new DummyMovieDbService(), new WindowManager(), new FavoritesFileManagerDataService());
        ContentViewModel = TabbedContent;
    }
}