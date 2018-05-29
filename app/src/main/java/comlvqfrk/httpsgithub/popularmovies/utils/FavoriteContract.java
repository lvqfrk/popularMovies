package comlvqfrk.httpsgithub.popularmovies.utils;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

    public static final String CONTENT_AUTHORITY = "comlvqfrk.httpsgithub.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /* define the content of the favorites table */
    public static final class FavoriteEntry implements BaseColumns {
        /* name of the table used by the database */
        public static final String TABLE_NAME = "favorites";

        /* name of the column to store id's of movies */
        public static final String COLUMN_TMDB_ID = "tmdb_id";
        /* name of the column to store the title*/
        public static final String COLUMN_TITLE = "title";
        /* name of the column to store the poster */
        public static final String COLUMN_POSTER = "poster";
        /* name of the column to store the release date */
        public static final String COLUMN_RELEASE_DATE = "release_date";
        /* name of the column to store the rates average */
        public static final String COLUMN_RATE = "rate";
        /* name of the column to store the overview of the movie*/
        public static final String COLUMN_OVERVIEW = "overview";

        /* The base CONTENT_URI used to query the favorite table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();

        /**
         * Builds a URI that adds the movies _ID to the end of the favorite content URI path.
         * This is used to query details about a single favorite entry by _ID. This is what we
         * use for the detail view query.
         *
         * @param id Unique id pointing to that row
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildFavoriteUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }
}
