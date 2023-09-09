using CommunityToolkit.Mvvm.ComponentModel;
using WhatsNextSeries.DataServices;
using WhatsNextSeries.Helpers;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public partial class MainViewModel : ViewModelBase
{
    private readonly ITvShowFileManager _tvShowFileManager;

    [ObservableProperty]
    private ViewModelBase _contentViewModel = default!;
    
    public TabbedViewModel TabbedContent { get; }
    
    public MainViewModel(IMovieDbService movieDbService, IWindowManager windowManager, ITvShowFileManager tvShowFileManager)
    {
        _tvShowFileManager = tvShowFileManager;
        TabbedContent = new TabbedViewModel(this, movieDbService, windowManager, tvShowFileManager);
        ContentViewModel = TabbedContent;
    }

    public MainViewModel()
    {
        Helper.ThrowIfNotDesignMode();

        TabbedContent = new TabbedViewModel(this, new DummyMovieDbService(), new WindowManager(), new TvShowFileManager());
        ContentViewModel = TabbedContent;
    }
}