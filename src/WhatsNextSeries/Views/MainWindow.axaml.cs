using Avalonia.Controls;

namespace WhatsNextSeries.Views;

public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
    }

    // protected override void OnInitialized()
    // {
    //     base.OnInitialized();
    //     
    //     var screen = Screens.ScreenFromWindow(this);
    //     if (screen is null)
    //     {
    //         return;
    //     }
    //     
    //     Width = screen.Bounds.Width / 2f;
    //     Height = screen.Bounds.Height / 2f;
    // }
}