using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

/// <summary>
/// Dummy implementation of <see cref="IMovieDbService"/> for testing purposes and design time use.
/// </summary>
public class DummyMovieDbService : IMovieDbService
{
    /// <inheritdoc />
    public Task<IEnumerable<TvShow>> GetPopularShows(int page, CancellationToken cancellationToken = default)
    {
        return Task.FromResult<IEnumerable<TvShow>>(new List<TvShow>(
            new List<TvShow>
            {
                CreateShow("Grey's Anatomy"),
                CreateShow("The Walking Dead"),
                CreateShow("The Good Doctor"),
                CreateShow("The Simpsons"),
                CreateShow("The Flash"),
                CreateShow("The Big Bang Theory"),
                CreateShow("Family Guy"),
                CreateShow("The Blacklist"),
                CreateShow("The Rookie"),
                CreateShow("The Mandalorian"),
                CreateShow("The Good Doctor"),
                CreateShow("The Simpsons"),
                CreateShow("The Flash"),
            }));
    }

    /// <inheritdoc />
    public Task<IEnumerable<TvShow>> GetAiringTodayShows(int page, CancellationToken cancellationToken = default)
    {
        return Task.FromResult<IEnumerable<TvShow>>(new List<TvShow>(
            new List<TvShow>
            {
                CreateShow("Grey's Anatomy"),
                CreateShow("The Walking Dead"),
                CreateShow("The Good Doctor"),
                CreateShow("The Simpsons"),
                CreateShow("The Flash"),
                CreateShow("The Big Bang Theory"),
                CreateShow("Family Guy"),
                CreateShow("The Blacklist"),
                CreateShow("The Rookie"),
                CreateShow("The Mandalorian"),
                CreateShow("The Good Doctor"),
                CreateShow("The Simpsons"),
                CreateShow("The Flash"),
            }));
    }
    
    /// <inheritdoc />
    public Task<TvShowDetail> GetTvShowDetails(int id, CancellationToken cancellationToken = default)
    {
        var tvShow = CreateShow("Grey's Anatomy");
        return Task.FromResult(new TvShowDetail(tvShow));
    }

    /// <inheritdoc />
    public Task<IEnumerable<TvShow>> GetTvShowsByName(string name, int page = 1, CancellationToken cancellationToken = default)
    {
        return Task.FromResult<IEnumerable<TvShow>>(new List<TvShow>(
            new List<TvShow>
            {
                CreateShow("Grey's Anatomy"),
                CreateShow("The Walking Dead"),
                CreateShow("The Good Doctor"),
                CreateShow("The Simpsons"),
                CreateShow("The Flash"),
                CreateShow("The Big Bang Theory"),
                CreateShow("Family Guy"),
                CreateShow("The Blacklist"),
                CreateShow("The Rookie"),
                CreateShow("The Mandalorian"),
                CreateShow("The Good Doctor"),
                CreateShow("The Simpsons"),
                CreateShow("The Flash"),
            }));
    }

    private static TvShow CreateShow(string name) => new ()
    {
        FirstAirDate = "2021-09-22",
        PrefixPosterLink = "https://image.tmdb.org/t/p/",
        Id = 1416,
        Name = name,
        Overview ="Rabb Se Hai Dua é uma série de televisão dramática em língua hindi indiana que estreou em 28 de novembro de 2022 na Zee TV e está disponível digitalmente na ZEE5. Produzido por Prateek Sharma sob LSD Films Private Limited, é estrelado por Karanvir Sharma, Richa Rathore e Aditi Sharma. Dua e seu marido são um casal perfeito aos olhos de sua família. No entanto, a vida de Dua vira de cabeça para baixo quando seu marido pede sua permissão para se casar com outra mulher.",
        PosterSize = "w342",
        BackDropSize = "w1280",
        VoteAverage = 8.2,
        Popularity = 3142.436,
        VoteCount = 2749,
        PosterPath = "/pZaFdrYekwC9ITq4yWrkqEwCy3E.jpg",
        BackdropPath = "/1HOBv1QxSbTwn5VyZ2vAVRhdR8e.jpg",
        GenreIds = new List<int>(),
    };
    
}