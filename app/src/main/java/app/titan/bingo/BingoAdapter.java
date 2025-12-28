package app.titan.bingo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BingoAdapter extends RecyclerView.Adapter<BingoAdapter.ViewHolder> {

    private List<Integer> numbers;
    private boolean[] selectedItems = new boolean[25];
    private OnCellClickListener listener;

    public interface OnCellClickListener {
        void onCellClick(int number, int position);
    }

    public BingoAdapter(List<Integer> numbers, OnCellClickListener listener) {
        this.numbers = numbers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the retro layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bingo_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int val = numbers.get(position);
        holder.cellText.setText(String.valueOf(val));

        // RETRO UI LOGIC: Swap Drawables instead of changing CardView colors
        if (selectedItems[position]) {
            // Selected = Yellow Pixel Button
            holder.cellText.setBackgroundResource(R.drawable.bg_cell_selected);
        } else {
            // Unselected = White Pixel Button
            holder.cellText.setBackgroundResource(R.drawable.bg_cell_unselected);
        }

        holder.itemView.setOnClickListener(v -> {
            if (!selectedItems[position]) {
                listener.onCellClick(val, position);
            }
        });
    }

    public void markNumber(int number) {
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) == number) {
                selectedItems[i] = true;
                notifyItemChanged(i);
                break;
            }
        }
    }

    public boolean[] getSelectedItems() { return selectedItems; }

    @Override
    public int getItemCount() { return 25; }

    // Updated ViewHolder to match the Retro XML
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cellText;

        // Removed MaterialCardView because we deleted it from the XML
        ViewHolder(View itemView) {
            super(itemView);
            cellText = itemView.findViewById(R.id.cell_text);
        }
    }
}