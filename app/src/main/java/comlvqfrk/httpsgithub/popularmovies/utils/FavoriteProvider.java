package comlvqfrk.httpsgithub.popularmovies.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FavoriteProvider extends ContentProvider{

    public static final int CODE_FAVORITE = 100;
    public static final int CODE_FAVORTIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavoriteDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoriteContract.FavoriteEntry.TABLE_NAME, CODE_FAVORITE);
        matcher.addURI(authority, FavoriteContract.FavoriteEntry.TABLE_NAME + "/#", CODE_FAVORTIE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FavoriteDbHelper(getContext());
        return mOpenHelper != null;
    }

    /**
     * Handles query requests from clients. We will use this method to query for all
     * of our favorites data as well as to query for the specific favorite record.
     *
     * @param uri           The URI to query
     * @param projection    The list of columns to put into the cursor. If null, all columns are
     *                      included.
     * @param selection     A selection criteria to apply when filtering rows. If null, then all
     *                      rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the
     *                      selection.
     * @param sortOrder     How the rows in the cursor should be sorted.
     * @return A Cursor containing the results of the query. In our implementation,
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORTIE_WITH_ID: {
                String _ID = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{_ID};
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        FavoriteContract.FavoriteEntry._ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_FAVORITE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /**
     * Handles request to insert a new favorite row.
     * @param uri The URI of the insertion request. this must not be null.
     * @param values a set of column_name/value pairs tp add to the database. this must not be null.
     * @return the URI for the newly inserted item.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE:
                long _id = db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, values);

                // if _id is equal to -1 insertion failed
                if (_id != -1) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return FavoriteContract.FavoriteEntry.buildFavoriteUriWithId(_id);
            default:
                return null;
        }
    }

    /**
     * this method will be used to delete movies in favorite table.
     * @param uri The URI to query.
     * @param selection must be the row of tmdb's id.
     * @param selectionArgs the tmdb's id.
     * @return the numbers of rows affected.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            // if uri with id is'not supported, we use the tmdb_id to select a row to delete.
            case CODE_FAVORITE:
                return db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
