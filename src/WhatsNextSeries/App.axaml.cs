using Avalonia;
using Avalonia.Controls.ApplicationLifetimes;
using Avalonia.Markup.Xaml;
using WhatsNextSeries.Services;
using WhatsNextSeries.ViewModels;
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
        var theMovieDbService = new TheMovieDbService();
        var mainWindowViewModel = new MainWindowViewModel(theMovieDbService);
        
        if (ApplicationLifetime is IClassicDesktopStyleApplicationLifetime desktop)
        {
            desktop.MainWindow = new MainWindow
            {
                DataContext = mainWindowViewModel
            };
        }
        else if (ApplicationLifetime is ISingleViewApplicationLifetime singleViewPlatform)
        {
            singleViewPlatform.MainView = new PopularView
            {
                DataContext = mainWindowViewModel
            };
        }

        base.OnFrameworkInitializationCompleted();
    }
}