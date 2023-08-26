using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

public class DummyMovieDbService : IMovieDbService
{
    public Task<IEnumerable<TvShow>> GetPopularShows(CancellationToken cancellationToken)
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

    public Task<IEnumerable<TvShow>> GetAiringTodayShows(CancellationToken cancellationToken)
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

    private static TvShow CreateShow(string name) => new TvShow
    {
        BackDropSize = "w1280",
        FirstAirDate = "2021-09-22",
        Id = 1416,
        Name = name,
        Overview =
            "Follows the personal and professional lives of a group of doctors at Seattle’s Grey Sloan Memorial Hospital.",
        PosterSize = "w342",
        VoteAverage = 8.2,
        Popularity = 3142.436,
        VoteCount = 2749,
        PosterPath = "",
        GenreIds = new List<int>(),
        BackdropPath = "",
    };
}