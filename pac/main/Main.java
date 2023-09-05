package pac.main;

import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final char DOT_HUMAN = 'X'; // фишка игрока-человека
    private static final char DOT_AI = '0'; // фишка игрока-компьютера
    private static final char DOT_EMPTY = '_'; // признак пустого поля
    private static final Scanner scanner = new Scanner(System.in);

    private static final Random random = new Random();
    private static char[][] field; // двумерный массив хранит текущее состояние игрового поля
    private static int fieldSizeX; // размерность игрового поля по горизонтали
    private static int fieldSizeY; // размерность игрового поля по вертикали

    private static int winCount; // количество фишек для победы


    public static void main(String[] args) {
        while (true) {
            initialize();
            printField();
            while (true){
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, "Компьютер победил!"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз? (Y - да): " );
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }



    }

    /**
     * Инициализация объектов игры
     */
    private static void initialize() {
        fieldSizeX = 9;
        fieldSizeY = 9;
        winCount = 5;
        field = new char[fieldSizeY][fieldSizeY];
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                field[i][j] = DOT_EMPTY;
            }
        }
    }

    /**
     * отрисовка игрового поля
     */
    private static void printField() {
        System.out.print(" ");
        for (int i = 0; i < fieldSizeY * 2 + 1; i++) {
            System.out.print((i % 2 == 0) ? " " : i / 2 + 1);
        }
        System.out.println();

        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < fieldSizeY; j++) {
                System.out.print(field[i][j] + "|");
            }
            System.out.println();
        }
        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print(" ");
        }
        System.out.println();

    }

    private static void humanTurn() {
        int x, y;
        do {
            System.out.printf("Введите координаты хода X (от 1 до %d) и Y (от 1 до %d) \n через пробел >>>>", fieldSizeX, fieldSizeY);
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));

        field[x][y] = DOT_HUMAN;
    }


    /**
     * Проверка, является ли ячейка пустой (DOT_EMPTY)
     */

    private static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода
     */
    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Обработка хода компьютера
     */
    private static void aiTurn() {
        if (modelingOneStep(DOT_AI)) return; // поиск выигрышного хода компьютера
        if (modelingOneStep(DOT_HUMAN)) return; // поиск выигрышного хода человека
        if (modelingTwoStep(DOT_AI)) return; // поиск выигрышного хода компьютера через 2 шага
        if (modelingTwoStep(DOT_HUMAN)) return; // поиск выигрышного хода человека через 2 шага

        int x, y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellEmpty(x, y));

        field[x][y] = DOT_AI;
    }

    /**
     * Метод проверки выигрыша при следующем шаге компьютера или человека
     */

    private static boolean modelingOneStep(char c) {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) {
                    field[i][j] = c;
                    if (checkWin(c)) {
                        field[i][j] = DOT_AI;
                        return true;
                    } else field[i][j] = DOT_EMPTY;
                }
            }
        }
        return false;
    }

    /**
     * Метод проверки выигрыша компьютера или человека через два шага
     */
    private static boolean modelingTwoStep(char c) {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) {
                    field[i][j] = c;
                    if (modelingOneStep(c)) {
                        field[i][j] = DOT_EMPTY;
                        return true;
                    }
                    field[i][j] = DOT_EMPTY;
                }
            }
        }
        return false;
    }
    /**
     * Проверка состояния игры
     * @param c фишка игрока
     * @param s победный слоган
     * @return
     */
    private static boolean checkGameState (char c, String s){
        if (checkWin(c)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }

        return false; // игра продолжается
    }



    /**
     * Проверка победы
     *
     * @param c
     * @return
     */
    private static boolean checkWin(char c) {
        for (int i = 0; i < fieldSizeX; i++) { // проход по горизонталям
            int win = 0;
            for (int j = 0; j < fieldSizeY; j++) {
                if (field[i][j] == c) win ++;
                else win = 0;
                if (win == winCount) return true;
            }
        }
        for (int j = 0; j < fieldSizeX; j++) { // проход по вертикалям
            int win = 0;
            for (int i = 0; i < fieldSizeY; i++) {
                if (field[i][j] == c) win ++;
                else win = 0;
                if (win == winCount) return true;
            }
        }
        for (int i = 0; i < fieldSizeX; i++) { // проход по диагоналям слева направа снизу вверх до главной диагонали
            int win = 0;
            int ii = i;
            for (int j = 0; j < fieldSizeY && ii >= 0; j++, ii--) {
                if (field[ii][j] == c) win ++;
                else win = 0;
                if (win == winCount) return true;
            }
        }
        for (int j = 0; j < fieldSizeY;  j++) { // проход по диагоналям слева направа снизу вверх от главной диагонали до конца поля
            int win = 0;
            int jj = j;
            for (int i = fieldSizeX-1; jj < fieldSizeY && i >= 0; jj++, i--) {
                if (field[i][jj] == c) win ++;
                else win = 0;
                if (win == winCount) return true;
            }
        }

        for (int i = fieldSizeX-1; i > 0; i--) { // проход по диагоналям слева направа сверху вниз до побочной диагонали
            int win = 0;
            int ii = i;
            for (int j = 0; j < fieldSizeY & ii < fieldSizeX; j++, ii++) {
                if (field[ii][j] == c) win ++;
                else win = 0;
                if (win == winCount) return true;
            }
        }
        for (int j = 0; j < fieldSizeY;  j++) { // проход по диагоналям слева направа сверху вниз от побочной диагонали до конца поля
            int win = 0;
            int jj = j;
            for (int i = 0; jj < fieldSizeY & i < fieldSizeY; jj++, i++) {
                if (field[i][jj] == c) win ++;
                else win = 0;
                if (win == winCount) return true;
            }
        }
        return false;
    }

    /**
     * проверка на ничью
     * @return
     */
    private static boolean checkDraw() {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) return false;
            }
        }
        return true;
    }





}