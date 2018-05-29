package comlvqfrk.httpsgithub.popularmovies.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import comlvqfrk.httpsgithub.popularmovies.R;
import comlvqfrk.httpsgithub.popularmovies.adapters.FavoritesAdapter;
import comlvqfrk.httpsgithub.popularmovies.utils.FavoriteContract;

public class FavoritesActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // TODO: strings value and translate
            actionBar.setTitle("Favorites");
        }

        Cursor favCursor = loadFavoriteFromDb();

        FavoritesAdapter favAdapter = new FavoritesAdapter(this, favCursor);
        ListView lvFavorites = findViewById(R.id.lv_favorites);
        lvFavorites.setAdapter(favAdapter);
        lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int tmdbId = Integer.valueOf(view.getTag().toString());
                Intent startDetailsActivity = new Intent(getApplicationContext(), DetailsActivity.class);
                startDetailsActivity.putExtra(getString(R.string.bundle_key_movie_id), tmdbId);
                startActivity(startDetailsActivity);
            }
        });
    }

    private Cursor loadFavoriteFromDb() {
        ContentResolver cr = getContentResolver();
        return cr.query(FavoriteContract.FavoriteEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }


}
