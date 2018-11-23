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
import com.google.firebase.firestore.FirebaseFirestore;
import com.pherodev.datingapp.R;
import com.pherodev.datingapp.models.Person;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "Profile";

    public final static String PERSON_USER_KEY = "userPerson";
    public final static String PERSON_SELECTED_KEY = "selectedPerson";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private Person profilePerson;

    private TextView profileNameTextView;
    private TextView profileStatTextView;
    private Button profileChatButton;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        profileNameTextView = (TextView) findViewById(R.id.text_view_profile_name);
        profileStatTextView = (TextView) findViewById(R.id.text_view_profile_stat);
        profileChatButton = (Button) findViewById(R.id.button_profile_chat);
        signOutButton = (Button) findViewById(R.id.button_profile_logout);

        profileChatButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: Make the app launch to the conversations screen and redirect if no token.
        if (firebaseAuth == null) {
            String err = "Not logged in because Firebase is null";
            Log.e(TAG, err);
            Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
        } else {
            // TODO: Make custom UI edit fields for when you're viewing your own profile.
            // TODO: Create the ability to edit the profile

            populateUI();
        }
    }

    private void populateUI() {
        if (getIntent() != null || getIntent().getExtras() != null) {
            Bundle personBundle = getIntent().getBundleExtra(PERSON_SELECTED_KEY);
            profilePerson = personBundle.getParcelable(PERSON_SELECTED_KEY);
            profileNameTextView.setText(profilePerson.getName());
            profileStatTextView.setText(profilePerson.getEmail());
        } else {
            // TODO: Figure out error report submission.
            String error = "Intent (" + (getIntent() == null ? "null" : "non-null") + ")" +
                    "\t" +
                    "Extras (" + (getIntent() == null || getIntent().getExtras() == null ? "null" : "non-null") + ")";
            // TODO: Extend Exception for this?
            Log.e(TAG, error);
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_profile_logout:
                signOut();
                break;
            case R.id.button_profile_chat:
                // TODO: (Create a new chat, then) launch the chat.
                // TODO: (It. 3) You choose from a list of threads that you're both in.

                break;
        }
    }

    private void signOut() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent backToLoginIntent = new Intent(this, LoginActivity.class);
        startActivity(backToLoginIntent);
        finish();
    }
}
