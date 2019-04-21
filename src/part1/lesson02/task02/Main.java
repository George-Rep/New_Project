package part1.lesson02.task02;

import java.security.SecureRandom;
import java.util.Scanner;

/**
 * Составить программу, генерирующую N случайных чисел. Для каждого числа k вычислить квадратный корень q.
 * Если квадрат целой части q числа равен k, то вывести это число на экран.
 * Предусмотреть что первоначальные числа могут быть отрицательные, в этом случае генерировать исключение.
 */
public class Main {
    /**
     * С консоли считывается число (N). генерируется N случайных чисел, для отрицательных чисел
     * генерируется исключение. Остальные числа, при выполнении условия выводятся на экран.
     * q=sqrt(k)
     */
    public static void main(String[] args) {
        int N, numbersCounter = 0;

        Scanner inputScanner = new Scanner(System.in);
        do {
            System.out.println("Enter N:");
            while (!inputScanner.hasNextInt()) {
                System.out.println("введите положительное число");
                inputScanner.next();
            }
            N = inputScanner.nextInt();
        } while (N <= 0);

        System.out.println();
        SecureRandom secureRandom = new SecureRandom();
        int nextRandom;
        int exceptionsCounter = 0;
        for (int i = 0; i < N; i++) {
            try {
                nextRandom = secureRandom.nextInt();
                if (nextRandom < 0) throw new Exception("11");
            } catch (Exception e) {
                exceptionsCounter++;
                continue;
            }
            if (nextRandom == Math.pow(Math.floor(Math.sqrt(nextRandom)), 2)) {
                System.out.println("k=" + nextRandom + " q=" + (int) Math.sqrt(nextRandom));
                numbersCounter++;
            }
        }
        System.out.println(System.getProperty("line.separator") + "Обработано исключений (отрицательные числа): " + exceptionsCounter
                + System.getProperty("line.separator") + "Найдено чисел: " + numbersCounter);
    }
}

