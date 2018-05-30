package comlvqfrk.httpsgithub.popularmovies.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.R;
import comlvqfrk.httpsgithub.popularmovies.data.DetailedMovie;
import comlvqfrk.httpsgithub.popularmovies.data.Review;
import comlvqfrk.httpsgithub.popularmovies.utils.DbBitmapUtility;
import comlvqfrk.httpsgithub.popularmovies.utils.FavoriteContract;
import comlvqfrk.httpsgithub.popularmovies.utils.JsonParsingUtilities;
import comlvqfrk.httpsgithub.popularmovies.utils.MovieLoader;
import comlvqfrk.httpsgithub.popularmovies.utils.NetworkingUtilities;
import comlvqfrk.httpsgithub.popularmovies.utils.ReviewLoader;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    /** base Url for getting poster image from TMDB */
    private static final String TMDB_POSTER_W185_BASE_URL = "http://image.tmdb.org/t/p/w185//";
    /** base Url for getting backdrop image from TMDB*/
    private static final String TMDB_BACKDROP_W1280_BASE_URL = "http://image.tmdb.org/t/p/w1280//";
    /** base Url for Youtube */
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    /** mQuerycode 150 == details*/
    private static final int mQueryCode = NetworkingUtilities.QUERY_CODE_GET_DETAILS;
    /** id for the details Loader */
    private final int TMDB_LOADER_ID = 22;
    /** id for the reviews loader */
    private final int TMDB_REVIEW_LOADER_ID = 44;

    /** favorite status */
    private boolean mIsFavorite = false;

    private FrameLayout flDetailsLoading;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView ivBackdrop;
    private ImageView ivBtnPlay;
    private TextView tvDetailTitle;
    private TextView tvDetailVoteAverage;
    private TextView tvDetailReleaseDate;
    private ImageView ivDetailPoster;
    private TextView tvDetailOverview;
    private FloatingActionButton fabFavorite;

    // Review part
    private TextView tvFirstReviewAuthor;
    private TextView tvFirstReviewContent;
    private TextView tvSecondReviewAuthor;
    private TextView tvSecondReviewContent;
    private TextView tvThirdReviewAuthor;
    private TextView tvThirdReviewContent;

    // data from Intent
    private int MOVIE_IMDB_ID;
    // Intent to watch trailer
    private Intent trailerIntent;
    // Intent to share trailer
    private Intent shareIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewCompat.setTransitionName(findViewById(R.id.appBarLayout), "Name");

        // Show the progress bar while loading.
        flDetailsLoading = findViewById(R.id.fl_details_loading);
        flDetailsLoading.setVisibility(View.VISIBLE);

        if (isNetworkAvailable()) {
            collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle("title");
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            Intent incIntent = getIntent();
            MOVIE_IMDB_ID = incIntent.getIntExtra(getString(R.string.bundle_key_movie_id), 0);

            // header background
            ivBackdrop = findViewById(R.id.iv_backdrop);

            // play button on the header
            ivBtnPlay = findViewById(R.id.iv_btn_play);
            ivBtnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (trailerIntent.resolveActivity(getPackageManager()) != null){
                        startActivity(trailerIntent);
                    }
                }
            });

            displayFavBtn();

            // Details : title, release date, vote, poster image.
            tvDetailTitle = findViewById(R.id.tv_details_title);
            tvDetailVoteAverage = findViewById(R.id.tv_details_vote_average);
            tvDetailReleaseDate = findViewById(R.id.tv_details_release_release_date);
            ivDetailPoster = findViewById(R.id.iv_details_poster);
            tvDetailOverview = findViewById(R.id.tv_details_synopsis_content);
            loadDetails();

            // Reviews
            tvFirstReviewAuthor = findViewById(R.id.tv_first_review_author);
            tvFirstReviewContent = findViewById(R.id.tv_first_review_content);
            tvSecondReviewAuthor = findViewById(R.id.tv_second_review_author);
            tvSecondReviewContent = findViewById(R.id.tv_second_review_content);
            tvThirdReviewAuthor = findViewById(R.id.tv_third_review_author);
            tvThirdReviewContent = findViewById(R.id.tv_third_review_content);
            loadReviews();

            TextView tvShowReview = findViewById(R.id.tv_show_reviews);


            tvShowReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent reviewsIntent = new Intent(getApplicationContext(), ReviewsActivity.class);
                    reviewsIntent.putExtra(getString(R.string.bundle_key_movie_id), MOVIE_IMDB_ID);
                    startActivity(reviewsIntent);
                }
            });
        } else {
            FrameLayout internetError = findViewById(R.id.fl_details_internet_error);
            internetError.setVisibility(View.VISIBLE);
            flDetailsLoading.setVisibility(View.GONE);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_details, menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.me_share_trailer:
                if (shareIntent != null) {
                    startActivity(shareIntent);
                } else {
                    Toast.makeText(this, "no trailer to share.", Toast.LENGTH_SHORT)
                            .show();
                }
                return true;
            default:
                return false;
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            // loader for reviews
            case TMDB_REVIEW_LOADER_ID:
                return new ReviewLoader(this, MOVIE_IMDB_ID);
            // default loader TMDB_LOADER_ID, for loading details
            default:
                return new MovieLoader(this, mQueryCode, MOVIE_IMDB_ID);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        // loader for details about the movie
        if (loader.getId() == TMDB_LOADER_ID) {
            try {
                DetailedMovie currentMovie = JsonParsingUtilities.extractDetailsFromJsonResponse(data, this);

                collapsingToolbarLayout.setTitle(currentMovie.getTitle());
                tvDetailTitle.setText(currentMovie.getTitle());
                tvDetailVoteAverage.setText(String.valueOf(currentMovie.getVoteAverage()));
                tvDetailReleaseDate.setText(currentMovie.getReleaseDate());
                tvDetailOverview.setText(currentMovie.getOverview());
                String urlToPoster = TMDB_POSTER_W185_BASE_URL + currentMovie.getPosterUrl();
                Picasso.with(this).load(urlToPoster).into(ivDetailPoster);
                String urlToBackdrop = TMDB_BACKDROP_W1280_BASE_URL + currentMovie.getBackdropPath();
                Picasso.with(this).load(urlToBackdrop).into(ivBackdrop);

                String trailerPath = currentMovie.getTrailerPath();
                if(trailerPath == null) {
                    ivBtnPlay.setVisibility(View.GONE);
                    shareIntent = null;
                } else {
                    String trailerUrl = YOUTUBE_BASE_URL + trailerPath;
                    trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                    shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, trailerUrl);
                    shareIntent.setType(getString(R.string.share_intent_type));
                }
                flDetailsLoading.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Loader for reviews
        else if (loader.getId() == TMDB_REVIEW_LOADER_ID) {
            List<Review> reviews;
            try {
                reviews = JsonParsingUtilities.extractReviewFromJsonResponse(data);

                assert reviews != null;
                if (reviews.size() >= 3) {
                    tvFirstReviewAuthor.setText(reviews.get(0).getReviewAuthor());
                    tvFirstReviewContent.setText(reviews.get(0).getReviewContent());
                    tvSecondReviewAuthor.setText(reviews.get(1).getReviewAuthor());
                    tvSecondReviewContent.setText(reviews.get(1).getReviewContent());
                    tvThirdReviewAuthor.setText(reviews.get(2).getReviewAuthor());
                    tvThirdReviewContent.setText(reviews.get(2).getReviewContent());
                } else if (reviews.size() == 2) {
                    tvFirstReviewAuthor.setText(reviews.get(0).getReviewAuthor());
                    tvFirstReviewContent.setText(reviews.get(0).getReviewContent());
                    tvSecondReviewAuthor.setText(reviews.get(1).getReviewAuthor());
                    tvSecondReviewContent.setText(reviews.get(1).getReviewContent());
                    tvThirdReviewAuthor.setVisibility(View.GONE);
                    tvThirdReviewContent.setVisibility(View.GONE);
                } else if (reviews.size() == 1) {
                    tvFirstReviewAuthor.setText(reviews.get(0).getReviewAuthor());
                    tvFirstReviewContent.setText(reviews.get(0).getReviewContent());
                    tvSecondReviewAuthor.setVisibility(View.GONE);
                    tvSecondReviewContent.setVisibility(View.GONE);
                    tvThirdReviewAuthor.setVisibility(View.GONE);
                    tvThirdReviewContent.setVisibility(View.GONE);
                } else {
                    tvFirstReviewAuthor.setVisibility(View.GONE);
                    tvFirstReviewContent.setText(R.string.no_reviews_available);
                    tvSecondReviewAuthor.setVisibility(View.GONE);
                    tvSecondReviewContent.setVisibility(View.GONE);
                    tvThirdReviewAuthor.setVisibility(View.GONE);
                    tvThirdReviewContent.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    /**
     * this method is used to start the loader that request movie's details.
     */
    public void loadDetails(){
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.bundle_key_query_code), mQueryCode);
        bundle.putInt(getString(R.string.bundle_key_movie_id), MOVIE_IMDB_ID);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(TMDB_LOADER_ID, bundle, this);
    }

    /**
     * this method is used to start the loader that request reviews.
     */
    public void loadReviews(){
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.bundle_key_movie_id), MOVIE_IMDB_ID);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(TMDB_REVIEW_LOADER_ID, bundle, this);
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
     * this method set the correct color to the fav button and the correct method onCLick,
     * depending if the movie is in the favorite database.
     */
    private void displayFavBtn() {
        checkIsFavorite();
        fabFavorite = findViewById(R.id.fab_favorite);
        if(mIsFavorite) fabFavorite.setColorFilter(getResources().getColor(R.color.colorPrimary));
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsFavorite) {
                    addToFavorites();
                } else {
                    removeFromFavorites();
                }
            }
        });
    }

    /**
     * used to add a movie in the favorite database.
     */
    private void addToFavorites() {
        String title = tvDetailTitle.getText().toString();
        String releaseDate = tvDetailReleaseDate.getText().toString();
        String overview = tvDetailOverview.getText().toString();
        String rate = tvDetailVoteAverage.getText().toString();
        // get the bitmap of the poster.
        Bitmap posterAsBitmap = ((BitmapDrawable) ivDetailPoster.getDrawable()).getBitmap();
        //convert the bitmap to byte array to store in db.
        byte[] posterAsByte = DbBitmapUtility.getBytes(posterAsBitmap);
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TMDB_ID, MOVIE_IMDB_ID);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, title);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, releaseDate);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_RATE, rate);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW, overview);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER, posterAsByte);
        ContentResolver cr = getContentResolver();
        cr.insert(FavoriteContract.FavoriteEntry.CONTENT_URI, values);
        fabFavorite.setColorFilter(getResources().getColor(R.color.colorPrimary));
        mIsFavorite = true;
        Toast.makeText(this, "The movie is added to your favorite.",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * used to remove the movie from favorite database.
     */
    private void removeFromFavorites() {
        ContentResolver cr = getContentResolver();
        String where = FavoriteContract.FavoriteEntry.COLUMN_TMDB_ID + " = ? ";
        String[] whereArgs = {String.valueOf(MOVIE_IMDB_ID)};
        cr.delete(FavoriteContract.FavoriteEntry.CONTENT_URI,
                where,
                whereArgs);
        fabFavorite.setColorFilter(getResources().getColor(R.color.colorWhite));
        mIsFavorite = false;
        Toast.makeText(this, "The movie is deleted from your favorite.",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * used to check if the movie is already in favorite database.
     */
    private void checkIsFavorite() {
        ContentResolver cr = getContentResolver();
        String where = FavoriteContract.FavoriteEntry.COLUMN_TMDB_ID + " = ? ";
        String[] whereArgs = {String.valueOf(MOVIE_IMDB_ID)};
        Cursor cursor = cr.query(FavoriteContract.FavoriteEntry.CONTENT_URI,
                null,
                where,
                whereArgs,
                null);
        if (cursor.getCount() > 0) mIsFavorite = true;
    }

}
