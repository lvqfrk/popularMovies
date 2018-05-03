package comlvqfrk.httpsgithub.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.data.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private final Context mContext;
    private List<Movie> mMovies;

    public MovieAdapter(Context context) {
        mContext = context;
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
    }



    @Override
    public int getItemCount() {
        return mMovies.size();
    }



    public void swapMovies(List<Movie> newMovies){
        mMovies = newMovies;
        notifyDataSetChanged();
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tvTitle;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_grid_title);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
