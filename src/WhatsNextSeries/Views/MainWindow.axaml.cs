using Avalonia.Controls;

namespace WhatsNextSeries.Views;

public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
    }

    protected override void OnInitialized()
    {
        base.OnInitialized();
        
        var screen = Screens.ScreenFromWindow(this);
        if (screen is null)
        {
            return;
        }
        
        Width = screen.Bounds.Width / 2f;
        Height = screen.Bounds.Height / 2f;
        // Width = int.Parse(Screens.Primary?.Bounds.Width.ToString() ?? string.Empty) / 2f;
        // Height = int.Parse(Screens.Primary.Bounds.Height.ToString()) / 2f; 
    }
}