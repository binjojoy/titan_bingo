package app.titan.bingo;// CHANGE THIS TO MATCH YOUR PACKAGE

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class BingoActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private ArrayList<Integer> numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo); // Pointing to the NEW layout

        gridLayout = findViewById(R.id.gridLayout);

        // Setup Numbers
        numbers = new ArrayList<>();
        for (int i = 1; i <= 75; i++) numbers.add(i);
        Collections.shuffle(numbers);

        setupBoard();
    }

    private void setupBoard() {
        int totalCells = 25;
        for (int i = 0; i < totalCells; i++) {
            TextView cell = new TextView(this);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(4, 4, 4, 4);
            cell.setLayoutParams(params);

            cell.setBackgroundResource(R.drawable.bg_cell_unselected);
            cell.setGravity(Gravity.CENTER);
            cell.setTextColor(0xFF2C2C2C); // Dark gray
            cell.setTextSize(18);

            // Center Star
            if (i == 12) {
                cell.setText("★");
                cell.setBackgroundResource(R.drawable.bg_cell_selected);
                cell.setTag(true);
            } else {
                cell.setText(String.valueOf(numbers.get(i)));
                cell.setOnClickListener(view -> toggleCellSelection((TextView) view));
            }
            gridLayout.addView(cell);
        }
    }

    private void toggleCellSelection(TextView cell) {
        boolean isSelected = cell.getTag() != null && (boolean) cell.getTag();

        cell.animate().scaleX(0.9f).scaleY(0.9f).setDuration(50).withEndAction(() -> {
            cell.animate().scaleX(1f).scaleY(1f).setDuration(50).start();
            if (isSelected) {
                cell.setBackgroundResource(R.drawable.bg_cell_unselected);
                cell.setTag(false);
            } else {
                cell.setBackgroundResource(R.drawable.bg_cell_selected);
                cell.setTag(true);
            }
        }).start();
    }
}
