package comlvqfrk.httpsgithub.popularmovies;

import android.content.AsyncTaskLoader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.data.Movie;
import comlvqfrk.httpsgithub.popularmovies.utils.NetworkingUtilities;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private final int TMDB_LOADER_ID = 22;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main_screen);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                2, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(TMDB_LOADER_ID, null, this);

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {

                String resultFromHttp = "";
                try {
                    resultFromHttp = NetworkingUtilities.getJsonResponseFromHttpsUrl();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return resultFromHttp;
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        List<Movie> movies = new ArrayList<>();
        Movie movTest = new Movie(data, "test", 5.5, "test", "test");
        movies.add(movTest);
        mMovieAdapter.swapMovies(movies);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
