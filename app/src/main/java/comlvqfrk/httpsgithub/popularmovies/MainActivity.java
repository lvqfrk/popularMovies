package comlvqfrk.httpsgithub.popularmovies;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.data.Movie;
import comlvqfrk.httpsgithub.popularmovies.utils.JsonParsingUtilities;
import comlvqfrk.httpsgithub.popularmovies.utils.MovieLoader;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, MovieAdapter.MovieAdapterOnClickHandler{

    //TODO : handle cases where there is no poster for the movie.
    private final int TMDB_LOADER_ID = 22;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mTvInternetError;

    private boolean connectivityState = false;
    private int mQueryPref = 100;

    private String mUserSearchQuery;

    private String mData = null;

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
            // Create a new Grid Layout manager, with 2 columns and vertical scrolling
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                    2, LinearLayoutManager.VERTICAL, false);

            mRecyclerView.setLayoutManager(gridLayoutManager);
            mMovieAdapter = new MovieAdapter(this, this);
            mRecyclerView.setAdapter(mMovieAdapter);

            if (savedInstanceState != null) {
                mData = savedInstanceState.getString("callback");
            }else {
                loadMovies(mQueryPref);
            }

        } else {
            mProgressBar.setVisibility(View.GONE);
            mTvInternetError.setVisibility(View.VISIBLE);
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
            searchView.setQueryHint("Search a Movie");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(MainActivity.this, "search...", Toast.LENGTH_SHORT).show();
                    mUserSearchQuery = query;
                    loadMovies(102);
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
                mQueryPref = 100;
                loadMovies(mQueryPref);
                return true;
            case R.id.me_sort_hightest_rated:
                // query movie by most Popular ( 101 == best rates)
                mQueryPref = 101;
                loadMovies(mQueryPref);
                return true;
            case R.id.me_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("callback", mData);
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
            int queryType = args.getInt("queryType");
            switch (queryType){
                case 102:
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
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void loadMovies(int queryCode){
        Bundle bundle = new Bundle();
        bundle.putInt("queryType", queryCode);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(TMDB_LOADER_ID, bundle, this);
    }

    @Override
    public void onClick(Movie currentMovie) {
        Context context = this;
        Intent detailIntent = new Intent(this, DetailsActivity.class);
        detailIntent.putExtra("IMDB_ID", currentMovie.getImdbId());
        startActivity(detailIntent);
    }
}
