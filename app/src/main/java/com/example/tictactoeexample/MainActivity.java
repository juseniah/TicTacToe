// TicTacToe
//
// The following code is a derivative work of the code from Coding in Flow, provided
// freely for all at https://codinginflow.com/tutorials/android/tic-tac-toe/
//
// Primarily, I added the ability to play against the computer and made cosmetic changes.
//
// Icon made by Freepik from www.flaticon.com
//
// Jussi Doherty
// 07/02/2020
// COP 4656

package com.example.tictactoeexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Declare variables
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    Random random = new Random();
    int nextX;
    int nextY;
    boolean isOpen = false;
    boolean winnerFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        // Initialize buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (winnerFound)
            winnerFound = false;

        // Player1: If button isn't empty, do nothing
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        // Player1: Otherwise, place X in button
        ((Button) v).setText("X");
        System.out.println( ((Button) v).getText().toString());
        roundCount++;
        System.out.println("roundCount is " + roundCount);

        // Check for winner or draw
        if (checkForWin()) {
            player1Wins();
            winnerFound = true;
        } else if (roundCount == 9)
            draw();

        // Player 2's turn
        if (!winnerFound) {
            if (roundCount != 9) {
                if (checkTwoInRow()) {
                    System.out.println("checkTwoInRow  nextX: " + nextX + "   nextY: " + nextY);
                    player2Blocks();
                }
                else {
                    player2Plays();
                }
                roundCount++;

                if (checkForWin()) {
                    player2Wins();
                    winnerFound = true;
                }
            }
            else
                draw();
        } // end of Player 2's turn

    } // end of onClick function

    private boolean checkTwoInRow() {
        // Put all X's and O's into field array
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        // Checking rows
        for (int i = 0; i < 3; i++) {
            nextX = i;
            if (field[i][0].equals(field[i][1]) && !field[i][0].equals("")
                    && field[i][2].equals("")) {
                nextY = 2;
                System.out.println("check rows nextX: " + nextX + " nextY: " + nextY);
                return true;
            }
            if (field[i][0].equals(field[i][2]) && !field[i][0].equals("")
                    && field[i][1].equals("")) {
                nextY = 1;
                System.out.println("check rows nextX: " + nextX + " nextY: " + nextY);
                return true;
            }
            if (field[i][1].equals(field[i][2]) && !field[i][1].equals("")
                    && field[i][0].equals("")) {
                nextY = 0;
                System.out.println("check rows nextX: " + nextX + " nextY: " + nextY);
                return true;
            }
        }

        // Checks each column
        for (int i = 0; i < 3; i++) {
            nextY = i;
            if (field[0][i].equals(field[1][i]) && !field[0][i].equals("")
                    && field[2][i].equals("")) {
                nextX = 2;
                System.out.println("check col nextX: " + nextX + " nextY: " + nextY);
                return true;
            }
            if (field[0][i].equals(field[2][i]) && !field[0][i].equals("")
                    && field[1][i].equals("")) {
                nextX = 1;
                System.out.println("check col nextX: " + nextX + " nextY: " + nextY);
                return true;
            }
            if (field[1][i].equals(field[2][i]) && !field[1][i].equals("")
                    && field[0][i].equals("")) {
                nextX = 0;
                System.out.println("check col nextX: " + nextX + " nextY: " + nextY);
                return true;
            }
        }
        // Checks main diagonal
        for (int i = 0; i < 3-1; i++) {
            for (int j = i+1; j < 3; j++) {
                for (int k = 0; k<3; k++){
                    if ( k!=i && k!=j && (field[i][i].equals(field[j][j]) && !field[i][i].equals("")
                            && field[k][k].equals("")))
                    {
                        System.out.println("main diag i: "+ i + ", j: "+j +", k: " +k);
                        nextX = k;
                        nextY = k;
                        return true;
                    }
                }
            }
        }
        // Checks inverse diagonal
        for (int i = 0; i < 2; i++) {
            for (int j = i+1; j < 3; j++) {
                for (int k = 0; k<3; k++){
                    if ( k != i && k != j && (field[i][2-i].equals(field[j][2-j]) && !field[i][2-i].equals("")
                            && field[k][2-k].equals("")))
                    {
                        nextX = k;
                        nextY = 2-k;
                        System.out.println("anti diag i: " + i + ", j: " + j + ", k: " + k);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkForWin() {
        // Put all X's and O's into field array
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        // Checks each row
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }
        // Checks each column
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }
        // Checks diagonal from top left
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        // Checks diagonal from top right
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }
    private void player1Wins() {
        player1Points++;
        System.out.println("Player 1 wins!");
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { resetBoard(); }
        }, 2000);
    }
    private void player2Wins() {
        player2Points++;
        System.out.println("Player 2 wins!");
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { resetBoard(); }
        }, 2000);
    }
    private void draw() {
        System.out.println("Draw!");
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { resetBoard(); }
        }, 2000);
    }
    private void updatePointsText() {
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText("Player 2: " + player2Points);
    }
    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        player1Turn = true;
    }
    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { resetBoard(); }
        }, 2000);
    }

    // Preserves variables when orientation is changed
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    // Restores variables when orientation is changed
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }

    // Generates new values between 0 and 2
    private void getNewXY() {
        nextX = random.nextInt(3);
        nextY = random.nextInt(3);
    }

    // Checks if square is open
    private boolean isXYopen(int x, int y) {
        getNewXY();
        if (!buttons[nextX][nextY].getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    // Non-blocking play by computer
    private void player2Plays() {
        isOpen = false;
        while (!isOpen) {
            getNewXY();
            if (isXYopen(nextX, nextY)) {
                isOpen = true;
                buttons[nextX][nextY].setText("O");
                System.out.println("Draw O in [" + nextX + "] [" + nextY + "]");
            }
        }
    }

    // Blocking play by computer
    private void player2Blocks() {
        buttons[nextX][nextY].setText("O");
        System.out.println("Draw O in [" + nextX + "] [" + nextY + "]");
    }
}