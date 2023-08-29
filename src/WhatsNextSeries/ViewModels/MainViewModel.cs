using System;
using CommunityToolkit.Mvvm.ComponentModel;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public partial class MainViewModel : ViewModelBase
{
    [ObservableProperty]
    private ViewModelBase _contentViewModel;
    
    public TabbedViewModel TabbedContent { get; }
    
    public MainViewModel(IMovieDbService movieDbService)
    {
        TabbedContent = new TabbedViewModel(this, movieDbService);
        ContentViewModel = TabbedContent;
    }
}