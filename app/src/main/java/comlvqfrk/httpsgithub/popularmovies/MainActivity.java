package comlvqfrk.httpsgithub.popularmovies;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.data.Movie;
import comlvqfrk.httpsgithub.popularmovies.utils.JsonParsingUtilities;
import comlvqfrk.httpsgithub.popularmovies.utils.NetworkingUtilities;


public class MainActivity extends AppCompatActivity {

    private TextView mTvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvTest = (TextView) findViewById(R.id.tv_test_response);
    }
}
