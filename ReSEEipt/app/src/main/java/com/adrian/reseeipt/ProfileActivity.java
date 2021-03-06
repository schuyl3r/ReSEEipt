package com.adrian.reseeipt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adrian.reseeipt.Constants.SharedPrefConstants;
import com.adrian.reseeipt.Database.DatabaseHandler;
import com.adrian.reseeipt.Model.User;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton profileReturnButton;
    private TextView FirstNameField, LastNameField;
    private LinearLayout edit_linearlayout, delete_linearlayout;

    SharedPreferences sharedPreferences;
    private DatabaseHandler databaseHandler;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileReturnButton = findViewById(R.id.ProfileReturnButton);
        FirstNameField = findViewById(R.id.FirstNameField);
        LastNameField = findViewById(R.id.LastNameField);
        edit_linearlayout = findViewById(R.id.edit_linearlayout);
        delete_linearlayout = findViewById(R.id.delete_linearlayout);

        databaseHandler = new DatabaseHandler(this);
        sharedPreferences = getSharedPreferences(SharedPrefConstants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Get Stored User ID
        int user_id = sharedPreferences.getInt(SharedPrefConstants.PREF_USER_ID, 1);
        user = databaseHandler.getUser(user_id);

        FirstNameField.setText(user.getFirstName());
        LastNameField.setText(user.getLastName());

        profileReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(newIntent);
                finish();
            }
        });

        delete_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                LayoutInflater inflater;
                final AlertDialog dialog;
                builder = new AlertDialog.Builder(ProfileActivity.this);
                inflater = LayoutInflater.from(getApplicationContext());
                View view = inflater.inflate(R.layout.delete_account_confirm, null);

                Button noButton = view.findViewById(R.id.deleteConfNo);
                Button yesButton = view.findViewById(R.id.deleteConfYes);

                builder.setView(view);
                dialog = builder.create();
                dialog.show();

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseHandler.deleteUser(user);
                        sharedPreferences.edit().remove(SharedPrefConstants.PREF_USER_ID).commit();
                        Toast toast = Toast.makeText(getApplicationContext(),"Account Deleted",Toast. LENGTH_SHORT);
                        toast.show();
                        Intent newIntent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(newIntent);
                        finish();
                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
