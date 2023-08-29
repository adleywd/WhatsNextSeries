using System;
using System.Reflection;
using CommunityToolkit.Mvvm.DependencyInjection;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using WhatsNextSeries.Services;
using WhatsNextSeries.ViewModels;

namespace WhatsNextSeries;

public static class Defaults
{
    public static readonly Ioc Locator = Ioc.Default;

    public static void SetUpLocator()
    {
        var serviceCollection = new ServiceCollection();
        var serviceProvider = serviceCollection.ConfigureDefaultServices().BuildServiceProvider();
        Locator.ConfigureServices(serviceProvider);
    }
    public static IServiceCollection ConfigureDefaultServices(this IServiceCollection serviceCollection)
    {
        serviceCollection.AddSingleton<IMovieDbService, TheMovieDbMovieService>();
        
        var a = Assembly.GetExecutingAssembly();
        using var stream = a.GetManifestResourceStream("WhatsNextSeries.TheMovieDbSettings.json");
        var config = new ConfigurationBuilder()
            .AddJsonStream(stream)
            .Build();
        serviceCollection.AddSingleton<IConfiguration>(config);
        
        serviceCollection.AddTransient<MainViewModel>();
        serviceCollection.AddTransient<TvShowDetailsWindowViewModel>();

        return serviceCollection;
    }
}