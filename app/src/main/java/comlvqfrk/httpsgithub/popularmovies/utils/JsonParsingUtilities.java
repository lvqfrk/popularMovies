package comlvqfrk.httpsgithub.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.data.Movie;

public class JsonParsingUtilities {

    /** name of the array where movies data are located in Json response */
    private static final String KEY_RESULTS = "results";
    /** keyword for extract title from JSon */
    private static final String KEY_TITLE = "title";
    /** keyword for extract poster path from JSon */
    private static final String KEY_POSTER_PATH = "poster_path";
    /** keyword for extract vote average from JSon */
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    /** keyword for extract overview from JSon*/
    private static final String KEY_OVERVIEW = "overview";
    /** keyword for extract release date from JSon */
    private static final String KEY_RELEASE_DATE = "release_date";


    public static List<Movie> extractDataFromJsonResponse(String jsonResponseStr) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        JSONObject jsonResponse = new JSONObject(jsonResponseStr);
        JSONArray results = jsonResponse.getJSONArray(KEY_RESULTS);

        for (int i = 0; i < results.length(); i++) {
            JSONObject currentMovie = results.getJSONObject(i);
            String title = currentMovie.getString(KEY_TITLE);
            String posterPath = currentMovie.getString(KEY_POSTER_PATH);
            double voteAverage = currentMovie.getDouble(KEY_VOTE_AVERAGE);
            String overview = currentMovie.getString(KEY_OVERVIEW);
            String releaseDate = currentMovie.getString(KEY_RELEASE_DATE);
            Movie newMovie = new Movie(title, posterPath, voteAverage, overview, releaseDate);
            movies.add(newMovie);
        }

        if (!(movies.isEmpty())) {
            return movies;
        } else {
            return null;
        }
    }
}
