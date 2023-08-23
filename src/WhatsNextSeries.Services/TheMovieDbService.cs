using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

public class TheMovieDbService : BaseService, ITheMovieDbService
{
    public async Task<List<TvShow>> GetPopularShows()
    {
        var apiKey = "6625ee037f38fd509670d65df6587d57";
        var apiUrl = "https://api.themoviedb.org/3/tv/popular";
        var language = "en-US";
        var page = 1;

        var requestUrl = $"{apiUrl}?api_key={apiKey}&language={language}&page={page}";

        try
        {
            var response = await ClientHttp.GetAsync(requestUrl).ConfigureAwait(false);

            if (response.IsSuccessStatusCode)
            {
                var responseBody = await response.Content.ReadAsStringAsync().ConfigureAwait(false);

                var tvShowListResponse = JsonConvert.DeserializeObject<TvShowListResponse>(responseBody);

                // Extract and work with the TVShowResult objects
                var tvShowResults = tvShowListResponse?.Results ?? new List<TvShow>();

                return tvShowResults;
            }

            Console.WriteLine($"Request failed with status code: {response.StatusCode}");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"An error occurred: {ex.Message}");
        }
        
        return new List<TvShow>();
    }
}