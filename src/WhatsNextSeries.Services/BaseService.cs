using System.Net.Http.Headers;
using Newtonsoft.Json.Linq;

namespace WhatsNextSeries.Services;

public class BaseService : IDisposable
{
    private const string BaseUrl = "https://api.themoviedb.org/3/";

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
    
    /// <summary>
    /// Deserialize the sneak case JSON strings into camel case properties.
    /// </summary>
    /// <param name="json"></param>
    /// <typeparam name="T"></typeparam>
    /// <returns></returns>
    protected static T DeserializeWithSneakCaseToCamelCase<T>(string json)
    {
        var jObject = JObject.Parse(json);
        TransformKeysToCamelCase(jObject);
        return jObject.ToObject<T>();
    }

    private static void TransformKeysToCamelCase(JToken token)
    {
        if (token.Type == JTokenType.Object)
        {
            JObject obj = (JObject)token;
            var properties = obj.Properties().ToList();

            foreach (var property in properties)
            {
                property.Replace(new JProperty(CamelCase(property.Name), property.Value));
                TransformKeysToCamelCase(property.Value);
            }
        }
        else if (token.Type == JTokenType.Array)
        {
            foreach (var item in token)
            {
                TransformKeysToCamelCase(item);
            }
        }
    }

    private static string CamelCase(string input)
    {
        if (string.IsNullOrEmpty(input) || char.IsLower(input[0]))
            return input;

        char[] chars = input.ToCharArray();
        chars[0] = char.ToLower(chars[0]);
        return new string(chars);
    }
}