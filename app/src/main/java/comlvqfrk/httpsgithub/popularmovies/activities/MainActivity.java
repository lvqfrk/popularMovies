package comlvqfrk.httpsgithub.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.adapters.MovieAdapter;
import comlvqfrk.httpsgithub.popularmovies.R;
import comlvqfrk.httpsgithub.popularmovies.data.Movie;
import comlvqfrk.httpsgithub.popularmovies.utils.JsonParsingUtilities;
import comlvqfrk.httpsgithub.popularmovies.utils.MovieLoader;
import comlvqfrk.httpsgithub.popularmovies.utils.NetworkingUtilities;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        MovieAdapter.MovieAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    /* display if no internet connection */
    private TextView mShowMyFavorites;

    /** used to check if Internet is avalaible */
    private boolean connectivityState = false;
    /** Base query type when apps is started */
    private int mQueryPref = NetworkingUtilities.QUERY_CODE_MOST_POPULAR;
    private String mUserSearchQuery;
    /** for save JsonStr response if activity is recreated (changing orientation of the device...)*/
    private String mData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_main_screen);
        mRecyclerView.setVisibility(View.GONE);

        mProgressBar = findViewById(R.id.pb_main_loading);
        mProgressBar.setVisibility(View.VISIBLE);

        mShowMyFavorites = findViewById(R.id.tv_Show_favorites_no_internet);

        TextView mTvInternetError = findViewById(R.id.tv_main_error_internet);
        connectivityState = isNetworkAvailable();

        if (connectivityState) {
            // Create a new Grid Layout manager, with 2 columns and vertical scrolling
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                    2, LinearLayoutManager.VERTICAL, false);

            mRecyclerView.setLayoutManager(gridLayoutManager);
            mMovieAdapter = new MovieAdapter(this, this);
            mRecyclerView.setAdapter(mMovieAdapter);

            if (savedInstanceState != null) {
                mData = savedInstanceState.getString(getString(R.string.bundle_key_callback));
            }else {
                loadMovies(mQueryPref);
            }

        } else {
            mProgressBar.setVisibility(View.GONE);
            mTvInternetError.setVisibility(View.VISIBLE);
            mShowMyFavorites.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (connectivityState) {
            MenuInflater menuInflater = new MenuInflater(this);
            menuInflater.inflate(R.menu.menu_main, menu);

            MenuItem searchItem = menu.findItem(R.id.me_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint(getString(R.string.hint_search_query));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mUserSearchQuery = query;
                    loadMovies(NetworkingUtilities.QUERY_CODE_SEARCH_BY_TITLE);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.me_sort_most_popular:
                // query movie by most Popular ( 100 == most popular)
                mQueryPref = NetworkingUtilities.QUERY_CODE_MOST_POPULAR;
                loadMovies(mQueryPref);
                return true;
            case R.id.me_sort_hightest_rated:
                // query movie by most Popular ( 101 == best rates)
                mQueryPref = NetworkingUtilities.QUERY_CODE_HIGHEST_RATED;
                loadMovies(mQueryPref);
                return true;
            case R.id.me_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            case R.id.me_to_favorites:
                Intent startFavoritesActivity = new Intent(this, FavoritesActivity.class);
                startActivity(startFavoritesActivity);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.bundle_key_callback), mData);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            List<Movie> movies = JsonParsingUtilities.extractMoviesFromJson(mData);
            mMovieAdapter.swapMovies(movies);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        assert args != null;
        int queryType = args.getInt(getString(R.string.bundle_key_query_code));
            switch (queryType){
                case NetworkingUtilities.QUERY_CODE_SEARCH_BY_TITLE:
                    return new MovieLoader(this, queryType, mUserSearchQuery);
                default:
                    return new MovieLoader(this, queryType);
            }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        mData = data;
        try {
            List<Movie> movies = JsonParsingUtilities.extractMoviesFromJson(data);
            mMovieAdapter.swapMovies(movies);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    /**
     * check for network available
     * @return true or false, depending if internet if avalaible.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * user to start the loader that request a list of movieto display.
     * @param queryCode : the type of the request (Most popular, highest rated ...)
     */
    public void loadMovies(int queryCode){
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.bundle_key_query_code), queryCode);
        LoaderManager loaderManager = getSupportLoaderManager();
        int TMDB_LOADER_ID = 22;
        loaderManager.restartLoader(TMDB_LOADER_ID, bundle, this);
    }

    @Override
    public void onClick(Movie currentMovie) {
        Intent detailIntent = new Intent(this, DetailsActivity.class);
        detailIntent.putExtra(getString(R.string.bundle_key_movie_id), currentMovie.getImdbId());
        startActivity(detailIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.prefs_key_show_titles_on_main_screen))){
            try {
                List<Movie> movies = JsonParsingUtilities.extractMoviesFromJson(mData);
                mMovieAdapter.swapMovies(movies);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void toFavorites(View view) {
        Intent startFavorites = new Intent(this, FavoritesActivity.class);
        startActivity(startFavorites);
    }
}
