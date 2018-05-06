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
    private long MOVIE_VOTE_AVERAGE;
    private String MOVIE_OVERVIEW;
    private String MOVIE_RELEASE_DATE;
    private String MOVIE_POSTER_PATH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        tvDetailTitle = findViewById(R.id.tv_details_title);
        tvDetailVoteAverage = findViewById(R.id.tv_details_vote_average);
        tvDetailReleaseDate = findViewById(R.id.tv_details_release_release_date);
        ivDetailPoster = findViewById(R.id.iv_details_poster);
        tvDetailOverview = findViewById(R.id.tv_details_synopsis_content);

        Intent incIntent = getIntent();
        MOVIE_IMDB_ID = incIntent.getIntExtra("IMDB_ID", 0);
        MOVIE_TITLE = incIntent.getStringExtra("TITLE");
        MOVIE_VOTE_AVERAGE = incIntent.getLongExtra("VOTE", 0);
        MOVIE_OVERVIEW = incIntent.getStringExtra("OVERVIEW");
        MOVIE_RELEASE_DATE = incIntent.getStringExtra("RELEASE_DATE");
        MOVIE_POSTER_PATH = incIntent.getStringExtra("POSTER_PATH");

        tvDetailTitle.setText(MOVIE_TITLE);
        tvDetailVoteAverage.setText(String.valueOf(MOVIE_VOTE_AVERAGE));
        tvDetailReleaseDate.setText(MOVIE_RELEASE_DATE);

        String posterUrl = TMDB_POSTER_W185_BASE_URL + MOVIE_POSTER_PATH;
        tvDetailOverview.setText(MOVIE_OVERVIEW);

        Picasso.with(this).load(posterUrl).into(ivDetailPoster);



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

//detailIntent.putExtra("IMDB_ID", currentMovie.getImdbId());
//        detailIntent.putExtra("TITLE", currentMovie.getTitle());
//        detailIntent.putExtra("VOTE", currentMovie.getVoteAverage());
//        detailIntent.putExtra("OVERVIEW", currentMovie.getOverview());
//        detailIntent.putExtra("POSTER_PATH", currentMovie.getPosterUrl());
//        detailIntent.putExtra("RELEASE_DATE", currentMovie.getReleaseDate());

// Picasso.with(mContext).load(posterUrl).into(holder.ivPoster);
