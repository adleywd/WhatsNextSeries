﻿using System;
using System.Reflection;
using CommunityToolkit.Mvvm.DependencyInjection;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using WhatsNextSeries.Helpers;
using WhatsNextSeries.Services;
using WhatsNextSeries.ViewModels;
using WhatsNextSeries.Views;

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

    private static IServiceCollection ConfigureDefaultServices(this IServiceCollection serviceCollection)
    {
        serviceCollection.AddSingleton<IMovieDbService, TheMovieDbMovieService>();
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