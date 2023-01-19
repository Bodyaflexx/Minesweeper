package minesweeper;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    Scanner scanner = new Scanner(System.in);
    Field field = new Field();

    public void startGame() {
        greeting();
        field.printFieldWithoutMines();
        while (!field.checkOnFinish() || !field.otherCheckerOnFinish()) {
            play();
            if (field.checkOnLose()) {
                System.out.println("You stepped on a mine and failed!");
                field.printField();
                return;
            }
        }
        System.out.println("Congratulations! You found all the mines!");

    }

    private void greeting() {
        System.out.println("How many mines do you want on the field?");
        int amountOfMines = scanner.nextInt();
        field.setMines(amountOfMines);
    }

    private void play() {
        do {
            System.out.println("Set/unset mine marks or claim a cell as free:");
            List<Integer> coordinates = new ArrayList<>();
            coordinates.add(scanner.nextInt() - 1);
            coordinates.add(scanner.nextInt() - 1);
            field.setCoordinates(coordinates);
            field.setCommand(scanner.next());
        } while (!field.checkCoordinates());
        field.stepChecker();
        field.printFieldWithoutMines();
    }
}
