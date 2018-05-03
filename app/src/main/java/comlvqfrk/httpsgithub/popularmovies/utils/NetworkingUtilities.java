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
    /** Base URL for discover features of the API (TMDB for The Movie DataBase) */
    private static final String TMDB_DISCOVER_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    /** param keyword for api key */
    private static final String API_KEY_PARAM = "api_key";
    /** param keyword for sorting */
    private static final String SORT_BY_PARAM = "sort_by";
    /** param keyword for displaying adult content*/
    private static final String INCLUDE_ADULT_PARAM = "include_adult";

    /**
     * build an URL for query a Json response from themoviedb's API.
     * @return URL to use to query the movies db.
     */
    private static URL buildURl() {
        Uri movieQueryUri = Uri.parse(TMDB_DISCOVER_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, ApiKey.THE_MOVIE_DATABASE_APIKEY_V3)
                .appendQueryParameter(SORT_BY_PARAM, "popularity.desc")
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
     * this method return the entire Json result from Http response.
     * @return Json data into a String
     * @throws IOException
     */
    public static String getJsonResponseFromHttpsUrl() throws IOException{
        URL queryUrl = buildURl();
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
