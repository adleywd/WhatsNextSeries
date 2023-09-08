using System;
using Avalonia;
using Avalonia.Controls;

namespace WhatsNextSeries.Views;

public partial class MainView : UserControl
{
    public MainView()
    {
        InitializeComponent();
    }

    protected override void OnInitialized()
    {
        base.OnInitialized();
        if(OperatingSystem.IsAndroid() || OperatingSystem.IsIOS())
        {
            Margin = new Thickness(0, 20, 0, 5);   
        }
    }
}