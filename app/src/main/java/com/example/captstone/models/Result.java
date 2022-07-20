package com.example.captstone.models;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Objects;

@Parcel
public final class Result {
    private static final String TAG = "ResultModel";

    private String openLibraryId;
    private String creator;
    private String title;
    private String mediaType;

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public String getMediaType() {
        return mediaType;
    }

    // Get result cover from covers API
    public String getBookCoverUrl() {
        return "https://covers.openlibrary.org/b/olid/" + openLibraryId + "-L.jpg?default=false";
    }

    // Returns a Result given the expected JSONx
    public static Result fromJson(JSONObject jsonObject, String mediaType) {
        Result result = new Result();

        if (Objects.equals(mediaType, "Book")) {
            result.mediaType = "Book";
            try {
                // Deserialize json into object fields
                // Check if a cover edition is available
                if (jsonObject.has("cover_edition_key")) {
                    result.openLibraryId = jsonObject.getString("cover_edition_key");
                } else if (jsonObject.has("edition_key")) {
                    final JSONArray ids = jsonObject.getJSONArray("edition_key");
                    result.openLibraryId = ids.getString(0);
                }
                result.title = jsonObject.has("title_suggest") ? jsonObject.getString("title_suggest") : "";
                result.creator = getAuthor(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        else if (Objects.equals(mediaType, "TrendingBook")) {
            result.mediaType = "Book";
            try {
                // Deserialize json into object fields
                result.title = jsonObject.has("title") ? convertToTitleCase(jsonObject.getString("title")) : "";
                result.creator = jsonObject.has("author") ? jsonObject.getString("author") : "";
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        else if (Objects.equals(mediaType, "Movie")) {
            result.mediaType = "Movie";
            try {
                // Deserialize json into object fields
                result.title = jsonObject.getString("title");
                result.creator = "Director Placeholder";
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        else if (Objects.equals(mediaType, "Song")) {
            result.mediaType = "Song";
            try {
                // Deserialize json into object fields
                result.title = jsonObject.getString("name");
                result.creator = jsonObject.getString("artist");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        else if (Objects.equals(mediaType, "Trending Song")) {
            result.mediaType = "Song";
            try {
                // Deserialize json into object fields
                result.title = jsonObject.getString("name");
                result.creator = jsonObject.getJSONObject("artist").getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        // Return new object
        return result;
    }

    // Return comma separated author list when there is more than one author
    private static String getAuthor(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray("author_name");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }

    // Decodes array of result json results into business model objects
    public static ArrayList<Result> fromJson(JSONArray jsonArray, String mediaType) {
        ArrayList<Result> results = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to business
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject resultJson;
            try {
                resultJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Result result = Result.fromJson(resultJson, mediaType);
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }
    public static String convertToTitleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }
}
