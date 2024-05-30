package com.example.hoangvancook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hoangvancook.Models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN_GG = 9001;

    private EditText edt_LogInEmail, edt_LogInPassword;
    private Button btn_LogIn, btn_LogInGG, btn_LogInFB;
    private TextView tv_SignUp;

    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        initializeUI();
        initializeFirebase();
        initializeFacebookLogin();
        initializeGoogleSignIn();

        setListeners();
    }


    private void initializeUI() {
        edt_LogInEmail = findViewById(R.id.edt_LogInEmail);
        edt_LogInPassword = findViewById(R.id.edt_LogInPassword);
        btn_LogIn = findViewById(R.id.btn_LogIn);
        btn_LogInFB = findViewById(R.id.btn_LogInFB);
        btn_LogInGG = findViewById(R.id.btn_LogInGG);
        tv_SignUp = findViewById(R.id.tv_SignUp);
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void initializeFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(this);


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // No action needed
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "Facebook login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeGoogleSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);
    }

    private void setListeners() {
        btn_LogIn.setOnClickListener(v -> onClickLogIn());
        btn_LogInGG.setOnClickListener(v -> onClickLogInGG());
        btn_LogInFB.setOnClickListener(v -> onClickLogInFB());
        tv_SignUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithCredential:success");
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.getException());
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    private void onClickLogInFB() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
    }

    private void onClickLogInGG() {
        Intent signInIntentGG = gsc.getSignInIntent();
        startActivityForResult(signInIntentGG, RC_SIGN_IN_GG);
    }

    private void onClickLogIn() {
        String strEmail = edt_LogInEmail.getText().toString().trim();
        String strPassword = edt_LogInPassword.getText().toString().trim();
        if (!strEmail.isEmpty() && !strPassword.isEmpty()) {
            mAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        updateUserProfile(user);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            setEmptyFieldErrors();
        }
    }

    private void updateUserProfile(FirebaseUser user) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getUid())
                .setPhotoUri(Uri.parse("android.resource://com.example.hoangvancook/drawable/ic_account"))
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "User profile updated.");
                updateUI(user);
            } else {
                Log.w(TAG, "Failed to update user profile.", task.getException());
            }
        });
    }

    private void setEmptyFieldErrors() {
        int redColor = getResources().getColor(android.R.color.holo_red_light);
        edt_LogInEmail.setHint("Email cannot be blank!");
        edt_LogInEmail.setHintTextColor(redColor);
        edt_LogInPassword.setHint("Password cannot be blank!");
        edt_LogInPassword.setHintTextColor(redColor);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            } else {
                Toast.makeText(LoginActivity.this, "Google authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN_GG) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String uid = user.getUid();
            String name = user.getDisplayName();
            String email = user.getEmail();
            String photoUrl = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : "android.resource://com.example.hoangvancook/drawable/default_profile_icon";

            database = FirebaseDatabase.getInstance();
            reference = database.getReference("users");
            User user1 = new User(uid, name, email, photoUrl);
            reference.child(uid).setValue(user1);

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
