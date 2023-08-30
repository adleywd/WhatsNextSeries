using Avalonia;
using Avalonia.Controls.ApplicationLifetimes;
using Avalonia.Markup.Xaml;
using WhatsNextSeries.Services;
using WhatsNextSeries.ViewModels;
using MainView = WhatsNextSeries.Views.MainView;
using MainWindow = WhatsNextSeries.Views.MainWindow;

namespace WhatsNextSeries;

public partial class App : Application
{
    public override void Initialize()
    {
        AvaloniaXamlLoader.Load(this);
    }

    public override void OnFrameworkInitializationCompleted()
    {
        Defaults.SetUpLocator();

        var mainWindowViewModel = Defaults.Locator.GetService<MainViewModel>();
        
        
        if (ApplicationLifetime is IClassicDesktopStyleApplicationLifetime desktop)
        {
            desktop.MainWindow = new MainWindow
            {
                DataContext = mainWindowViewModel
            };
            
            // Register the root window
            var windowManager = Defaults.Locator.GetService<IWindowManager>();
            windowManager?.OpenedWindows.Add(nameof(MainWindow), desktop.MainWindow);
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