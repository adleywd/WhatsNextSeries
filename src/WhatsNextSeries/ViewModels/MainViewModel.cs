using CommunityToolkit.Mvvm.ComponentModel;
using WhatsNextSeries.Helpers;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public partial class MainViewModel : ViewModelBase
{
    [ObservableProperty]
    private ViewModelBase _contentViewModel = default!;
    
    public TabbedViewModel TabbedContent { get; }
    
    public MainViewModel(IMovieDbService movieDbService, IWindowManager windowManager)
    {
        TabbedContent = new TabbedViewModel(this, movieDbService, windowManager);
        ContentViewModel = TabbedContent;
    }

    public MainViewModel()
    {
        Helper.ThrowIfNotDesignMode();

        TabbedContent = new TabbedViewModel(this, new DummyMovieDbService(), new WindowManager());
        ContentViewModel = TabbedContent;
    }
}