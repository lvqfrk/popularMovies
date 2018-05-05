package comlvqfrk.httpsgithub.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.data.Movie;
import comlvqfrk.httpsgithub.popularmovies.utils.MovieLoader;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>{

    private final int TMDB_LOADER_ID = 22;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mTvInternetError;

    private boolean connectivityState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main_screen);
        mRecyclerView.setVisibility(View.GONE);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_main_loading);
        mProgressBar.setVisibility(View.VISIBLE);

        mTvInternetError = (TextView) findViewById(R.id.tv_main_error_internet);
        connectivityState = isNetworkAvailable();

        if (connectivityState) {
            /** Create a new Grid Layout manager, with 2 columns and vertical scrolling */
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                    2, LinearLayoutManager.VERTICAL, false);

            mRecyclerView.setLayoutManager(gridLayoutManager);

            mMovieAdapter = new MovieAdapter(this);
            mRecyclerView.setAdapter(mMovieAdapter);

            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(TMDB_LOADER_ID, null, this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mTvInternetError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.me_sort_most_popular:
                loadMovies("popularity.desc");
                return true;
            case R.id.me_sort_hightest_rated:
                loadMovies("vote_average.desc");
                return true;
            case R.id.me_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        if (args == null) {
            return new MovieLoader(this, null);
        } else {
            String sortOrder = args.getString("key_sort");
            return new MovieLoader(this, sortOrder);
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        mMovieAdapter.swapMovies(data);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }

    /**
     * check for network available
     * @return true or false, depending if internet if avalaible.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void loadMovies(String sort_order){
        Bundle bundle = new Bundle();
        bundle.putString("key_sort", sort_order);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(TMDB_LOADER_ID, bundle, this);
    }

}
