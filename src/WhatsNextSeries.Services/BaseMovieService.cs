using System.Net.Http.Headers;
using Newtonsoft.Json.Linq;

namespace WhatsNextSeries.Services;

public class BaseMovieService : IDisposable
{
    private const string BaseUrl = "https://api.themoviedb.org/3/";

    protected const string ApiKey = TheMovieDbApiKey.ApiKey;
    
    protected static readonly HttpClient ClientHttp = new HttpClient(
        new SocketsHttpHandler
    {
        PooledConnectionLifetime = TimeSpan.FromMinutes(2)
    })
    {
        BaseAddress = new Uri(BaseUrl),
        DefaultRequestHeaders = { Accept = { new MediaTypeWithQualityHeaderValue("application/json") } }
    };

    public void Dispose()
    {
        ClientHttp?.Dispose();
    }
}