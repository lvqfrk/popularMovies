package comlvqfrk.httpsgithub.popularmovies.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;


import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.data.Movie;

public class MovieLoader extends AsyncTaskLoader <List<Movie>>{

    /** Tag for log messages  */
    private static final String LOG_TAG = MovieLoader.class.getName();

    /** Constructor for MovieLoader */
    public MovieLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        List<Movie> movies;

        try {
            String jsonStr = NetworkingUtilities.getJsonResponseFromHttpsUrl();
            movies = JsonParsingUtilities.extractDataFromJsonResponse(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return movies;
    }
}
