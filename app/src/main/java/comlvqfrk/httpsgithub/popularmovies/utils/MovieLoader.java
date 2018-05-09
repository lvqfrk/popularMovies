package comlvqfrk.httpsgithub.popularmovies.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;


import java.io.IOException;
import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.data.Movie;

public class MovieLoader extends AsyncTaskLoader <String>{

    /** Tag for log messages  */
    private static final String LOG_TAG = MovieLoader.class.getName();

    private static int mQueryCode = 100;

    private static int mImdbId;

    /** use as cache for httpResponse */
    private String mData;

    /** Constructor for MovieLoader */
    public MovieLoader(Context context, int queryCode) {
        super(context);
        mQueryCode = queryCode;
    }

    public MovieLoader(Context context, int queryCode, int imdbId) {
        super(context);
        mQueryCode = queryCode;
        mImdbId = imdbId;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
            return;
        }
            forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {

        switch (mQueryCode){
            // queryCode 100 for searching most popular
            case 100:
            // queryCode 102 for highest rated
            case 101:
                try {
                    return NetworkingUtilities.getJsonForMainScreen(mQueryCode);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            // queryCode 150 for searching movies by imdb's ID
            case 150:
                try {
                    return NetworkingUtilities.getJsonForDetails(mImdbId);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            default: return null;
        }

    }

    @Override
    public void deliverResult(@Nullable String data) {
        mData = data;
        super.deliverResult(data);
    }
}
