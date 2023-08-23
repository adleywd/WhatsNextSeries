using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using CommunityToolkit.Mvvm.ComponentModel;
using WhatsNextSeries.Models;
using WhatsNextSeries.Services;

namespace WhatsNextSeries.ViewModels;

public partial class MainViewModel : ViewModelBase
{
    private readonly ITheMovieDbService _theMovieDbService;
    
    [ObservableProperty]
    private List<TvShow> _popularShows = new();
    
    public MainViewModel(ITheMovieDbService theMovieDbService)
    {
        _theMovieDbService = theMovieDbService;
        Task.Run( async() => await LoadPopularShows().ConfigureAwait(true));
    }
    
    public async Task LoadPopularShows()
    {
        PopularShows = await _theMovieDbService.GetPopularShows().ConfigureAwait(true);
    }

    public MainViewModel()
    {
        if (Avalonia.Controls.Design.IsDesignMode == false)
        {
            throw new InvalidOperationException("This empty constructor should only be used in design mode.");
        }
        _theMovieDbService = new TheMovieDbService();
        Task.Run( async() => await LoadPopularShows().ConfigureAwait(true));
    }
    
    
}