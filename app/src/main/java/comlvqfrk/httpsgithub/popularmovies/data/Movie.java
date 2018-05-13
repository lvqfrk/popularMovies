package comlvqfrk.httpsgithub.popularmovies.data;

import java.io.Serializable;
import java.net.URL;

public class Movie {

    // TODO: change imdbid reference to tmdbid
    private int mImdbId;
    private String mTitle;
    private String mPosterUrl;

    /**
     * movie object constructor
     * @param imdbId
     * @param title of the movie.
     * @param posterUrl path to the poster.
     */
    public Movie(int imdbId, String title, String posterUrl) {
        mImdbId = imdbId;
        mTitle = title;
        mPosterUrl = posterUrl;

    }

    /**
     * get the imdb'id of the movie
     * @return mImdbId
     */
    public int getImdbId() {
        return mImdbId;
    }

    /**
     * get the title of the movie.
     * @return mTitle
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * get the path to the poster on TMDb.
     * @return mPosterUrl
     */
    public String getPosterUrl() {
        return mPosterUrl;
    }
}
