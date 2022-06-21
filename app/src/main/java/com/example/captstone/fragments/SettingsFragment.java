package com.example.captstone.fragments;

import android.content.Intent;
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

import com.example.captstone.LoginScreenActivity;
import com.example.captstone.R;
import com.example.captstone.models.Review;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";

    private Button bLogout;
    private Button bChangeUsername;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bLogout = view.findViewById(R.id.bLogout);
        bChangeUsername = view.findViewById(R.id.bChangeUsername);

        bLogout.setOnClickListener(v -> {
            Log.i(TAG, "onClick Logout button " + ParseUser.getCurrentUser());
            ParseUser.logOut();
            Intent i = new Intent(getContext(), LoginScreenActivity.class);
            startActivity(i);
        });

    }
}
