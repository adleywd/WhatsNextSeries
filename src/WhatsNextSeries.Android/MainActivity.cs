using Android.App;
using Android.Content.PM;
using Android.OS;
using Android.Views;
using AndroidX.AppCompat.View;
using Avalonia;
using Avalonia.Android;
using Avalonia.ReactiveUI;

namespace WhatsNextSeries.Android;

[Activity(
    Label = "WhatsNextSeries",
    Theme = "@style/MyTheme.NoActionBar",
    Icon = "@drawable/icon",
    MainLauncher = true,
    ConfigurationChanges = ConfigChanges.Orientation | ConfigChanges.ScreenSize | ConfigChanges.UiMode)]
public class MainActivity : AvaloniaMainActivity<App>
{
    protected override AppBuilder CustomizeAppBuilder(AppBuilder builder)
    {
        return base.CustomizeAppBuilder(builder)
            .UseReactiveUI()
            .WithInterFont();
    }

    public override void OnWindowFocusChanged(bool bHasFocus)
    {
        base.OnWindowFocusChanged(bHasFocus);
        
        // if (bHasFocus)
        //     SetWindowLayout();
    }

    private void SetWindowLayout()
    {
        if (Window == null) return;
        if (Build.VERSION.SdkInt >= BuildVersionCodes.R)
        {
            IWindowInsetsController wicController = Window.InsetsController;


            Window.SetDecorFitsSystemWindows(false);
            Window.SetFlags(WindowManagerFlags.Fullscreen, WindowManagerFlags.Fullscreen);

            if (wicController != null)
            {
                wicController.Hide(WindowInsets.Type.Ime());
                wicController.Hide(WindowInsets.Type.NavigationBars());
            }
        }
        else
        {
#pragma warning disable CS0618

            Window.SetFlags(WindowManagerFlags.Fullscreen, WindowManagerFlags.Fullscreen);

            Window.DecorView.SystemUiVisibility = (StatusBarVisibility)(SystemUiFlags.Fullscreen |
                                                                        SystemUiFlags.HideNavigation |
                                                                        SystemUiFlags.Immersive |
                                                                        SystemUiFlags.ImmersiveSticky |
                                                                        SystemUiFlags.LayoutHideNavigation |
                                                                        SystemUiFlags.LayoutStable |
                                                                        SystemUiFlags.LowProfile);
#pragma warning restore CS0618
        }
    }
}