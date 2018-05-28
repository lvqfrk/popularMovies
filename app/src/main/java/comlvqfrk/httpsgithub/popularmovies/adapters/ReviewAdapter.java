package comlvqfrk.httpsgithub.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import comlvqfrk.httpsgithub.popularmovies.R;
import comlvqfrk.httpsgithub.popularmovies.data.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{

    private Context mContext;

    private List<Review> mReviews;

    public ReviewAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutId = R.layout.review_list_view;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewAdapterViewHolder holder, int position) {
        String author = mReviews.get(position).getReviewAuthor();
        String content = mReviews.get(position).getReviewContent();
        holder.tvAuthor.setText(author);
        holder.tvContent.setText(content);
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        return mReviews.size();
    }

    public void swapReviews (List<Review> newReviews) {
        mReviews = newReviews;
        notifyDataSetChanged();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView tvAuthor;
        final TextView tvContent;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            tvAuthor = (TextView) view.findViewById(R.id.tv_reviews_author);
            tvContent = (TextView) view.findViewById(R.id.tv_review_content);
        }
    }
}
