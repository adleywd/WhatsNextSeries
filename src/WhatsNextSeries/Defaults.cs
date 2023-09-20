using System;
using System.Reflection;
using CommunityToolkit.Mvvm.DependencyInjection;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using WhatsNextSeries.Database;
using WhatsNextSeries.DataServices;
using WhatsNextSeries.Helpers;
using WhatsNextSeries.Services;
using WhatsNextSeries.ViewModels;
using WhatsNextSeries.Views;

namespace WhatsNextSeries;

public static class Defaults
{
    public static readonly Ioc Locator = Ioc.Default;

    public static void SetUpDILocator()
    {
        var serviceCollection = new ServiceCollection();
        var serviceProvider = serviceCollection.ConfigureDefaultServices().BuildServiceProvider();
        InitializeDatabase();
        Locator.ConfigureServices(serviceProvider);
    }

    private static void InitializeDatabase()
    {
        using var context = new WhatsNextDbContext();
        context.Database.Migrate();
    }
    
    private static IServiceCollection ConfigureDefaultServices(this IServiceCollection serviceCollection)
    {
        serviceCollection.AddSingleton<IMovieDbService, TheMovieDbMovieService>();
        serviceCollection.AddTransient<IFavoritesDataService, FavoritesSqliteDataService>();
        
        serviceCollection.AddSingleton<IWindowManager, WindowManager>();
        
        var a = Assembly.GetExecutingAssembly();
        using var stream = a.GetManifestResourceStream("WhatsNextSeries.TheMovieDbSettings.json");
        var config = new ConfigurationBuilder()
            .AddJsonStream(stream)
            .Build();
        serviceCollection.AddSingleton<IConfiguration>(config);
        
        serviceCollection.AddSingleton<MainViewModel>();
        serviceCollection.AddTransient<DetailsViewModel>();
        
        serviceCollection.AddSingleton<MainView>();
        serviceCollection.AddSingleton<TabbedView>();
        
        return serviceCollection;
    }
}