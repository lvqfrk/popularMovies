package comlvqfrk.httpsgithub.popularmovies;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import comlvqfrk.httpsgithub.popularmovies.data.Movie;

public class DetailsActivity extends AppCompatActivity {

    /** base Url for getting poster image from TMDB, width 200 */
    private static final String TMDB_POSTER_W185_BASE_URL = "http://image.tmdb.org/t/p/w342//";

    private TextView tvDetailTitle;
    private TextView tvDetailVoteAverage;
    private TextView tvDetailReleaseDate;
    private ImageView ivDetailPoster;
    private TextView tvDetailOverview;

    // data from Itent
    private int MOVIE_IMDB_ID;
    private String MOVIE_TITLE;
    private double MOVIE_VOTE_AVERAGE;
    private String MOVIE_OVERVIEW;
    private String MOVIE_RELEASE_DATE;
    private String MOVIE_POSTER_PATH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent incIntent = getIntent();
        MOVIE_IMDB_ID = incIntent.getIntExtra("IMDB_ID", 0);
        MOVIE_TITLE = incIntent.getStringExtra("TITLE");
        MOVIE_VOTE_AVERAGE = incIntent.getDoubleExtra("VOTE", 0.0);
        MOVIE_OVERVIEW = incIntent.getStringExtra("OVERVIEW");
        MOVIE_RELEASE_DATE = incIntent.getStringExtra("RELEASE_DATE");
        MOVIE_POSTER_PATH = incIntent.getStringExtra("POSTER_PATH");

        // title TextView
        tvDetailTitle = findViewById(R.id.tv_details_title);
        tvDetailTitle.setText(MOVIE_TITLE);
        // Vote average TextView
        tvDetailVoteAverage = findViewById(R.id.tv_details_vote_average);
        tvDetailVoteAverage.setText(MOVIE_VOTE_AVERAGE + "");
        // release date TextView
        tvDetailReleaseDate = findViewById(R.id.tv_details_release_release_date);
        tvDetailReleaseDate.setText(MOVIE_RELEASE_DATE);
        // poster ImageView
        ivDetailPoster = findViewById(R.id.iv_details_poster);
        String posterUrl = TMDB_POSTER_W185_BASE_URL + MOVIE_POSTER_PATH;
        Picasso.with(this).load(posterUrl).into(ivDetailPoster);
        // overview TextView
        tvDetailOverview = findViewById(R.id.tv_details_synopsis_content);
        tvDetailOverview.setText(MOVIE_OVERVIEW);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
