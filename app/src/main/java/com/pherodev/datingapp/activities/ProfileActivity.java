package com.pherodev.datingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.pherodev.datingapp.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "Profile";

    private FirebaseAuth firebaseAuth;

    private TextView profileNameTextView;
    private TextView profileStatTextView;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        profileNameTextView = (TextView) findViewById(R.id.text_view_profile_name);
        profileStatTextView = (TextView) findViewById(R.id.text_view_profile_stat);
        signOutButton = (Button) findViewById(R.id.button_profile_logout);

        signOutButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth == null) {
            String err = "Not logged in because Firebase is null";
            Log.e(TAG, err);
            Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
        } else {
            profileNameTextView.setText(firebaseAuth.getCurrentUser().getDisplayName());
            profileStatTextView.setText(firebaseAuth.getCurrentUser().getEmail());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_profile_logout:
                signOut();
                break;
        }
    }

    private void signOut() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent backToLoginIntent = new Intent(this, LoginActivity.class);
        startActivity(backToLoginIntent);
    }
}
