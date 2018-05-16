package comlvqfrk.httpsgithub.popularmovies.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.Nullable;

import java.io.IOException;

public class ReviewLoader extends AsyncTaskLoader<String> {

    private static int mMovieId;

    private String mData;

    public ReviewLoader(Context context, int id) {
        super(context);
        mMovieId = id;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
            return;
        }
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try {
            return NetworkingUtilities.getJsonForReviews(mMovieId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(@Nullable String data) {
        mData = data;
        super.deliverResult(data);
    }
}
