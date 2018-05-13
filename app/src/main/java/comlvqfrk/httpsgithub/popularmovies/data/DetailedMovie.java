package comlvqfrk.httpsgithub.popularmovies.data;

public class DetailedMovie extends Movie{

    private double mVoteAverage;
    private String mOverview;
    private String mReleaseDate;
    private String mTrailerPath;

    /**
     * movie object constructor
     *
     * @param imdbId
     * @param title of the movie.
     * @param posterUrl path to the poster.
     * @param voteAverage user's vote for the movie
     * @param overview
     * @param releaseDate
     * @param trailerPath
     */
    public DetailedMovie(int imdbId, String title, String posterUrl,
                         double voteAverage, String overview, String releaseDate,
                         String trailerPath) {
        super(imdbId, title, posterUrl);
        mVoteAverage = voteAverage;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mTrailerPath = trailerPath;
    }

    /**
     * get the rate for the movie.
     * @return mVoteAverage
     */
    public double getVoteAverage(){
        return mVoteAverage;
    }

    /**
     * get the overview.
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

    /**
     * get the trailer path
     * @return mTrailerPath
     */
    public String getTrailerPath(){
        return mTrailerPath;
    }
}
