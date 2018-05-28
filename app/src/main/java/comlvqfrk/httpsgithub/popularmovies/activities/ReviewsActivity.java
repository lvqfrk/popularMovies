package comlvqfrk.httpsgithub.popularmovies.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;
import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.R;
import comlvqfrk.httpsgithub.popularmovies.adapters.ReviewAdapter;
import comlvqfrk.httpsgithub.popularmovies.data.Review;
import comlvqfrk.httpsgithub.popularmovies.utils.JsonParsingUtilities;
import comlvqfrk.httpsgithub.popularmovies.utils.ReviewLoader;

public class ReviewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private int MOVIE_ID;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Intent incIntent = getIntent();
        MOVIE_ID = incIntent.getIntExtra(getString(R.string.bundle_key_movie_id), 0);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView mRecyclerview = findViewById(R.id.rv_reviews);
        mRecyclerview.setLayoutManager(layoutManager);
        mReviewAdapter = new ReviewAdapter(this);
        mRecyclerview.setAdapter(mReviewAdapter);
        loadReviews();
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new ReviewLoader(this, MOVIE_ID);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            List<Review> reviews = JsonParsingUtilities.extractReviewFromJsonResponse(data);
            mReviewAdapter.swapReviews(reviews);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    }

    public void loadReviews(){
        LoaderManager loaderManager = getSupportLoaderManager();
        int REVIEW_LOADER_ID = 88;
        loaderManager.restartLoader(REVIEW_LOADER_ID, null, this);
    }
}
