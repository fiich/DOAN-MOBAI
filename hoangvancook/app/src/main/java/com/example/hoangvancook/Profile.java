package com.example.hoangvancook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private CircleImageView ivProfile;
    private TextView tvName, tvEmail;
    private LinearLayout btnSignOut, btnAccount, btnEditProfile;
    private static final String USER = "users";
    BottomNavigationView bottomNavigationView;

    private final ActivityResultLauncher<Intent> profileDetailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Refresh profile data when returning from Profile_Detail
                    displayUserProfile();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_search) {
                    Intent searchintent = new Intent(Profile.this, SearchActivity.class);
                    startActivity(searchintent);
                    return true;
                } else if (itemId == R.id.action_home) {
                    // Check if the current activity is not HomeActivity before navigating
                        Intent profileintent = new Intent(Profile.this, HomeActivity.class);
                        startActivity(profileintent);
                        return true;
                } else if (itemId == R.id.action_bookmark) {
                    // Check if the current activity is not HomeActivity before navigating
                    Intent bookmarkintent = new Intent(Profile.this, BookmarkActivity.class);
                    startActivity(bookmarkintent);
                    return true;
                }
                return false;
            }
        });
        initializeViews();
        displayUserProfile();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure the profile data is always up-to-date
        displayUserProfile();
    }

    private void initializeViews() {
        ivProfile = findViewById(R.id.iv_profile);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        btnEditProfile= findViewById(R.id.btn_editProfile);
        btnSignOut = findViewById(R.id.btn_SignOut);
        btnAccount = findViewById(R.id.btn_Account);
    }

    private void displayUserProfile() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(USER).child(currentUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String photoUrl = dataSnapshot.child("photoUrl").getValue(String.class);

                    tvName.setText(name);
                    tvEmail.setText(email);

                    Picasso.get()
                            .load(photoUrl)
                            .placeholder(R.drawable.ic_account)
                            .into(ivProfile);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });
        }
    }

    private void setListeners() {
        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Profile.this, LoginActivity.class));
            finish();
        });

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, Profile_Detail.class);
            profileDetailLauncher.launch(intent);
        });

        btnAccount.setOnClickListener(v -> {
            startActivity(new Intent(Profile.this, Account_Detail.class));
        });
    }
}
