package br.com.adley.whatsnextseries.models.remote;

/**
 * Created by Adley on 05/11/2017.
 * Defines the BASE Url.
 */

public class ApiUtils {
    public static TheMovieDBService getTMDBService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(TheMovieDBService.class);
    }
}
