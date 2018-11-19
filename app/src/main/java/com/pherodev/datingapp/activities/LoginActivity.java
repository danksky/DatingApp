package com.pherodev.datingapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pherodev.datingapp.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Login";

    private EditText loginEmailEditText;
    private EditText loginPasswordEditText;
    private TextView loginStatusTextView;
    private TextView loginDetailTextView;
    private Button loginEmailButton;
    private Button loginRegisterButton;
    private LoginButton loginFacebookButton;
    private Button loginFacebookLogoutButton;

    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> facebookCallback;

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        loginEmailEditText = (EditText) findViewById(R.id.edit_text_login_email);
        loginPasswordEditText = (EditText) findViewById(R.id.edit_text_login_password);
        loginStatusTextView = (TextView) findViewById(R.id.text_view_login_status);
        loginDetailTextView = (TextView) findViewById(R.id.text_view_login_details);
        loginEmailButton = (Button) findViewById(R.id.button_login_email);
        loginRegisterButton = (Button) findViewById(R.id.button_login_email_register);
        loginFacebookButton = (LoginButton) findViewById(R.id.buttonFacebookLogin);
        loginFacebookLogoutButton = (Button) findViewById(R.id.buttonFacebookSignout);

        loginEmailButton.setOnClickListener(this);
        loginRegisterButton.setOnClickListener(this);
        loginFacebookLogoutButton.setOnClickListener(this);

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
        loginFacebookButton.registerCallback(callbackManager, facebookCallback);
//        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        AccessToken fbAccessToken = AccessToken.getCurrentAccessToken();
        if (fbAccessToken != null && fbAccessToken.isExpired()) {
            Toast.makeText(LoginActivity.this, "fbAccessToken is expired.",
                    Toast.LENGTH_SHORT).show();
//            handleFacebookAccessToken(fbAccessToken);
        }
        else {
            // TODO: Skip this login shit if you're good to go. Check if access token is expired, etc.
            // You're not concerned with changing the UI of the login page once you get this working.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login_email:
                if (loginEmailButton.getText().toString().equals(getText(R.string.login_activity_sign_in)))
                    signIn(loginEmailEditText.getText().toString(), loginPasswordEditText.getText().toString());
                else
                    signOut();
                break;
            case R.id.button_login_email_register:
                createAccount(loginEmailEditText.getText().toString(), loginPasswordEditText.getText().toString());
                break;
            case R.id.buttonFacebookSignout:
                signOut();
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

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm())
            return;
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()) {
                            loginStatusTextView.setText(R.string.login_activity_status_auth_failed);
                        }
                    }
                });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm())
            return;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token.getToken());

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, task.getResult().toString(),
                                    Toast.LENGTH_LONG).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            signOut();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(LoginActivity.this, "Resuming.",
//                Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            loginStatusTextView.setText(getString(R.string.emailpassword_status_format,
                    user.getEmail(), user.isEmailVerified()));
            loginDetailTextView.setText(getString(R.string.firebase_status_format, user.getUid()));
            loginEmailButton.setText(getText(R.string.login_activity_sign_out));
            loginFacebookButton.setVisibility(View.INVISIBLE);
            loginFacebookLogoutButton.setVisibility(View.VISIBLE);
        } else {
            loginStatusTextView.setText(R.string.login_activity_status_signed_out);
            loginDetailTextView.setText(null);
            loginEmailButton.setText(getText(R.string.login_activity_sign_in));
            loginFacebookButton.setVisibility(View.VISIBLE);
            loginFacebookLogoutButton.setVisibility(View.INVISIBLE);
        }
    }

}
