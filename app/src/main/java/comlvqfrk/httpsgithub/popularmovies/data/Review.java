package comlvqfrk.httpsgithub.popularmovies.data;

public class Review {
    private String mReviewAuthor;
    private String mReviewContent;

    public Review(String author, String reviewContent) {
        mReviewAuthor = author;
        mReviewContent = reviewContent;
    }

    /**
     * get the author of the review.
     * @return mReviewAuthor, String containing the author name
     */
    public String getReviewAuthor() {
        return mReviewAuthor;
    }

    /**
     * get the text content of the review.
     * @return mReviewContent, String containing the review content
     */
    public String getReviewContent() {
        return mReviewContent;
    }
}
