package com.example.hoangvancook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePw extends AppCompatActivity {
    private static final String TAG = "ChangePw";
    EditText edt_newpw, edt_repw;
    Button btn_cancel, btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_pw);

        edt_newpw = findViewById(R.id.edt_newpw);
        edt_repw = findViewById(R.id.edt_repw);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_confirm = findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edt_newpw.getText().toString().trim();
                String rePassword = edt_repw.getText().toString().trim();
                if (newPassword.equals(rePassword)) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User password updated.");
                                            Intent intent = new Intent(ChangePw.this, Profile.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Log.e(TAG, "Error updating password: " + task.getException().getMessage());
                                            Toast.makeText(ChangePw.this, "Error updating password. Please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Log.e(TAG, "User is null");
                        Toast.makeText(ChangePw.this, "User is null. Please sign in again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePw.this, "Password does not match! Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
