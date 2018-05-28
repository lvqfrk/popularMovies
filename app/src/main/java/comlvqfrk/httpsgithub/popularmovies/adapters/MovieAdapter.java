package comlvqfrk.httpsgithub.popularmovies.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.R;
import comlvqfrk.httpsgithub.popularmovies.data.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private Context mContext;
    private List<Movie> mMovies;
    private MovieAdapterOnClickHandler mClickHandler;

    /** base Url for getting poster image from TMDB, width 200 */
    private static final String TMDB_POSTER_W185_BASE_URL = "http://image.tmdb.org/t/p/w342//";

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;

    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie currentMovie);
    }

    @NonNull
    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.grid_item_view;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieAdapterViewHolder holder, int position) {
        String title = mMovies.get(position).getTitle();
        holder.tvTitle.setText(title);
        holder.tvTitle.setTag(position);

        // show tvTitle depending of user's preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (prefs.getBoolean("show_titles", true)){
            holder.tvTitle.setVisibility(View.VISIBLE);
        } else {
            holder.tvTitle.setVisibility(View.INVISIBLE);
        }

        String posterUrl = TMDB_POSTER_W185_BASE_URL + mMovies.get(position).getPosterUrl();
        Picasso.with(mContext).load(posterUrl).into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

    public void swapMovies(List<Movie> newMovies){
        mMovies = newMovies;
        notifyDataSetChanged();
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tvTitle;
        final ImageView ivPoster;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_grid_title);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_grid_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Movie currentMovie = mMovies.get(pos);
            mClickHandler.onClick(currentMovie);
        }
    }
}
