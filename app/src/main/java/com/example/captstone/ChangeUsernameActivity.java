package com.example.captstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ChangeUsernameActivity extends AppCompatActivity {
    private static final String TAG = "ChangeUsernameActivity";

    private EditText etUsername;
    private Button bChangeUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        getSupportActionBar().setTitle("Change Username");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etUsername = findViewById(R.id.etUsername);
        bChangeUsername = findViewById(R.id.bChangeUsername);

        bChangeUsername.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Change Username button");
                String username = etUsername.getText().toString();
                changeUsername(username);
            }

            private void changeUsername(String username) {
                Log.i(TAG, "Attempting to change username to " + username);
                // check if there is a user already named username
                ParseUser.getQuery().whereEqualTo("username", username).getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        if (object != null){
                            Log.e(TAG, "Username already exists");
                            Toast.makeText(ChangeUsernameActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            ParseUser.getCurrentUser().setUsername(username);
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null){
                                        Log.e(TAG, "Issue with saving username", e);
                                    } else {
                                        Log.i(TAG, "Username changed successfully");
                                        Toast.makeText(ChangeUsernameActivity.this, "Username changed successfully", Toast.LENGTH_SHORT).show();
                                        goMainActivity();
                                    }
                                }
                            });
                        }
                    }
                });
            }

            private void goMainActivity() {
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }

            private Context getContext() {
                return ChangeUsernameActivity.this;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}