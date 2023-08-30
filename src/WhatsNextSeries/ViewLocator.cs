using System;
using Avalonia.Controls;
using Avalonia.Controls.Templates;
using WhatsNextSeries.ViewModels;
using WhatsNextSeries.Views;

namespace WhatsNextSeries;

public class ViewLocator : IDataTemplate
{
    public Control Build(object data)
    {
        if (data is null)
            return null;

        var name = data.GetType().FullName!.Replace("ViewModel", "View");
        var type = Type.GetType(name);

        if (type == null)
        {
            return new TextBlock { Text = $"{name} not found" };
        }

        var getType = Defaults.Locator.GetService(type);
        if (getType != null)
        {
            return (Control)getType;
        }
            
        return (Control)Activator.CreateInstance(type)!;

    }

    public bool Match(object? data)
    {
        return data is ViewModelBase;
    }
}