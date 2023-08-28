using Microsoft.Extensions.Configuration;
using Newtonsoft.Json;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

public class TheMovieDbMovieService : BaseMovieService, IMovieDbService
{
    public TheMovieDbMovieService(IConfiguration configuration) : base(configuration)
    {
        
    }
    
    public async Task<IEnumerable<TvShow>> GetPopularShows(int page = 1, CancellationToken cancellationToken = default)
    {
        const string tvPopularEndpoint = "tv/popular";
        var language = "en-US";

        var requestUrl = $"{tvPopularEndpoint}?api_key={ApiKey}&language={language}&page={page}";

        return await GetTvShowResultsAsync(requestUrl).ConfigureAwait(true);
    }
    public async Task<IEnumerable<TvShow>> GetAiringTodayShows(int page = 1, CancellationToken cancellationToken = default)
    {
        const string tvAiringTodayEndpoint = "tv/airing_today";
        var language = "en-US";

        var requestUrl = $"{tvAiringTodayEndpoint}?api_key={ApiKey}&language={language}&page={page}";

        return await GetTvShowResultsAsync(requestUrl).ConfigureAwait(true);
    }

    private async Task<List<TvShow>> GetTvShowResultsAsync(string requestUrl)
    {
        List<TvShow>? tvShowList = null;
        try
        {
            var response = await ClientHttp.GetAsync(requestUrl).ConfigureAwait(false);

            if (response.IsSuccessStatusCode)
            {
                var responseBody = await response.Content.ReadAsStringAsync().ConfigureAwait(false);

                var tvShowListResponse = JsonConvert.DeserializeObject<TvShowListResponse>(responseBody);

                // Extract and work with the TVShowResult objects
                tvShowList = tvShowListResponse?.Results ?? new List<TvShow>();
                Console.WriteLine($"Request succeeded with status code: {response.StatusCode}");
            }

        }
        catch (OperationCanceledException ex)
        {
            Console.WriteLine($"Operation timeout: {ex.Message}");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"An error occurred: {ex.Message}");
        }

        return tvShowList ?? new List<TvShow>();
    }
}