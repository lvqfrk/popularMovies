package comlvqfrk.httpsgithub.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.data.DetailedMovie;
import comlvqfrk.httpsgithub.popularmovies.data.Movie;
import comlvqfrk.httpsgithub.popularmovies.data.Review;

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
    /** keyword for extract author name from json reviews */
    private static final String KEY_REVIEW_AUTHOR = "author";
    /** keyword for extract content from json reviews */
    private static final String KEY_REVIEW_CONTENT = "content";


    /**
     * this method extract the needed data from Json returned in the http reponse.
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

    /**
     * this method extract details for a movie from a json.
     * @param jsonResponseStr
     * @return a DetailedMovie
     * @throws JSONException
     */
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

    /**
     * this method return a List of review for a movie from a Json response.
     * @param jsonResponseStr
     * @return a list of Reviews or null if no reviews available
     * @throws JSONException
     */
    public static List<Review> extractReviewFromJsonResponse(String jsonResponseStr) throws JSONException {
        List<Review> reviews = new ArrayList<>();
        JSONObject jsonResponse = new JSONObject(jsonResponseStr);
        JSONArray results = jsonResponse.getJSONArray(KEY_RESULTS);
        if (results.length() == 0) return reviews;
        for (int i = 0; i < results.length(); i++) {
            JSONObject currentReview = results.getJSONObject(i);
            String author = currentReview.getString(KEY_REVIEW_AUTHOR);
            String content = currentReview.getString(KEY_REVIEW_CONTENT);
            Review newReview = new Review(author, content);
            reviews.add(newReview);
        }

        if (!(reviews.isEmpty())){
            return reviews;
        } else {
            return null;
        }
    }
}