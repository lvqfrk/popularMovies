package comlvqfrk.httpsgithub.popularmovies.utils;

import android.net.Uri;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;

public class NetworkingUtilities {

    private static final String TAG = NetworkingUtilities.class.getSimpleName();

    /**code 100 used to query most popular */
    public static final int QUERY_CODE_MOST_POPULAR = 100;
    /** code 102 used to query highest rated */
    public static final int QUERY_CODE_HIGHEST_RATED = 101;
    /** code 102 used to search by title query */
    public static final int QUERY_CODE_SEARCH_BY_TITLE = 102;
    /** code 150 used to query for details */
    public static final int QUERY_CODE_GET_DETAILS = 150;

    /** Base Url to show movie bases on keyword from users */
    private static final String TMDB_FIND_BY_TITLE_BASE_URL = "https://api.themoviedb.org/3/search/movie";
    /** Base Url to find movie by ID */
    private static final String TMDB_FIND_BY_ID_BASE_URL = "https://api.themoviedb.org/3/movie/";
    /** Base URL for discover features of the API (TMDB for The Movie DataBase) */
    private static final String TMDB_DISCOVER_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    /** param keyword for api key */
    private static final String API_KEY_PARAM = "api_key";
    /** param keyword for user's query*/
    private static final String QUERY_PARAM = "query";
    /** param keyword for sorting */
    private static final String SORT_BY_PARAM = "sort_by";
    /** param keyword for displaying adult content */
    private static final String INCLUDE_ADULT_PARAM = "include_adult";
    /** param value for most popular */
    private static final String MOST_POPULAR_VALUE = "popularity.desc";
    /** param value for best rates */
    private static final String VOTE_AVERAGE = "vote_average.desc";
    /** param keyword for minimum votes */
    private static final String MIN_VOTE_COUNT_PARAM = "vote_count.gte";
    /** param value to set a minimum of vote for query best rates */
    private static final String MIN_VOTE_COUNT_VALUE = "5000";
    /** param for add videos link to details response */
    private static final String APPEND_TO_RESPONSE = "append_to_response";
    /** param value for add videos to the response */
    private static final String VIDEOS = "videos";
    /** keyword to get reviews based on a movie id*/
    private static final String REVIEWS = "/reviews";
    /** param keyword for select a page of reponse content, NOT USED FOR NOW*/
    private static final String PAGE = "page";
    /** param to set the language of the request*/
    private static final String PARAM_LANGUAGE = "language";
    /** value for the language of the request, default is english*/
    private static String VALUE_LANGUAGE = "en-US";

    /**
     * this method is used to set the language of request's response.
     */
    private static void setLanguage() {
        String sysLanguage = Locale.getDefault().getDisplayLanguage();
        if (sysLanguage.equals("français")) VALUE_LANGUAGE = "fr-FR";
    }

