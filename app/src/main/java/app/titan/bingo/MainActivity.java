package app.titan.bingo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BingoAdapter adapter;
    private TextView statusText, linesText, playerStatusText;
    private Button btnReset;
    private DatabaseReference roomRef;

    // "myName" tracks your ROLE (Player 1 / Player 2) for game logic
    private String myName = null;
    // "myUsername" tracks your DISPLAY NAME (e.g., "Justin")
    private String myUsername = "Unknown";

    private String currentTurn = null;
    private boolean isGameOver = false;

    private final String DB_URL = "https://titan-bingo-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. GET USERNAME FROM STORAGE
        SharedPreferences prefs = getSharedPreferences("BingoPrefs", MODE_PRIVATE);
        myUsername = prefs.getString("username", "Player");

        // 2. Initialize Views
        statusText = findViewById(R.id.status_text);
        linesText = findViewById(R.id.lines_completed_text);
        playerStatusText = findViewById(R.id.player_status_text);
        btnReset = findViewById(R.id.btn_reset);
        recyclerView = findViewById(R.id.recyclerView);

        // 3. Setup Grid
        List<Integer> numbers = generateFreshBoard();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        adapter = new BingoAdapter(numbers, (number, position) -> {
            if (myName != null && myName.equals(currentTurn) && !isGameOver) {
                sendMoveToFirebase(number);
            }
        });
        recyclerView.setAdapter(adapter);

        // 4. Firebase Setup
        FirebaseDatabase db = FirebaseDatabase.getInstance(DB_URL);
        roomRef = db.getReference("rooms").child("game_1");

        btnReset.setOnClickListener(v -> resetServer());

        determineRoleAndStart();
    }

    private void determineRoleAndStart() {
        roomRef.child("players").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                // If P1 slot is empty, take it and write OUR USERNAME
                if (!mutableData.hasChild("p1")) {
                    mutableData.child("p1").setValue(myUsername);
                    myName = "Player 1"; // Logic ID remains "Player 1"
                }
                // If P2 slot is empty, take it and write OUR USERNAME
                else if (!mutableData.hasChild("p2")) {
                    mutableData.child("p2").setValue(myUsername);
                    myName = "Player 2"; // Logic ID remains "Player 2"
                } else {
                    return Transaction.abort();
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                if (committed) {
                    // AUTO-CLEANUP: If app closes, delete our name from the slot
                    String slot = myName.equals("Player 1") ? "p1" : "p2";
                    roomRef.child("players").child(slot).onDisconnect().removeValue();

                    if (myName.equals("Player 1")) {
                        // Player 1 resets the game state
                        roomRef.child("clickedNumbers").removeValue();
                        roomRef.child("winner").removeValue();
                        String starter = (Math.random() < 0.5) ? "Player 1" : "Player 2";
                        roomRef.child("turn").setValue(starter);
                    }
                    setupGameSync();
                } else {
                    statusText.setText("Room Full!");
                }
            }
        });
    }

    private void setupGameSync() {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 1. Get Player Names from Firebase
                String p1Name = snapshot.child("players").child("p1").getValue(String.class);
                String p2Name = snapshot.child("players").child("p2").getValue(String.class);

                // Default text if null
                if (p1Name == null) p1Name = "Waiting...";
                if (p2Name == null) p2Name = "Waiting...";

                // Update the Retro UI at the bottom
                playerStatusText.setText(String.format("%s vs %s", p1Name, p2Name));

                // 2. Handle Game Turn Logic (Logic still uses "Player 1" / "Player 2")
                if (!snapshot.hasChild("turn")) {
                    recyclerView.setEnabled(false);
                    recyclerView.setAlpha(0.5f); // Dim board if waiting
                    statusText.setText("Waiting for opponent...");
                    return;
                }

                recyclerView.setEnabled(true);
                recyclerView.setAlpha(1.0f);

                currentTurn = snapshot.child("turn").getValue(String.class);

                // Show nice turn message using names if possible
                if (currentTurn != null) {
                    if (currentTurn.equals(myName)) {
                        statusText.setText("YOUR TURN!");
                    } else {
                        // Figure out opponent's name for the status text
                        String opponentName = myName.equals("Player 1") ? p2Name : p1Name;
                        statusText.setText(opponentName + "'s Turn");
                    }
                }

                // 3. Sync Moves
                DataSnapshot clickedSnapshot = snapshot.child("clickedNumbers");
                if (clickedSnapshot.exists()) {
                    for (DataSnapshot child : clickedSnapshot.getChildren()) {
                        Integer val = child.getValue(Integer.class);
                        if (val != null) adapter.markNumber(val);
                    }
                }

                // 4. Winner Check
                if (snapshot.hasChild("winner") && !isGameOver) {
                    String winnerRole = snapshot.child("winner").getValue(String.class);
                    // Map the winner ID (Player 1) to the Name (Justin)
                    String winnerName = "Unknown";
                    if ("Player 1".equals(winnerRole)) winnerName = p1Name;
                    if ("Player 2".equals(winnerRole)) winnerName = p2Name;

                    showWinDialog(winnerName);
                } else if (!isGameOver) {
                    checkWin();
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void resetServer() {
        roomRef.removeValue().addOnCompleteListener(task -> {
            Toast.makeText(this, "Server Reset!", Toast.LENGTH_SHORT).show();
            // In a real app, you might want to kick players back to Home
            // For now, we just recreate the activity
            recreate();
        });
    }

    private void sendMoveToFirebase(int number) {
        String nextTurn = myName.equals("Player 1") ? "Player 2" : "Player 1";
        roomRef.child("clickedNumbers").child(String.valueOf(number)).setValue(number);
        roomRef.child("turn").setValue(nextTurn);
    }

    private List<Integer> generateFreshBoard() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 25; i++) list.add(i);
        Collections.shuffle(list);
        return list;
    }

    private void checkWin() {
        int lines = BingoLogic.countCompletedLines(adapter.getSelectedItems());
        linesText.setText("Lines: " + lines + "/5");
        if (lines >= 5) roomRef.child("winner").setValue(myName); // Logic sends "Player 1"
    }

    private void showWinDialog(String winnerName) {
        isGameOver = true;
        new MaterialAlertDialogBuilder(this)
                .setTitle("BINGO!")
                .setMessage(winnerName + " Wins!")
                .setPositiveButton("Main Menu", (d, w) -> {
                    // Go back to Home Screen
                    finish();
                })
                .setCancelable(false).show();
    }
}