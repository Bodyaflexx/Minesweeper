package minesweeper;

import java.util.*;

public class Field {
    boolean[][] visited = new boolean[9][9];
    private final char mine = 'X';
    private final char safeZone = '.';
    private final char flag = '*';
    char[][] field = new char[9][9];
    char[][] fieldWithoutMines = new char[9][9];
    private int amountOfMines;
    String command;
    private List<Integer> coordinates;
    int r;
    int c;

    public char[][] getField() {
        return field;
    }

    public void setCoordinates(List<Integer> coordinates) {
        this.coordinates = coordinates;
        this.r = coordinates.get(1);
        this.c = coordinates.get(0);
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setMines(int amountOfMines) {
        this.amountOfMines = amountOfMines;
        fillField();
        copyField();
        fillFieldWithoutMines();
    }

    public void printField() {
        for (char[] chars : field) {
            for (char c : chars) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public void printFieldWithoutMines() {
        System.out.println(" |123456789|\n" +
                "-|---------|");
        for (int i = 0; i < fieldWithoutMines.length; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < fieldWithoutMines[i].length; j++) {
                System.out.print(fieldWithoutMines[i][j]);
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("-|---------|");
    }

    private void fillField() {
        for (char[] chars : field) {
            Arrays.fill(chars, safeZone);
        }
        Random random = new Random();
        for (int i = 0; i < amountOfMines; i++) {
            int row = random.nextInt(0, 9);
            int column = random.nextInt(0, 9);
            while (field[row][column] == mine) {
                row = random.nextInt(0, 9);
                column = random.nextInt(0, 9);
            }
            field[row][column] = mine;
        }
    }

    private void fillFieldWithoutMines() {
        for (char[] fieldWithoutMine : fieldWithoutMines) {
            Arrays.fill(fieldWithoutMine, safeZone);
        }

    }


    private void copyField() {
        for (int i = 0; i < field.length; i++) {
            System.arraycopy(field[i], 0, fieldWithoutMines[i], 0, field[i].length);
        }
    }

    private int checkBox(int row, int column) {
        int countOfMinesAround = 0;
        for (int m = row - 1; m <= row + 1 && m < 9; m++) {
            for (int n = column - 1; n <= column + 1 && n < 9; n++) {
                if (m < 0 || n < 0 || m >= field.length || n >= field.length) {
                    continue;
                }
                if (field[m][n] == mine) {
                    countOfMinesAround++;
                }
            }
        }
        return countOfMinesAround;
    }


    public boolean checkCoordinates() {
        if (fieldWithoutMines[coordinates.get(1)][coordinates.get(0)] == safeZone) {
            return true;
        } else if (fieldWithoutMines[coordinates.get(1)][coordinates.get(0)] == flag) {
            return true;
        } else {
            System.out.println("There is a number here!");
            return false;
        }
    }

    public boolean checkOnFinish() {
        int countOfPoints = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == mine) {
                    if (fieldWithoutMines[i][j] != flag) {
                        return false;
                    } else {
                        countOfPoints++;
                    }
                }
            }
        }
        return countOfPoints == amountOfMines;
    }

    public boolean otherCheckerOnFinish() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (fieldWithoutMines[i][j] == safeZone) {
                    if (field[i][j] != mine) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkOnLose() {
        return field[coordinates.get(1)][coordinates.get(0)] == mine && command.equalsIgnoreCase("Free");
    }


    public void stepChecker() {
        if (command.equalsIgnoreCase("Free")) {
            if (checkBox(r, c) != 0) {
                fieldWithoutMines[r][c] = (char) (checkBox(r, c) + '0');
            } else {
                reveal(r, c);
            }
        } else if (command.equalsIgnoreCase("Mine")) {
            if (fieldWithoutMines[coordinates.get(1)][coordinates.get(0)] == safeZone) {
                fieldWithoutMines[coordinates.get(1)][coordinates.get(0)] = flag;
            } else if (fieldWithoutMines[coordinates.get(1)][coordinates.get(0)] == flag) {
                fieldWithoutMines[coordinates.get(1)][coordinates.get(0)] = safeZone;
            }
        }
    }


    boolean outBounds(int x, int y) {
        return x < 0 || y < 0 || x >= 9 || y >= 9;
    }


    void reveal(int x, int y) {
        if (outBounds(x, y)) return;
        if (visited[x][y]) return;
        visited[x][y] = true;
        if (checkBox(x, y) != 0) {
            fieldWithoutMines[x][y] = (char) (checkBox(x, y) + '0');
            return;
        }
        fieldWithoutMines[x][y] = '/';
        reveal(x - 1, y - 1);
        reveal(x - 1, y + 1);
        reveal(x + 1, y - 1);
        reveal(x + 1, y + 1);
        reveal(x - 1, y);
        reveal(x + 1, y);
        reveal(x, y - 1);
        reveal(x, y + 1);
    }
}
