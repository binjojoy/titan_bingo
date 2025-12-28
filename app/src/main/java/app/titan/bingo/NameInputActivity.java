package app.titan.bingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NameInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_input); // Ensure this XML exists!

        EditText etName = findViewById(R.id.etUsername);

        findViewById(R.id.btnContinue).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name!", Toast.LENGTH_SHORT).show();
                return;
            }

            // SAVE NAME TO STORAGE
            SharedPreferences prefs = getSharedPreferences("BingoPrefs", MODE_PRIVATE);
            prefs.edit().putString("username", name).apply();

            // GO TO HOME SCREEN
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }
}