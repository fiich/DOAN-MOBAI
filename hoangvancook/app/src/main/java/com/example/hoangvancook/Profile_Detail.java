package com.example.hoangvancook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Detail extends AppCompatActivity {
    private EditText edt_profile_name, edt_profile_email;
    private Button btn_save, btn_changePhoto, btn_cancel;
    private CircleImageView iv_profile_avt;
    private static final String TAG = "Profile_Detail";
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        edt_profile_email = findViewById(R.id.edt_profile_email);
        edt_profile_name = findViewById(R.id.edt_profile_name);
        iv_profile_avt = findViewById(R.id.iv_profile_avt);
        btn_save = findViewById(R.id.btn_save);
        btn_changePhoto = findViewById(R.id.btn_changePhoto);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        btn_changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(Intent.ACTION_PICK);
                myintent.setType("image/*");
                startActivityForResult(myintent, 1);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setUserInformation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            getImage();
        }
    }

    private void getImage() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        iv_profile_avt.setImageBitmap(bitmap);
    }

    public void updateProfile() {
        if (selectedImageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString());
            storageRef.putFile(selectedImageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri downloadUri = task.getResult();
                                            updateFirebaseUserProfile(downloadUri);
                                        } else {
                                            Toast.makeText(Profile_Detail.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Profile_Detail.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            updateFirebaseUserProfile(null);
        }
    }

    private void updateFirebaseUserProfile(@Nullable Uri photoUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String new_name = edt_profile_name.getText().toString().trim();
            UserProfileChangeRequest.Builder requestBuilder = new UserProfileChangeRequest.Builder()
                    .setDisplayName(new_name);
            if (photoUri != null) {
                requestBuilder.setPhotoUri(photoUri);
            }
            UserProfileChangeRequest profileUpdates = requestBuilder.build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                                Toast.makeText(Profile_Detail.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                updateUserDatabase(new_name, photoUri != null ? photoUri.toString() : null);
                            } else {
                                Toast.makeText(Profile_Detail.this, "Profile update failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void updateUserDatabase(String name, @Nullable String photoUrl) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            userRef.child("name").setValue(name);
            if (photoUrl != null) {
                userRef.child("photoUrl").setValue(photoUrl);
            }
            Toast.makeText(Profile_Detail.this, "User data updated successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Set result to OK to notify the calling activity
            finish();
        }
    }

    public void setUserInformation() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String photoUrl = dataSnapshot.child("photoUrl").getValue(String.class);

                    if (name != null) {
                        edt_profile_name.setText(name);
                    }
                    if (email != null) {
                        edt_profile_email.setText(email);
                    }
                        Picasso.get()
                                .load(photoUrl)
                                .placeholder(R.drawable.ic_account)
                                .into(iv_profile_avt);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle possible errors.
                }
            });
        }
    }
}
