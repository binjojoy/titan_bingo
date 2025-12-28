package app.titan.bingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. CHECK FOR USERNAME
        SharedPreferences prefs = getSharedPreferences("BingoPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);

        // If no name, redirect to Input Screen immediately
        if (username == null || username.trim().isEmpty()) {
            startActivity(new Intent(this, NameInputActivity.class));
            finish(); // Close Home so user can't go back without a name
            return;   // Stop execution
        }

        // 2. SETUP UI
        setContentView(R.layout.activity_home);

        // Set Welcome Text
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("WELCOME BACK, " + username.toUpperCase() + "!");

        // 3. BUTTON LISTENERS
        findViewById(R.id.btnStart).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnQuit).setOnClickListener(v -> {
            finishAffinity();
            System.exit(0);
        });
    }
}