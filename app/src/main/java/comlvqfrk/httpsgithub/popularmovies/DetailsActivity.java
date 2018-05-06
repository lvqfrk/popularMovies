package comlvqfrk.httpsgithub.popularmovies;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private int IMDB_ID;

    private TextView mDetailTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        mDetailTitle = findViewById(R.id.tv_details_title);

        Intent incIntent = getIntent();
        IMDB_ID = incIntent.getIntExtra("EXTRA_ID", 0);

        mDetailTitle.setText(IMDB_ID + "");

    }
}
