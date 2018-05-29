package comlvqfrk.httpsgithub.popularmovies.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import comlvqfrk.httpsgithub.popularmovies.R;
import comlvqfrk.httpsgithub.popularmovies.activities.DetailsActivity;
import comlvqfrk.httpsgithub.popularmovies.utils.DbBitmapUtility;
import comlvqfrk.httpsgithub.popularmovies.utils.FavoriteContract;

public class FavoritesAdapter extends CursorAdapter {

    public FavoritesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context)
                .inflate(R.layout.favorite_item_view, parent, false);
    }

    @Override
    public void bindView(final View view, Context context, Cursor cursor) {
        ImageView favPoster = view.findViewById(R.id.iv_fav_poster);
        final TextView favTitle = view.findViewById(R.id.tv_fav_title);
        TextView favReleaseDate = view.findViewById(R.id.tv_fav_release_date);
        TextView favRate = view.findViewById(R.id.tv_fav_rate);

        int id = cursor.getInt(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TMDB_ID));
        String title = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE));
        String releaseDate = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE));
        String rate = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RATE));

        // get the image as byte array, then convert to bitmap.
        byte[] posterAsByte = cursor.getBlob(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER));
        Bitmap posterAsBitmap = DbBitmapUtility.getImage(posterAsByte);

        view.setTag(id);
        favPoster.setImageBitmap(posterAsBitmap);
        favTitle.setText(title);
        favReleaseDate.setText(releaseDate);
        favRate.setText(rate);
    }




}
