package comlvqfrk.httpsgithub.popularmovies.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 1;

    public FavoriteDbHelper (Context context) {
        super(context, DATABASE_NAME,null ,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE =

                "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                        FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteContract.FavoriteEntry.COLUMN_TMDB_ID + " INTEGER NOT NULL, " +
                        FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        FavoriteContract.FavoriteEntry.COLUMN_POSTER + " BLOB, " +
                        FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                        FavoriteContract.FavoriteEntry.COLUMN_RATE + " REAL, " +
                        FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT);";

        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
