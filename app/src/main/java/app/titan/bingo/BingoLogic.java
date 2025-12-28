package app.titan.bingo;

public class BingoLogic {
    public static int countCompletedLines(boolean[] selected) {
        int lines = 0;

        // Check Rows
        for (int i = 0; i < 5; i++) {
            if (selected[i*5] && selected[i*5+1] && selected[i*5+2] && selected[i*5+3] && selected[i*5+4]) lines++;
        }

        // Check Columns
        for (int i = 0; i < 5; i++) {
            if (selected[i] && selected[i+5] && selected[i+10] && selected[i+15] && selected[i+20]) lines++;
        }

        // Check Diagonals
        if (selected[0] && selected[6] && selected[12] && selected[18] && selected[24]) lines++;
        if (selected[4] && selected[8] && selected[12] && selected[16] && selected[20]) lines++;

        return lines;
    }
}