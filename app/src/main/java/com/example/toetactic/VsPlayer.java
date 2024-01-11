package com.example.toetactic;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toetactic.R;

public class VsPlayer extends AppCompatActivity implements View.OnClickListener {
    private Button [][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int player1Points;
    private int player2Points;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private int roundCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vsplayer);

        textViewPlayer1=findViewById(R.id.txtview_player1);
        textViewPlayer2=findViewById(R.id.txtview_player2);


        for(int i=0;i<3;i++){
            for (int j =0;j<3;j++){
                String buttonId ="tile_"+i+j;
                int resourceId=getResources().getIdentifier(buttonId,"id",getPackageName());
                buttons[i][j]=findViewById(resourceId);
                View.OnClickListener onClickListener =this;
                buttons[i][j].setOnClickListener(onClickListener);

            }
        }
        Button buttonrestart =findViewById(R.id.btn_restart);
        buttonrestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (!((Button)view).getText().toString().equals("")){
            return;
        }
        if (player1Turn) {
            ((Button) view).setText("X");
        } else{
            ((Button)view).setText("O");
        }
        roundCount++;

        if (CheckForWin()){
            if (player1Turn){
                player1Wins();
            }else{
                player2Wins();
            }
        }else if (roundCount==9){
            draw();

        }else {
            player1Turn = !player1Turn;
        }

    }
    private boolean CheckForWin() {
        String[][]field = new  String[3][3];
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                field[i][j]=buttons[i][j].getText().toString();
            }
        }

        for (int i=0;i<3;i++){
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")){
                return true;
            }
        }

        for (int i= 0;i<3;i++){
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")){
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")){
            return true;
        }
        if (field[0][2].equals(field[1][1])
                &&field[0][2].equals(field[2][0])
                && !field[0][2].equals("")){
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