    /**
     * build an URL for query movies based on user's keyword.
     * @param userQuery the keyword from the user
     * @return URL to use to query the movie db
     */
    private static URL buildUrlForUserSearch(String userQuery) {
        Uri movieQueryUri = Uri.parse(TMDB_FIND_BY_TITLE_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, ApiKey.THE_MOVIE_DATABASE_APIKEY_V3)
                .appendQueryParameter(QUERY_PARAM, userQuery)
                .appendQueryParameter(INCLUDE_ADULT_PARAM, "false")
                .build();

        try {
            return new URL(movieQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * build an URL for query most popular movies on TMDb.
     * @return URL to use to query the movies db.
     */
    private static URL buildUrlForMostPopular() {
        setLanguage();
        Uri movieQueryUri = Uri.parse(TMDB_DISCOVER_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, ApiKey.THE_MOVIE_DATABASE_APIKEY_V3)
                .appendQueryParameter(SORT_BY_PARAM, MOST_POPULAR_VALUE)
                .appendQueryParameter(INCLUDE_ADULT_PARAM, "false")
                .appendQueryParameter(PARAM_LANGUAGE, VALUE_LANGUAGE)
                .build();

        try {
            return new URL(movieQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * build an URL for query most highest rated movies on TMDb.
     * @return URL to use to query the movies db.
     */
    private static URL buildUrlForHighestRated() {
        setLanguage();
        Uri movieQueryUri = Uri.parse(TMDB_DISCOVER_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, ApiKey.THE_MOVIE_DATABASE_APIKEY_V3)
                .appendQueryParameter(SORT_BY_PARAM, VOTE_AVERAGE)
                .appendQueryParameter(INCLUDE_ADULT_PARAM, "false")
                .appendQueryParameter(MIN_VOTE_COUNT_PARAM, MIN_VOTE_COUNT_VALUE)
                .appendQueryParameter(PARAM_LANGUAGE, VALUE_LANGUAGE)
                .build();
        try {
            return new URL(movieQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * build an URL for query details of movies based on imdb'id.
     * @return URL to query movies details.
     */
    private static URL buildUrlForDetails(int id){
        setLanguage();
        String urlWithId = TMDB_FIND_BY_ID_BASE_URL + id;
        Uri movieQueryUri = Uri.parse(urlWithId).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, ApiKey.THE_MOVIE_DATABASE_APIKEY_V3)
                .appendQueryParameter(APPEND_TO_RESPONSE, VIDEOS)
                .appendQueryParameter(PARAM_LANGUAGE, VALUE_LANGUAGE)
                .build();
        try {
            return new URL(movieQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * build an URL for get reviews from TMDB based on movie's id.
     * @param id of the movie
     * @return URL to get reviews
     */
    private static URL buildUrlForReviews(int id){
        String urlForReviews = TMDB_FIND_BY_ID_BASE_URL + id + REVIEWS;

        Uri reviewsQueryUri = Uri.parse(urlForReviews).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, ApiKey.THE_MOVIE_DATABASE_APIKEY_V3)
                .build();

        try {
            return new URL(reviewsQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get the JSON response to get a list of movie based on the user's query.
     * @param userQuery keyword from the user.
     * @return String json response
     * @throws IOException
     */
    public static String getJsonForUserSearch(String userQuery) throws IOException {
        URL queryUrl = buildUrlForUserSearch(userQuery);

        assert queryUrl != null;
        HttpsURLConnection urlConnection = (HttpsURLConnection) queryUrl.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scan = new Scanner(in);
            // setting the delimiter to \A force the scanner to read the entire content
            // of the stream into the next token stream.
            scan.useDelimiter("\\A");
            // check if there is data to scan.
            boolean hasInput = scan.hasNext();
            if (hasInput) {
                return scan.next();
            } else {
                return null;
            }
        }finally {
            // close the connection.
            urlConnection.disconnect();
        }
    }

    /**
     * this method return the entire Json result from Http response.
     * @return Json data into a String
     * @throws IOException
     */
    public static String getJsonForMainScreen(int queryCode) throws IOException{

        URL queryUrl;
        switch (queryCode){
            case QUERY_CODE_MOST_POPULAR:
                queryUrl = buildUrlForMostPopular();
                break;
            case QUERY_CODE_HIGHEST_RATED:
                queryUrl = buildUrlForHighestRated();
                break;
            default:
                queryUrl = buildUrlForMostPopular();
        }

        assert queryUrl != null;
        HttpsURLConnection urlConnection = (HttpsURLConnection) queryUrl.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scan = new Scanner(in);
            // setting the delimiter to \A force the scanner to read the entire content
            // of the stream into the next token stream.
            scan.useDelimiter("\\A");
            // check if there is data to scan.
            boolean hasInput = scan.hasNext();
            if (hasInput) {
                return scan.next();
            } else {
                return null;
            }
        }finally {
            // close the connection.
            urlConnection.disconnect();
        }
    }

    /**
     * this method return the entire Json result from Http response.
     * Use this method to find a movie by ID to get more details on a movie.
     * @return Json data into a String
     * @throws IOException
     */
    public static String getJsonForDetails(int id) throws IOException {

        URL queryUrl = buildUrlForDetails(id);

        assert queryUrl != null;
        HttpsURLConnection urlConnection = (HttpsURLConnection) queryUrl.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scan = new Scanner(in);
            // setting the delimiter to \A force the scanner to read the entire content
            // of the stream into the next token stream.
            scan.useDelimiter("\\A");
            // check if there is data to scan.
            boolean hasInput = scan.hasNext();
            if (hasInput) {
                return scan.next();
            } else {
                return null;
            }
        }finally {
            // close the connection.
            urlConnection.disconnect();
        }
    }


    /**
     * this method return a Json result with reviews.
     * @param id of the movie
     * @return String containing the reviews in JSON.
     * @throws IOException
     */
    public static String getJsonForReviews(int id) throws IOException {
        URL queryUrl = buildUrlForReviews(id);

        assert queryUrl != null;
        HttpsURLConnection urlConnection = (HttpsURLConnection) queryUrl.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scan = new Scanner(in);
            // setting the delimiter to \A force the scanner to read the entire content
            // of the stream into the next token stream.
            scan.useDelimiter("\\A");
            // check if there is data to scan.
            boolean hasInput = scan.hasNext();
            if (hasInput) {
                return scan.next();
            } else {
                return null;
            }
        }finally {
            // close the connection.
            urlConnection.disconnect();
        }
    }
}
