package org.onlinetictactoe.game;

public class TicTacToe {
    private char[][] board;
    private char currentPlayerMark;
    private static final int SIZE = 3;

    public TicTacToe() {
        board = new char[SIZE][SIZE];
        currentPlayerMark = 'X';
        initializeBoard();
    }

    public char getMark() {
        return currentPlayerMark;
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '-';
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean makeMove(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == '-') {
            board[row][col] = currentPlayerMark;
            return true;
        }
        return false;
    }

    public boolean checkForWin() {
        return (checkRowsForWin() || checkColumnsForWin() || checkDiagonalsForWin());
    }

    public boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    public void changePlayer() {
        currentPlayerMark = (currentPlayerMark == 'X') ? 'O' : 'X';
    }

    private boolean checkRowsForWin() {
        for (int i = 0; i < SIZE; i++) {
            if (checkRowCol(board[i][0], board[i][1], board[i][2])) {
                board[i][0] = board[i][1] = board[i][2] = 'W';
                return true;
            }
        }
        return false;
    }

    public boolean winningSquare(int x, int y) {
        return (board[x][y] == 'W');
    }

    private boolean checkColumnsForWin() {
        for (int i = 0; i < SIZE; i++) {
            if (checkRowCol(board[0][i], board[1][i], board[2][i])) {
                board[0][i] = board[1][i] = board[2][i] = 'W';
                return true;
            }
        }
        return false;
    }



    private boolean checkDiagonalsForWin() {
        if (checkRowCol(board[0][0], board[1][1], board[2][2])) {
            board[0][0] = board[1][1] = board[2][2] = 'W';
            return true;
        }
        if (checkRowCol(board[0][2], board[1][1], board[2][0])) {
            board[0][2] = board[1][1] = board[2][0] = 'W';
            return true;
        }
        return false;
    }

    private boolean checkRowCol(char c1, char c2, char c3) {
        return ((c1 != '-') && (c1 == c2) && (c2 == c3));
    }
}
