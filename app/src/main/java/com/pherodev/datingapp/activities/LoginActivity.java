package com.pherodev.datingapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pherodev.datingapp.R;
import com.pherodev.datingapp.models.Person;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Login";
    // RC = request or result code
    private static final int RC_SIGN_IN = 9001;

    private EditText loginEmailEditText;
    private EditText loginPasswordEditText;
    private TextView loginStatusTextView;
    private TextView loginDetailTextView;
    private Button loginSignInButton;
    private Button loginRegisterButton;
    private LoginButton loginFacebookButton;
    private SignInButton loginGoogleButton;
    // Register UI
    private ConstraintLayout loginRegisterConstraintLayout;
    private EditText loginFirstNameEditText;
    private EditText loginLastNameEditText;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> facebookCallback;
    private GoogleSignInClient googleSignInClient;
    private OnCompleteListener<AuthResult> loginCompletionListener;

    private boolean debugRemainInLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        callbackManager = CallbackManager.Factory.create();

        loginEmailEditText = (EditText) findViewById(R.id.edit_text_login_email);
        loginPasswordEditText = (EditText) findViewById(R.id.edit_text_login_password);
        loginStatusTextView = (TextView) findViewById(R.id.text_view_login_status);
        loginDetailTextView = (TextView) findViewById(R.id.text_view_login_details);
        loginSignInButton = (Button) findViewById(R.id.button_login_email);
        loginRegisterButton = (Button) findViewById(R.id.button_login_email_register);
        loginFacebookButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginGoogleButton = (SignInButton) findViewById(R.id.button_google_login);
        // Register UI
        loginRegisterConstraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout_login_register_fields);
        loginFirstNameEditText = (EditText) findViewById(R.id.edit_text_login_first_name);
        loginLastNameEditText = (EditText) findViewById(R.id.edit_text_login_last_name);

        loginSignInButton.setOnClickListener(this);
        loginRegisterButton.setOnClickListener(this);
        loginGoogleButton.setOnClickListener(this);

        loginFacebookButton.setReadPermissions("email", "public_profile");
        facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult + " - " + loginResult.getAccessToken().getToken());
                Log.i(TAG, "facebook:onSuccess:" + loginResult);
                Log.i(TAG, "Access token details: " + loginResult.getAccessToken().toString());
                Log.i(TAG,"Token: " + AccessToken.getCurrentAccessToken().getToken());
                Log.i(TAG,"Acc ID: " + AccessToken.getCurrentAccessToken().getUserId());
                Log.i(TAG,"App ID: " + AccessToken.getCurrentAccessToken().getApplicationId());
                Log.i(TAG,"Expires: " + AccessToken.getCurrentAccessToken().getExpires());
                Log.i(TAG,"Last Refresh: " + AccessToken.getCurrentAccessToken().getLastRefresh());
                Log.i(TAG,"Permissions: " + AccessToken.getCurrentAccessToken().getPermissions());
                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                handleFacebookAccessToken(AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                updateUI(null);
            }
        };
        loginCompletionListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    Log.d(TAG, "signIn:success");
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    // a new and unregistered user
                    if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                        // Check that the displayName is empty
                        if (TextUtils.isEmpty(task.getResult().getUser().getDisplayName())) {
                            final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(loginFirstNameEditText.getText() +
                                            " " + loginLastNameEditText.getText())
                                    .build();
                            // Update the displayName, write to RTDB, and proceed to next activity
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        // I know, it's an OnCompleteListener in an OnCompleteListener.
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "updateProfile:success (displayName='" +
                                                        profileUpdates.getDisplayName() + "')");
                                                if (user != firebaseAuth.getCurrentUser())
                                                    Log.e(TAG, "updateProfile:(error) WRONG PROFILE?");
                                                else {
                                                    // Now it's safe to write using the display name.
                                                    writeNewUser(user.getUid(), user.getDisplayName(), user.getEmail());
                                                    // Next
                                                    if (debugRemainInLogin) updateUI(user);
                                                    else proceedToNextActivity();

                                                }
                                            }
                                        }
                                    });
                        } else {
                            writeNewUser(user.getUid(), user.getDisplayName(), user.getEmail());
                            // Next
                            if (debugRemainInLogin) updateUI(user);
                            else proceedToNextActivity();

                        }
                    } else {
                        // Next
                        if (debugRemainInLogin) updateUI(user);
                        else proceedToNextActivity();
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signIn:failure", task.getException());
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                if (!task.isSuccessful()) {
                    loginStatusTextView.setText(R.string.login_activity_status_auth_failed);
                }
            }
        };
        loginFacebookButton.registerCallback(callbackManager, facebookCallback);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gsio = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_google_oauth_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gsio);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        AccessToken fbAccessToken = AccessToken.getCurrentAccessToken();
        if (fbAccessToken != null && fbAccessToken.isExpired())
            Toast.makeText(LoginActivity.this, "fbAccessToken is expired.", Toast.LENGTH_SHORT).show();
        else {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null && !debugRemainInLogin) {
                proceedToNextActivity();
                return;
            } else
                updateUI(currentUser);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login_email:
                if (loginSignInButton.getText().toString().equals(getText(R.string.login_activity_sign_in_label)))
                    emailSignIn(loginEmailEditText.getText().toString(), loginPasswordEditText.getText().toString());
                else
                    signOut();
                break;
            case R.id.button_login_email_register:
                if (loginSignInButton.getVisibility() == View.VISIBLE) {
                    // Prompt the user with more registration fields
                    loginSignInButton.setVisibility(View.GONE);
                    loginRegisterConstraintLayout.setVisibility(View.VISIBLE);
                } else {
                    // Attempt to submit all the registration fields.
                    createAccountManually(loginEmailEditText.getText().toString(),
                            loginPasswordEditText.getText().toString(),
                            loginFirstNameEditText.getText().toString(),
                            loginLastNameEditText.getText().toString());
                }
                break;
            case R.id.button_google_login:
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;

        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = loginEmailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            loginEmailEditText.setError("Required.");
            valid = false;
        } else {
            loginEmailEditText.setError(null);
        }

        String password = loginPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            loginPasswordEditText.setError("Required.");
            valid = false;
        } else {
            loginPasswordEditText.setError(null);
        }

        return valid;
    }
    private boolean validateForm(String email, String password, String first, String last) {
        boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            loginEmailEditText.setError("Required.");
            valid = false;
        } else {
            loginEmailEditText.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            loginPasswordEditText.setError("Required.");
            valid = false;
        } else {
            loginPasswordEditText.setError(null);
        }

        if (TextUtils.isEmpty(first)) {
            loginFirstNameEditText.setError("Required.");
            valid = false;
        } else {
            loginFirstNameEditText.setError(null);
        }

        if (TextUtils.isEmpty(last)) {
            loginLastNameEditText.setError("Required.");
            valid = false;
        } else {
            loginLastNameEditText.setError(null);
        }

        return valid;
    }

    private void emailSignIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm())
            return;

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, loginCompletionListener);
    }

    private void createAccountManually(final String email, String password, final String first, final String last) {
        // Authenticate a new user and add basic information to real time database
        Log.d(TAG, "createAccountManually: (" + email + ")");
        if (!validateForm(email, password, first, last))
            return;
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, loginCompletionListener);
    }

    private void writeNewUser(String userId, String name, String email) {
        Person p = new Person(userId, name, email);
        firebaseFirestore.collection("users").document(userId).set(p)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "writeNewUser:success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "writeNewUser:" + e.getMessage());
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, loginCompletionListener);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this,loginCompletionListener);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    public void signOut() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            loginStatusTextView.setText(getString(R.string.emailpassword_status_format,
                    user.getEmail(), user.isEmailVerified()));
            loginDetailTextView.setText(getString(R.string.firebase_status_format, user.getUid()));
            loginSignInButton.setText(getText(R.string.login_activity_sign_out_label));
            loginFacebookButton.setVisibility(View.INVISIBLE);
        } else {
            loginStatusTextView.setText(R.string.login_activity_status_signed_out);
            loginDetailTextView.setText(null);
            loginSignInButton.setText(getText(R.string.login_activity_sign_in_label));
            loginFacebookButton.setVisibility(View.VISIBLE);
        }
    }

    private void proceedToNextActivity() {
        // TODO: Make this startConversationsActivity
        Intent startProfileActivityIntent = new Intent(this, ProfileActivity.class);
        Intent startSearchActivityIntent = new Intent(this, SearchActivity.class);
        startActivity(startSearchActivityIntent);
        finish();
    }

}
