package com.pherodev.datingapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pherodev.datingapp.R;
import com.pherodev.datingapp.adapters.SearchResultsAdapter;
import com.pherodev.datingapp.models.Person;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "Profile";

    public final static String PERSON_KEY = "person";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private TextView profileNameTextView;
    private TextView profileStatTextView;
    private Button signOutButton;

    // TODO: NEXT! Seed the database programmatically.

    // TODO: Update this to bring in Bundle information, not FirebaseUser information
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        profileNameTextView = (TextView) findViewById(R.id.text_view_profile_name);
        profileStatTextView = (TextView) findViewById(R.id.text_view_profile_stat);
        signOutButton = (Button) findViewById(R.id.button_profile_logout);

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
            // TODO: Populate the UI with Bundle fields rather than Firebase user fields.
            // TODO: Make custom UI edit fields for when you're viewing your own profile.
            // TODO: Create the ability to edit the profile

            populateUI();
        }
    }

    private void populateUI() {
        if (getIntent() != null || getIntent().getExtras() != null) {
            Bundle personBundle = getIntent().getBundleExtra(PERSON_KEY);
            Person person = personBundle.getParcelable(PERSON_KEY);
            profileNameTextView.setText(person.getName());
            profileStatTextView.setText(person.getEmail());
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
