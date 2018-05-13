package comlvqfrk.httpsgithub.popularmovies.utils;

import android.net.Uri;
import android.support.annotation.Nullable;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;

public class NetworkingUtilities {

    private static final String TAG = NetworkingUtilities.class.getSimpleName();
    /** Base Url to find movie by ID */
    private static final String TMDB_FIND_BY_ID_BASE_URL = "https://api.themoviedb.org/3/movie/";
    /** Base URL for discover features of the API (TMDB for The Movie DataBase) */
    private static final String TMDB_DISCOVER_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    /** param keyword for api key */
    private static final String API_KEY_PARAM = "api_key";
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

    /**
     * build an URL for query most popular movies on TMDb.
     * @return URL to use to query the movies db.
     */
    private static URL buildUrlForMostPopular() {
        Uri movieQueryUri = Uri.parse(TMDB_DISCOVER_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, ApiKey.THE_MOVIE_DATABASE_APIKEY_V3)
                .appendQueryParameter(SORT_BY_PARAM, MOST_POPULAR_VALUE)
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
     * build an URL for query most highest rated movies on TMDb.
     * @return URL to use to query the movies db.
     */
    private static URL buildUrlForHighestRated() {
        Uri movieQueryUri = Uri.parse(TMDB_DISCOVER_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, ApiKey.THE_MOVIE_DATABASE_APIKEY_V3)
                .appendQueryParameter(SORT_BY_PARAM, VOTE_AVERAGE)
                .appendQueryParameter(INCLUDE_ADULT_PARAM, "false")
                .appendQueryParameter(MIN_VOTE_COUNT_PARAM, MIN_VOTE_COUNT_VALUE)
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
        String urlWithId = TMDB_FIND_BY_ID_BASE_URL + id;

        Uri movieQueryUri = Uri.parse(urlWithId).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, ApiKey.THE_MOVIE_DATABASE_APIKEY_V3)
                .appendQueryParameter(APPEND_TO_RESPONSE, VIDEOS)
                .build();
        try {
            return new URL(movieQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
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
            case 100:
                queryUrl = buildUrlForMostPopular();
                break;
            case 101:
                queryUrl = buildUrlForHighestRated();
                break;
            default:
                queryUrl = buildUrlForMostPopular();
        }

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
    public static String getJsonForDetails(int id) throws IOException{

        URL queryUrl = buildUrlForDetails(id);

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
