using System.Net.Http.Headers;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json.Linq;

namespace WhatsNextSeries.Services;

public class BaseMovieService : IDisposable
{
    private const string BaseUrl = "https://api.themoviedb.org/3/";

    protected string ApiKey { get; private set; }
    
    protected static readonly HttpClient ClientHttp = new HttpClient(
        new SocketsHttpHandler
    {
        PooledConnectionLifetime = TimeSpan.FromMinutes(2)
    })
    {
        BaseAddress = new Uri(BaseUrl),
        DefaultRequestHeaders = { Accept = { new MediaTypeWithQualityHeaderValue("application/json") } }
    };

    protected BaseMovieService(IConfiguration configuration)
    {
        ApiKey = configuration["api_key"] ?? "API_KEY_NOT_SET";
    }
    
    public void Dispose()
    {
        ClientHttp?.Dispose();
    }
}