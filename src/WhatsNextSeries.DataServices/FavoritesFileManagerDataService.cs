using System.Diagnostics;
using Newtonsoft.Json;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.DataServices;

/// <inheritdoc />
public class FavoritesFileManagerDataService : IFavoritesDataService
{
    private const string FavoritesFileName = "favorites.json";
    private readonly string _directoryPath = Path.Combine(Environment.CurrentDirectory, "Data");
    private readonly string _path = Path.Combine(Environment.CurrentDirectory, FavoritesFileName);

    public FavoritesFileManagerDataService()
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
    public async Task<bool> SaveFavoriteTvShow(List<TvShowDetail> tvShowDetailList, CancellationToken cancellationToken = default)
    {
        try
        {
            var favoritesTvShows = await LoadFavoritesTvShow(cancellationToken).ConfigureAwait(false);
            favoritesTvShows.AddRange(tvShowDetailList);

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

    /// <inheritdoc />
    public Task<bool> RemoveFavoritesTvShow(IList<int> tvShowId, CancellationToken cancellationToken = default)
    {
        throw new NotImplementedException("RemoveFavoritesTvShow is not implemented for file manager data service");
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