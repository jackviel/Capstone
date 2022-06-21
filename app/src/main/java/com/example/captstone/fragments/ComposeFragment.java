package com.example.captstone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.captstone.R;
import com.example.captstone.models.Review;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ComposeFragment extends Fragment {

    public static final String TAG = "ComposeFragment";

    private EditText etBody;
    private Button bSubmit;
    public ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etBody = view.findViewById(R.id.etBody);
        bSubmit = view.findViewById(R.id.bSubmit);
        pb = view.findViewById(R.id.pbLoading);

        bSubmit.setOnClickListener(v -> {
            Log.i(TAG, "onClick Submit button");
            String body = etBody.getText().toString();
            if (body.isEmpty()) {
                Toast.makeText(getContext(), "Description can't be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            ParseUser currentUser = ParseUser.getCurrentUser();
            savePost(body, currentUser);
        });

    }

    private void savePost(String description, ParseUser currentUser) {
        Review review = new Review();
        review.setBody(description);
        review.setUser(currentUser);
        pb.setVisibility(ProgressBar.VISIBLE);
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving.", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Review save was successful.");
                etBody.setText("");
                pb.setVisibility(ProgressBar.INVISIBLE);
        }
    });
}
}
