package com.example.captstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.captstone.fragments.ComposeFragment;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class LoginScreenActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    private EditText etUsername;
    private EditText etpPassword;
    private Button bLogin;
    private Button bRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }
        etUsername = findViewById(R.id.etUsername);
        etpPassword = findViewById(R.id.etpPassword);
        bLogin = findViewById(R.id.bLogin);
        bRegister = findViewById(R.id.bRegister);

        bLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Login button");
                String username = etUsername.getText().toString();
                String password = etpPassword.getText().toString();
                loginUser(username, password);
            }

            private void loginUser(String username, String password) {
                Log.i(TAG, "Attempting to login user " + username);
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Issue with login", e);
                            Toast.makeText(LoginScreenActivity.this, "Issue with login", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        goMainActivity();
                        Toast.makeText(LoginScreenActivity.this, "Successful Login", Toast.LENGTH_SHORT ).show();
                    }
                });
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Signup Button");
                String username = etUsername.getText().toString();
                String password = etpPassword.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(LoginScreenActivity.this, "Username can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()){
                    Toast.makeText(LoginScreenActivity.this, "Password can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    createUser(username, password);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Issue logging in", e);
                            Toast.makeText(LoginScreenActivity.this, "Issue logging in", Toast.LENGTH_SHORT);

                            return;
                        }
                        goMainActivity();
                        Toast.makeText(LoginScreenActivity.this, "Logged in", Toast.LENGTH_SHORT);
                    }
                });
            }
        });

    }

    private void createUser(String username, String password) throws ParseException {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUp();


        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error saving user", e);
                    Toast.makeText(LoginScreenActivity.this, "Error creating account", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Account Creation was successful!");
                etUsername.setText("");
                etpPassword.setText("");
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(LoginScreenActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}