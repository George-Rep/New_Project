package part1.lesson02.task01;
/**
 * Шишкин Георгий
 *
 * Задание 1.2.1
 * Написать программу ”Hello, World!”. В ходе выполнения программы она должна выбросить исключение и завершиться с ошибкой.
 *
 *         Смоделировав ошибку «NullPointerException»
 *         Смоделировав ошибку «ArrayIndexOutOfBoundsException»
 *         Вызвав свой вариант ошибки через оператор throw
 */
public class Main {
    /**
     * Генерация исключений.
     * Два первых исключения пойманы, третье специально не ловится
     * (" В ходе выполнения программы она должна выбросить исключение и завершиться с ошибкой")
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");
        try {
            Object obj = null;
            obj.hashCode();
        } catch (NullPointerException e) {
            System.out.println("NullPointerException");
        }
        try {
            int c[] = {1};
            c[1] = 1;
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("ArrayIndexOutOfBoundsException");
        }
        throw new Exception("Exception3");
    }
}
