package comlvqfrk.httpsgithub.popularmovies;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import comlvqfrk.httpsgithub.popularmovies.data.DetailedMovie;
import comlvqfrk.httpsgithub.popularmovies.utils.JsonParsingUtilities;
import comlvqfrk.httpsgithub.popularmovies.utils.MovieLoader;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    /** base Url for getting poster image from TMDB, width 200 */
    private static final String TMDB_POSTER_W185_BASE_URL = "http://image.tmdb.org/t/p/w185//";

    private static final String TMDB_BACKDROP_W1280_BASE_URL = "http://image.tmdb.org/t/p/w1280//";

    private static final int mQueryCode = 150;

    private final int TMDB_LOADER_ID = 22;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private ImageView ivBackdrop;
    private TextView tvDetailTitle;
    private TextView tvDetailVoteAverage;
    private TextView tvDetailReleaseDate;
    private ImageView ivDetailPoster;
    private TextView tvDetailOverview;

    // data from Itent
    private int MOVIE_IMDB_ID;
    private DetailedMovie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewCompat.setTransitionName(findViewById(R.id.appBarLayout), "Name");

        collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("title");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Intent incIntent = getIntent();
        MOVIE_IMDB_ID = incIntent.getIntExtra("IMDB_ID", 0);


        ivBackdrop = findViewById(R.id.iv_backdrop);
        // title TextView
        tvDetailTitle = findViewById(R.id.tv_details_title);
        // Vote average TextView
        tvDetailVoteAverage = findViewById(R.id.tv_details_vote_average);
        // release date TextView
        tvDetailReleaseDate = findViewById(R.id.tv_details_release_release_date);
        // poster ImageView
        ivDetailPoster = findViewById(R.id.iv_details_poster);
        // overview TextView
        tvDetailOverview = findViewById(R.id.tv_details_synopsis_content);

        // TODO : add a progressBar and error View that handle connection error.
        loadDetails();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new MovieLoader(this, mQueryCode, MOVIE_IMDB_ID);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        try {
            currentMovie = JsonParsingUtilities.extractDetailsFromJsonResponse(data);

            collapsingToolbarLayout.setTitle(currentMovie.getTitle());
            tvDetailTitle.setText(currentMovie.getTitle());
            tvDetailVoteAverage.setText(currentMovie.getVoteAverage() + "");
            tvDetailReleaseDate.setText(currentMovie.getReleaseDate());
            tvDetailOverview.setText(currentMovie.getOverview());
            String urlToPoster = TMDB_POSTER_W185_BASE_URL + currentMovie.getPosterUrl();
            Picasso.with(this).load(urlToPoster).into(ivDetailPoster);
            String urlToBackdrop = TMDB_BACKDROP_W1280_BASE_URL + currentMovie.getBackdropPath();
            Picasso.with(this).load(urlToBackdrop).into(ivBackdrop);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public void loadDetails(){
        Bundle bundle = new Bundle();
        bundle.putInt("queryType", mQueryCode);
        bundle.putInt("id", MOVIE_IMDB_ID);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(TMDB_LOADER_ID, bundle, this);
    }
}
