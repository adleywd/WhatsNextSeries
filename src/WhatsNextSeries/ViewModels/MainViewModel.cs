using System;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public partial class MainViewModel : ViewModelBase
{
    private readonly IMovieDbService _movieDbService;

    public MainViewModel(IMovieDbService movieDbService)
    {
        _movieDbService = movieDbService;
    }
    public MainViewModel()
    {
        if (Avalonia.Controls.Design.IsDesignMode == false)
        {
            throw new InvalidOperationException("This empty constructor should only be used in design mode.");
        }

        _movieDbService = new DummyMovieDbService();
    }
}