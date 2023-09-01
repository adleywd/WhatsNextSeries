﻿using WhatsNextSeries.Models;

namespace WhatsNextSeries.Services;

public class DummyMovieDbService : IMovieDbService
{
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
                CreateShow("The Good Doctorch"),
                CreateShow("The Simpsons"),
                CreateShow("The Flash"),
            }));
    }

    private static TvShow CreateShow(string name) => new TvShow
    {
        FirstAirDate = "2021-09-22",
        PrefixPosterLink = "https://image.tmdb.org/t/p/",
        Id = 1416,
        Name = name,
        Overview =
            "Follows the personal and professional lives of a group of doctors at Seattle’s Grey Sloan Memorial Hospital.",
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