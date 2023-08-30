using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;

namespace WhatsNextSeries.Views;

public partial class DetailsWindow : Window
{
    public DetailsWindow()
    {
        InitializeComponent();
    }
    
    protected override void OnInitialized()
    {
        base.OnInitialized();

        var visualParent = Avalonia.VisualTree.VisualExtensions.GetVisualParent<Window>(this);

        if (visualParent is null)
        {
            return;
        }
        var width = visualParent.ClientSize.Width / 2f;
        var height = visualParent.ClientSize.Height / 2f;

        Width = width;
        Height = height;
    }
}