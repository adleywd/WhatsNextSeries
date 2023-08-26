using Newtonsoft.Json;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

public class TheMovieDbService : BaseService, IMovieDbService
{
    public async Task<IEnumerable<TvShow>> GetPopularShows(CancellationToken cancellationToken)
    {
        const string tvPopularEndpoint = "tv/popular";
        var language = "en-US";
        var page = 1;

        var requestUrl = $"{tvPopularEndpoint}?api_key={ApiKey}&language={language}&page={page}";

        return await GetTvShowResultsAsync(requestUrl).ConfigureAwait(true);
    }
    public async Task<IEnumerable<TvShow>> GetAiringTodayShows(CancellationToken cancellationToken)
    {
        const string tvAiringTodayEndpoint = "tv/airing_today";
        var language = "en-US";
        var page = 1;

        var requestUrl = $"{tvAiringTodayEndpoint}?api_key={ApiKey}&language={language}&page={page}";

        return await GetTvShowResultsAsync(requestUrl).ConfigureAwait(true);
    }

    private async Task<List<TvShow>> GetTvShowResultsAsync(string requestUrl)
    {
        var tvShowList = new List<TvShow>();
        try
        {
            var response = await ClientHttp.GetAsync(requestUrl).ConfigureAwait(false);

            if (response.IsSuccessStatusCode)
            {
                var responseBody = await response.Content.ReadAsStringAsync().ConfigureAwait(false);

                var tvShowListResponse = JsonConvert.DeserializeObject<TvShowListResponse>(responseBody);

                // Extract and work with the TVShowResult objects
                tvShowList = tvShowListResponse?.Results;
            }

            Console.WriteLine($"Request failed with status code: {response.StatusCode}");
        }
        catch (OperationCanceledException ex)
        {
            Console.WriteLine($"Operation timeout: {ex.Message}");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"An error occurred: {ex.Message}");
        }

        return tvShowList;
    }
}