package br.com.adley.whatsnextseries.models.remote;

import br.com.adley.whatsnextseries.models.retrofit.SeasonEpisodes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Adley on 05/11/2017.
 * Api methods
 */

public interface TheMovieDBService {
    @GET("tv/{show_id}/season/{season_number}")
    Call<SeasonEpisodes> getEpisodesSeason(
            @Path("show_id") String showID,
            @Path("season_number") String seasonNumber,
            @Query("api_key") String apiKey,
            @Query("language") String language );
}
