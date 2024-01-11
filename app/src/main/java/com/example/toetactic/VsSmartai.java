package com.example.toetactic;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toetactic.R;

import java.util.ArrayList;
import java.util.List;

public class VsSmartai extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vssmartai);

        textViewPlayer1 = findViewById(R.id.txtview_player1);
        textViewPlayer2 = findViewById(R.id.txtview_ai);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "tile_" + i + j;
                int resourceId = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j]=findViewById(resourceId);
                View.OnClickListener onClickListener =this;
                buttons[i][j].setOnClickListener(onClickListener);
            }
        }

        Button buttonReset = findViewById(R.id.btn_restart);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
            if (!player1Turn) {
                int[] computerMove = getComputerMove();
                if (computerMove != null) {
                    String buttonID = "tile_" + computerMove[0] + computerMove[1];
                    int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    Button button = findViewById(resourceID);
                    button.performClick();
                }
            }
        }
    }

    private int[] getComputerMove() {
        int[][] board = getBoardState();
        int[] move = minimax(board, 0, 2);
        if (move == null) {
            return null;
        }
        return new int[]{move[1], move[2]};
    }

    private int[][] getBoardState() {
        int[][] board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String text = buttons[i][j].getText().toString();
                if (text.equals("X")) {
                    board[i][j] = 1;
                } else if (text.equals("O")) {
                    board[i][j] = 2;
                }
            }
        }
        return board;
    }

    private int[] minimax(int[][] board, int depth, int player) {
        if (checkWin(board, 1)) {
            return new int[]{-10 + depth, -1, -1};
        } else if (checkWin(board, 2)) {
            return new int[]{10 - depth, -1, -1};
        }
        List<int[]> availableSpots = getAvailableSpots(board);
        if (availableSpots.isEmpty()) {
            return new int[]{0, -1, -1};
        }

        int[] bestMove = new int[3];
        int bestValue;
        if (player == 2) {
            bestValue = Integer.MIN_VALUE;
            for (int[] spot : availableSpots) {
                board[spot[0]][spot[1]] = 2;
                int[] currentMove = minimax(board, depth + 1, 1);
                if (currentMove[0] > bestValue) {
                    bestValue = currentMove[0];
                    bestMove = new int[]{bestValue, spot[0], spot[1]};
                }
                board[spot[0]][spot[1]] = 0;
            }
        } else {
            bestValue = Integer.MAX_VALUE;
            for (int[] spot : availableSpots) {
                board[spot[0]][spot[1]] = 1;
                int[] currentMove = minimax(board, depth + 1, 2);
                if (currentMove[0] < bestValue) {
                    bestValue = currentMove[0];
                    bestMove = new int[]{bestValue, spot[0], spot[1]};
                }
                board[spot[0]][spot[1]] = 0;
            }
        }
        return bestMove;
    }

    private List<int[]> getAvailableSpots(int[][] board) {
        List<int[]> spots = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    spots.add(new int[]{i, j});
                }
            }
        }
        return spots;
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private boolean checkWin(int[][] board, int player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }

        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }

        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }
    private void player1Wins() {
        textViewPlayer1.setText("Player 1 Wins!");
        textViewPlayer2.setText("");
        disableButtons();
    }

    private void player2Wins() {
        textViewPlayer2.setText("Player 2 Wins!");
        textViewPlayer1.setText("");
        disableButtons();
    }

    private void draw() {
        textViewPlayer1.setText("Draw!");
        textViewPlayer2.setText("Draw!");
        disableButtons();
    }
    private void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }
    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
        roundCount = 0;
        player1Turn = true;
        Toast.makeText(this, "New Game", Toast.LENGTH_SHORT).show();
    }

    private void updateScore() {
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText("Player 2: " + player2Points);
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updateScore();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}




