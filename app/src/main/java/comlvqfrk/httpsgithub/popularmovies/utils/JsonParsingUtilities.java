package comlvqfrk.httpsgithub.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.data.DetailedMovie;
import comlvqfrk.httpsgithub.popularmovies.data.Movie;

public class JsonParsingUtilities {

    /** name of the array where movies data are located in Json response */
    private static final String KEY_RESULTS = "results";

    private static final String KEY_ID = "id";
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


    /**
     * this method will extract the needed data from Json returned in the http reponse.
     * @param jsonResponseStr returned by NetworkingUtilities. getJsonReponseFromHttpUrl.
     * @return a List of Movie.
     * @throws JSONException
     */
    public static List<Movie> extractMoviesFromJson(String jsonResponseStr) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        JSONObject jsonResponse = new JSONObject(jsonResponseStr);
        JSONArray results = jsonResponse.getJSONArray(KEY_RESULTS);

        for (int i = 0; i < results.length(); i++) {
            JSONObject currentMovie = results.getJSONObject(i);
            int imdbId = currentMovie.getInt(KEY_ID);
            String title = currentMovie.getString(KEY_TITLE);
            String posterPath = currentMovie.getString(KEY_POSTER_PATH);
            Movie newMovie = new Movie(imdbId,
                    title,
                    posterPath);

            movies.add(newMovie);
        }

        if (!(movies.isEmpty())) {
            return movies;
        } else {
            return null;
        }
    }

    // TODO: use final vars for parsing this Json
    public static DetailedMovie extractDetailsFromJsonResponse(String jsonResponseStr) throws JSONException{
        JSONObject jsonResponse = new JSONObject(jsonResponseStr);
        int id = jsonResponse.getInt("id");
        String title = jsonResponse.getString("title");
        String posterPath = jsonResponse.getString("poster_path");
        double voteAverage = jsonResponse.getDouble("vote_average");
        String overview = jsonResponse.getString("overview");
        String releaseDate = jsonResponse.getString("release_date");
        String backdrop = jsonResponse.getString("backdrop_path");

        JSONObject videos = jsonResponse.getJSONObject("videos");
        JSONArray videosResults = videos.getJSONArray("results");
        String trailerPath;
        if (videosResults.length() > 1){
            trailerPath = videosResults.getJSONObject(0).getString("key");
        } else {
            trailerPath = "no_result";
        }

        return new DetailedMovie(id, title, posterPath, voteAverage,
                overview, releaseDate, trailerPath, backdrop);
    }
}