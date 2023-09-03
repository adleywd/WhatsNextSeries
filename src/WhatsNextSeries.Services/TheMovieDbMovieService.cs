using System.Diagnostics;
using System.Globalization;
using System.Text.RegularExpressions;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

public class TheMovieDbMovieService : BaseMovieService, IMovieDbService
{
    private const string DefaultLanguage = "en-US";
    private const string TvPopularEndpoint = "tv/popular";
    private const string TvAiringTodayEndpoint = "tv/airing_today";
    
    public TheMovieDbMovieService(IConfiguration configuration) : base(configuration)
    {
        
    }
    
    public async Task<IEnumerable<TvShow>> GetPopularShows(int page = 1, CancellationToken cancellationToken = default)
    {
        return await GetTvShowResultsAsync(TvPopularEndpoint, ApiKey, page).ConfigureAwait(true);
    }
    public async Task<IEnumerable<TvShow>> GetAiringTodayShows(int page = 1, CancellationToken cancellationToken = default)
    {
        return await GetTvShowResultsAsync(TvAiringTodayEndpoint, ApiKey, page).ConfigureAwait(true);
    }

    private async Task<List<TvShow>> GetTvShowResultsAsync(string endpoint, string apiKey, int page)
    {
        List<TvShow>? tvShowList = null;
        try
        {
            var requestUrl = $"{endpoint}?api_key={apiKey}&language={GetLanguage()}&page={page}";
            var response = await ClientHttp.GetAsync(requestUrl).ConfigureAwait(false);

            if (response.IsSuccessStatusCode)
            {
                var responseBody = await response.Content.ReadAsStringAsync().ConfigureAwait(false);

                var tvShowListResponse = JsonConvert.DeserializeObject<TvShowListResponse>(responseBody);

                // Extract and work with the TVShowResult objects
                tvShowList = tvShowListResponse?.Results ?? new List<TvShow>();
                Debug.WriteLine("Request succeeded: {0}", responseBody);
            }

        }
        catch (OperationCanceledException ex)
        {
            Debug.WriteLine("Operation timeout: {0}",ex.Message);
        }
        catch (HttpRequestException ex)
        {
            Debug.WriteLine("Request exception: {0}",ex.Message);
        }
        catch (Exception ex)
        {
            Debug.WriteLine("An error occurred: {0}",ex.Message);
        }

        return tvShowList ?? new List<TvShow>();
    }
    
    private string GetLanguage()
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
                language = DefaultLanguage;;
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