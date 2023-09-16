using System.Diagnostics;
using Newtonsoft.Json;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.DataServices;

/// <inheritdoc />
public class TvShowFileManager : ITvShowFileManager
{
    private const string FavoritesFileName = "favorites.json";
    private readonly string _directoryPath = Path.Combine(Environment.CurrentDirectory, "Data");
    private readonly string _path = Path.Combine(Environment.CurrentDirectory, FavoritesFileName);

    public TvShowFileManager()
    {
        if(OperatingSystem.IsAndroid() || OperatingSystem.IsIOS())
        {
            _directoryPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "Data");
        }
        if (CreatePathIfNotExists(_directoryPath))
        {
            _path = Path.Combine(_directoryPath, FavoritesFileName);
        }
    }
    
    /// <inheritdoc />
    public async Task<bool> SaveFavoriteTvShow(TvShowDetail tvShowDetail, CancellationToken cancellationToken = default)
    {
        try
        {
            var favoritesTvShows = await LoadFavoritesTvShow(cancellationToken).ConfigureAwait(false);

            favoritesTvShows.Add(tvShowDetail);

            var serializedData = JsonConvert.SerializeObject(favoritesTvShows);

            var writer = new StreamWriter(_path, false);
            await using var disposableStreamWriter = writer.ConfigureAwait(false);
            await writer.WriteAsync(serializedData).ConfigureAwait(false);
            await disposableStreamWriter.DisposeAsync();
            return true;
        }
        catch (Exception e)
        {
            Debug.Write(e.Message);
            return false;
        }
    }

    /// <inheritdoc />
    public async Task<List<TvShowDetail>> LoadFavoritesTvShow(CancellationToken cancellationToken = default)
    {
        if (!File.Exists(_path))
        {
            return new List<TvShowDetail>();
        }

        using var reader = new StreamReader(_path);
        var jsonData = await reader.ReadToEndAsync(cancellationToken).ConfigureAwait(false);
        return JsonConvert.DeserializeObject<List<TvShowDetail>>(jsonData) ?? new List<TvShowDetail>();
    }

    private static bool CreatePathIfNotExists(string path)
    {
        try
        {
            Directory.CreateDirectory(path);
            return true;
        }
        catch (UnauthorizedAccessException e)
        {
            Debug.Write(e.Message);
        }
        catch (DirectoryNotFoundException e)
        {
            Debug.Write(e.Message);
        }
        catch (PathTooLongException e)
        {
            Debug.Write(e.Message);
        }
        catch (ArgumentException e)
        {
            Debug.Write(e.Message);
        }
        catch (NotSupportedException e)
        {
            Debug.Write(e.Message);
        }
        catch (IOException e)
        {
            Debug.Write(e.Message);
        }
        catch (Exception e)
        {
            Debug.Write(e.Message);
        }

        return false;
    }
}