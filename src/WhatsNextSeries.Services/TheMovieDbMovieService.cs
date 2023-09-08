using System.Diagnostics;
using System.Globalization;
using System.Text;
using System.Text.RegularExpressions;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

/// <summary>
/// TheMovieDb service
/// </summary>
public class TheMovieDbMovieService : BaseMovieService, IMovieDbService
{
    private const string DefaultLanguage = "en-US";
    private const string TvPopularEndpoint = "tv/popular";
    private const string TvAiringTodayEndpoint = "tv/airing_today";
    private const string TvShowDetailEndpoint = "tv"; // this needs an Id tv/{tv_id}
    private const string SearchTvShowEndpoint = "search/tv";

    /// <summary>
    /// Generate a new instance of <see cref="TheMovieDbMovieService"/>
    /// </summary>
    /// <param name="configuration"></param>
    public TheMovieDbMovieService(IConfiguration configuration) : base(configuration)
    {
    }

    /// <inheritdoc />
    public async Task<IEnumerable<TvShow>> GetPopularShows(int page = 1, CancellationToken cancellationToken = default)
    {
        return await GetTvShowsAsync(TvPopularEndpoint, ApiKey, page, cancellationToken: cancellationToken)
            .ConfigureAwait(true);
    }

    /// <inheritdoc />
    public async Task<IEnumerable<TvShow>> GetAiringTodayShows(int page = 1,
        CancellationToken cancellationToken = default)
    {
        return await GetTvShowsAsync(TvAiringTodayEndpoint, ApiKey, page, cancellationToken: cancellationToken)
            .ConfigureAwait(true);
    }

    /// <inheritdoc />
    public async Task<TvShowDetail> GetTvShowDetails(int id, CancellationToken cancellationToken = default)
    {
        try
        {
            var endpoint = $"tv/{id}";
            var requestUrl = $"{endpoint}?api_key={ApiKey}&language={GetLanguage()}";
            var response = await ClientHttp.GetAsync(requestUrl, cancellationToken).ConfigureAwait(false);

            if (response.IsSuccessStatusCode)
            {
                var responseBody = await response.Content.ReadAsStringAsync(cancellationToken).ConfigureAwait(false);

                var tvShowListResponse = JsonConvert.DeserializeObject<TvShowDetail>(responseBody);

                if (tvShowListResponse != null)
                {
                    tvShowListResponse.NextEpisodeToAir ??= new AiringEpisode();
                    tvShowListResponse.LastEpisodeToAir ??= new AiringEpisode();
                }

                Debug.WriteLine("Request succeeded: {0}", responseBody);

                // Extract and work with the TVShowResult objects
                return tvShowListResponse ?? new TvShowDetail();
            }
        }
        catch (OperationCanceledException ex)
        {
            Debug.WriteLine("Operation timeout: {0}", ex.Message);
        }
        catch (HttpRequestException ex)
        {
            Debug.WriteLine("Request exception: {0}", ex.Message);
        }
        catch (Exception ex)
        {
            Debug.WriteLine("An error occurred: {0}", ex.Message);
        }

        return new TvShowDetail();
    }

    /// <inheritdoc />
    public async Task<IEnumerable<TvShow>> GetTvShowsByName(string name, int page = 1,
        CancellationToken cancellationToken = default)
    {
        var query = $"&query={name}";
        return await GetTvShowsAsync(SearchTvShowEndpoint, ApiKey, page, query, cancellationToken).ConfigureAwait(true);
    }

    /// <summary>
    /// Get the TVShow list results from the API
    /// </summary>
    /// <param name="endpoint"></param>
    /// <param name="apiKey"></param>
    /// <param name="page"></param>
    /// <param name="query"></param>
    /// <param name="cancellationToken"></param>
    /// <returns></returns>
    private async Task<List<TvShow>> GetTvShowsAsync(string endpoint, string apiKey, int page, string query = "",
        CancellationToken cancellationToken = default)
    {
        List<TvShow>? tvShowList = null;
        try
        {
            var requestUrl = $"{endpoint}?api_key={apiKey}&language={GetLanguage()}&page={page}{query}";
            var response = await ClientHttp.GetAsync(requestUrl, cancellationToken).ConfigureAwait(false);

            if (response.IsSuccessStatusCode)
            {
                var responseBody = await response.Content.ReadAsStringAsync(cancellationToken).ConfigureAwait(false);

                var tvShowListResponse = JsonConvert.DeserializeObject<TvShowListResponse>(responseBody);

                // Extract and work with the TVShowResult objects
                tvShowList = tvShowListResponse?.Results ?? new List<TvShow>();
                Debug.WriteLine("Request succeeded: {0}", responseBody);
            }
        }
        catch (OperationCanceledException ex)
        {
            Debug.WriteLine("Operation timeout: {0}", ex.Message);
        }
        catch (HttpRequestException ex)
        {
            Debug.WriteLine("Request exception: {0}", ex.Message);
        }
        catch (Exception ex)
        {
            Debug.WriteLine("An error occurred: {0}", ex.Message);
        }

        return tvShowList ?? new List<TvShow>();
    }

    /// <summary>
    /// Get the language to use for the request
    /// </summary>
    /// <returns></returns>
    private static string GetLanguage()
    {
        var language = CultureInfo.CurrentCulture.Name;
        switch (language)
        {
            case "pt-PT":
                language = "pt-BR";
                break;
            case "pt-BR":
                language = "pt-BR";
                break;
            case "en-GB":
                language = "en-GB";
                break;
            default:
                language = DefaultLanguage;
                ;
                break;
        }
        // const string languagePattern = @"[a-zA-Z]{2}-[a-zA-Z]{2}";
        // else if (!Regex.IsMatch(language, languagePattern, RegexOptions.IgnoreCase))
        // {
        //     language = DefaultLanguage;
        // }

        return language;
    }
}