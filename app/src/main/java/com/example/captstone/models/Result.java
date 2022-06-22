package com.example.captstone.models;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Result {
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
    public String getCoverUrl() {
        return "https://covers.openlibrary.org/b/olid/" + openLibraryId + "-L.jpg?default=false";
    }

    // Returns a Result given the expected JSON
    public static Result fromJson(JSONObject jsonObject) {
        Result result = new Result();
        try {
            // Deserialize json into object fields
            // Check if a cover edition is available
            if (jsonObject.has("cover_edition_key")) {
                result.openLibraryId = jsonObject.getString("cover_edition_key");
            } else if(jsonObject.has("edition_key")) {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                result.openLibraryId = ids.getString(0);
            }
            result.title = jsonObject.has("title_suggest") ? jsonObject.getString("title_suggest") : "";
            result.creator = getAuthor(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
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
    public static ArrayList<Result> fromJson(JSONArray jsonArray) {
        ArrayList<Result> results = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject resultJson;
            try {
                resultJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Result result = Result.fromJson(resultJson);
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }
}
