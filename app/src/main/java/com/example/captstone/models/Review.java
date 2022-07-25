package com.example.captstone.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Review")
public class Review extends ParseObject {
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_MEDIA_TYPE = "mediaType";
    public static final String KEY_MEDIA_TITLE = "mediaTitle";
    public static final String KEY_MEDIA_CREATOR = "mediaCreator";
    public static final String KEY_REVIEW_BODY = "reviewBody";
    private static final String KEY_REVIEW_RATING = "reviewRating";

    public Review(String reviewBody, String user, String createdAt, String mediaType, String mediaTitle, String mediaCreator, float reviewRating) {
        put(KEY_REVIEW_BODY, reviewBody);
        put(KEY_USER, user);
        put(KEY_CREATED_AT, createdAt);
        put(KEY_MEDIA_TYPE, mediaType);
        put(KEY_MEDIA_TITLE, mediaTitle);
        put(KEY_MEDIA_CREATOR, mediaCreator);
        put(KEY_REVIEW_RATING, reviewRating);
    }

    public Review() {
        // default constructor
    }

    public String getReviewBody(){
        return getString(KEY_REVIEW_BODY);
    }

    public void setReviewBody(String body){
        put(KEY_REVIEW_BODY, body);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public String getMediaType(){
        return getString(KEY_MEDIA_TYPE);
    }

    public void setMediaType(String mediaType){
        put(KEY_MEDIA_TYPE, mediaType);
    }

    public String getMediaTitle(){
        return getString(KEY_MEDIA_TITLE);
    }

    public void setMediaTitle(String mediaTitle){
        put(KEY_MEDIA_TITLE, mediaTitle);
    }

    public String getMediaCreator(){
        return getString(KEY_MEDIA_CREATOR);
    }

    public void setMediaCreator(String mediaCreator){
        put(KEY_MEDIA_CREATOR, mediaCreator);
    }

    public void setCreatedAt(String createdAt) {
        put(KEY_CREATED_AT, createdAt);
    }

    public float getReviewRating() {
        return getNumber(KEY_REVIEW_RATING).floatValue();
    }
    public void setReviewRating(float reviewRating) {
        put(KEY_REVIEW_RATING, reviewRating);
    }
}