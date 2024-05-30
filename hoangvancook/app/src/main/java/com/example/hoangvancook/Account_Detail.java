package com.example.hoangvancook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Account_Detail extends AppCompatActivity {
    private final String TAG = "Account_Detail";
    private LinearLayout btn_ChangePasswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_detail);

        btn_ChangePasswd = findViewById(R.id.btn_ChangePasswd);

        btn_ChangePasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account_Detail.this, ChangePw.class));
            }
        });
    }
}