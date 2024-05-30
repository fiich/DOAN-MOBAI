package com.example.hoangvancook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends AppCompatActivity {
    EditText edt_SignUpEmail, edt_SignUpPassword;
    Button btn_SignUp;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edt_SignUpEmail = findViewById(R.id.edt_SignUpEmail);
        edt_SignUpPassword = findViewById(R.id.edt_SignUpPassword);
        btn_SignUp = findViewById(R.id.btn_SignUp);

        initListener();

    }

    private void initListener() {
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        String strEmail = edt_SignUpEmail.getText().toString().trim();
        String strPassword = edt_SignUpPassword.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent myintent = new Intent(SignUpActivity.this, HomeActivity.class);
                            startActivity(myintent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}