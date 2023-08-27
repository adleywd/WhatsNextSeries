using System.IO;
using System.Reflection;
using Avalonia;
using Avalonia.Controls.ApplicationLifetimes;
using Avalonia.Markup.Xaml;
using Microsoft.Extensions.Configuration;
using WhatsNextSeries.Services;
using WhatsNextSeries.ViewModels;
using WhatsNextSeries.Views.UserControlViews;
using MainWindow = WhatsNextSeries.Views.Windows.MainWindow;
using PopularView = WhatsNextSeries.Views.UserControlViews.PopularView;

namespace WhatsNextSeries;

public partial class App : Application
{
    public override void Initialize()
    {
        AvaloniaXamlLoader.Load(this);
    }

    public override void OnFrameworkInitializationCompleted()
    {
        var a = Assembly.GetExecutingAssembly();
        
        using var stream = a.GetManifestResourceStream("WhatsNextSeries.TheMovieDbSettings.json");
        
        var config = new ConfigurationBuilder()
            .AddJsonStream(stream)
            .Build();

        var theMovieDbService = new TheMovieDbMovieService(config);
        var mainWindowViewModel = new MainViewViewModel(theMovieDbService);
        
        if (ApplicationLifetime is IClassicDesktopStyleApplicationLifetime desktop)
        {
            desktop.MainWindow = new MainWindow
            {
                DataContext = mainWindowViewModel
            };
        }
        else if (ApplicationLifetime is ISingleViewApplicationLifetime singleViewPlatform)
        {
            singleViewPlatform.MainView = new MainView()
            {
                DataContext = mainWindowViewModel
            };
        }

        base.OnFrameworkInitializationCompleted();
    }
}