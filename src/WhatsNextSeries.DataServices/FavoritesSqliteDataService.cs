using System.Diagnostics;
using System.Globalization;
using Microsoft.EntityFrameworkCore;
using WhatsNextSeries.Database;
using WhatsNextSeries.Database.Entities;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.DataServices;

public class FavoritesSqliteDataService : IFavoritesDataService
{
    public async Task<bool> SaveFavoriteTvShow(TvShowDetail tvShowDetail, CancellationToken cancellationToken = default)
    {
        try
        {
            if (!DateTime.TryParse(tvShowDetail.NextEpisodeToAir.AirDate, out var nextEpisodeToAirDate))
            {
                nextEpisodeToAirDate = DateTime.MinValue;
            }
            using (var context = new WhatsNextDbContext())
            {
                await context.Favorites.AddAsync(new Favorite
                {
                    TvShowId = tvShowDetail.Id,
                    Name = tvShowDetail.Name,
                    OriginalName = tvShowDetail.OriginalName,
                    InProduction = tvShowDetail.InProduction,
                    NextEpisodeToAir = nextEpisodeToAirDate,
                    NumberOfEpisodes = tvShowDetail.NumberOfEpisodes,
                    NumberOfSeasons = tvShowDetail.NumberOfSeasons,
                    Status = tvShowDetail.Status,
                    Type = tvShowDetail.Type,
                    PosterPath = tvShowDetail.PosterPath,
                    BackdropPath = tvShowDetail.BackdropPath,
                    Popularity = tvShowDetail.Popularity
                }, cancellationToken).ConfigureAwait(false);

                await context.SaveChangesAsync(cancellationToken).ConfigureAwait(false);
            }

            return true;
        }
        catch (Exception e)
        {
            Debug.Write(e.Message);
            return false;
        }
    }

    public async Task<List<TvShowDetail>> LoadFavoritesTvShow(CancellationToken cancellationToken = default)
    {
        try
        {
            using (var context = new WhatsNextDbContext())
            {
                var favorites = await context.Favorites.ToListAsync(cancellationToken).ConfigureAwait(false);
                var tvShowDetails = favorites.Select(favorite => new TvShowDetail
                {
                    Id = favorite.TvShowId,
                    Name = favorite.Name,
                    OriginalName = favorite.OriginalName,
                    InProduction = favorite.InProduction,
                    NextEpisodeToAir = new AiringEpisode
                    {
                        AirDate = favorite.NextEpisodeToAir.ToString(CultureInfo.InvariantCulture)
                    },
                    NumberOfEpisodes = favorite.NumberOfEpisodes,
                    NumberOfSeasons = favorite.NumberOfSeasons,
                    Status = favorite.Status,
                    Type = favorite.Type,
                    PosterPath = favorite.PosterPath,
                    BackdropPath = favorite.BackdropPath,
                    Popularity = favorite.Popularity
                }).ToList();

                return tvShowDetails;
            }
        }
        catch (Exception e)
        {
            Debug.Write(e.Message);
            return new List<TvShowDetail>();
        }
    }
}