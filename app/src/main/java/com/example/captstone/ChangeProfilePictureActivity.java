package com.example.captstone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.captstone.fragments.ProfileFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ChangeProfilePictureActivity extends AppCompatActivity {
    private static final String TAG = "ChangeProfilePictureActivity";

    private Button bSelectImage;
    private Button bChangeProfilePic;
    private ImageView ivProfilePic;
    private ParseFile file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_picture);

        getSupportActionBar().setTitle("Change Profile Picture");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bSelectImage = findViewById(R.id.bSelectImage);
        bChangeProfilePic = findViewById(R.id.bChangeProfilePic);
        ivProfilePic = findViewById(R.id.ivProfilePic);

        bSelectImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Select Image button");
                imageChooser();
            }

            private void imageChooser() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        bChangeProfilePic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Change Profile Picture button");
                changeParseProfilePic(file);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    try {
                        // get the image from data
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        // set image to image view
                        ivProfilePic.setImageBitmap(bitmap);
                        // save the bitmap to a file
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        file = new ParseFile("profilePic.jpg", outputStream.toByteArray());
                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error saving: " + e.getMessage());
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void changeParseProfilePic(ParseFile file) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("profilePic", file);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error saving: " + e.getMessage());
                } else {
                    Toast.makeText(ChangeProfilePictureActivity.this, "Profile Picture Changed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangeProfilePictureActivity.this, ProfileFragment.class);
                    startActivity(intent);
                }
            }
        });
    }

    // Returns the File for a photo stored on disk given the fileName
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