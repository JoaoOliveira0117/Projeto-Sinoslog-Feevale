package com.example.projetofeevale.activities;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class SislogActivity extends AppCompatActivity {

    public void startLoginActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
