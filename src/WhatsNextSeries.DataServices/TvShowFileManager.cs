using Newtonsoft.Json;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.DataServices;

/// <inheritdoc />
public class TvShowFileManager : ITvShowFileManager
{
    private readonly string _path = Path.Combine(Environment.CurrentDirectory, "Data", "favorites.json");

    /// <inheritdoc />
    public async Task SaveFavoriteTvShow(TvShowDetail tvShowDetail, CancellationToken cancellationToken = default)
    {
        var favoritesTvShows = await LoadFavoritesTvShow(cancellationToken).ConfigureAwait(false);

        favoritesTvShows.Add(tvShowDetail);

        var serializedData = JsonConvert.SerializeObject(favoritesTvShows);

        var writer = new StreamWriter(_path, false);
        await using var disposableStreamWriter = writer.ConfigureAwait(false);
        await writer.WriteAsync(serializedData).ConfigureAwait(false);
        await disposableStreamWriter.DisposeAsync();
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
}