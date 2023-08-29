using System;
using CommunityToolkit.Mvvm.ComponentModel;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public partial class TabbedViewModel : ViewModelBase
{
    [ObservableProperty]
    private MainViewModel _mainViewModel;
    
    private readonly IMovieDbService _movieDbService;
    
    public TabbedViewModel(MainViewModel mainViewModel, IMovieDbService movieDbService)
    {
        _mainViewModel = mainViewModel;
        _movieDbService = movieDbService;
    }

    public TabbedViewModel()
    {
        if (Avalonia.Controls.Design.IsDesignMode == false)
        {
            throw new InvalidOperationException("This empty constructor should only be used in design mode.");
        }

        _movieDbService = new DummyMovieDbService();
        _mainViewModel = new MainViewModel(_movieDbService);
    }
}