package com.example.captstone.cacheModels;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.captstone.models.Review;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity
public class ReviewCache {

    @NonNull
    @ColumnInfo
    @PrimaryKey
    public String reviewBody;
    @ColumnInfo
    public String user;
    @ColumnInfo
    public String createdAt;
    @ColumnInfo
    public String mediaType;
    @ColumnInfo
    public String mediaTitle;
    @ColumnInfo
    public String mediaCreator;

    public ReviewCache() {
        // default constructor
    }

    public ReviewCache(@NonNull String reviewBody, String user, String createdAt, String mediaType, String mediaTitle, String mediaCreator) {
        this.reviewBody = reviewBody;
        this.user = user;
        this.createdAt = createdAt;
        this.mediaType = mediaType;
        this.mediaTitle = mediaTitle;
        this.mediaCreator = mediaCreator;
    }

    public static List<Review> getReviewsList(List<ReviewCache> reviewCaches) {
        List<Review> reviews = new ArrayList<>();
        for (ReviewCache reviewCache : reviewCaches) {
            Review review = new Review();
            review.setReviewBody(reviewCache.reviewBody);
            review.setUser(ParseUser.getCurrentUser());
            review.setCreatedAt(reviewCache.createdAt);
            review.setMediaType(reviewCache.mediaType);
            review.setMediaTitle(reviewCache.mediaTitle);
            review.setMediaCreator(reviewCache.mediaCreator);
            reviews.add(review);
        }
        return reviews;
    }
}
