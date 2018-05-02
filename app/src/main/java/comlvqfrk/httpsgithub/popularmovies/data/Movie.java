package comlvqfrk.httpsgithub.popularmovies.data;

import java.net.URL;

public class Movie {

    private String mTitle;
    private String mPosterUrl;
    private double mVoteAverage;
    private String mOverview;
    private String mReleaseDate;

    /**
     * movie object constructor
     * @param title of the movie.
     * @param posterUrl path to the poster.
     * @param voteAverage average of user's vote.
     * @param overview synospis of the movie.
     * @param releaseDate for the movie.
     */
    public Movie(String title, String posterUrl, double voteAverage,
                 String overview, String releaseDate) {
        mTitle = title;
        mPosterUrl = posterUrl;
        mVoteAverage = voteAverage;
        mOverview = overview;
        mReleaseDate = releaseDate;
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

    /**
     * get the vote average of users from TMDb.
     * @return mVoteAverage
     */
    public double getVoteAverage() {
        return mVoteAverage;
    }

    /**
     * get the overview
     * @return mOverview
     */
    public String getOverview() {
        return mOverview;
    }

    /**
     * get the release date
     * @return mReleaseDate
     */
    public String getReleaseDate() {
        return mReleaseDate;
    }
}
