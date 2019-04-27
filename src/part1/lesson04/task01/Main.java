package part1.lesson04.task01;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

/**
 * Написать класс MathBox, реализующий следующий функционал:
 * Конструктор на вход получает массив Number. Элементы не могут повторяться. Элементы массива внутри объекта раскладываются в подходящую коллекцию (выбрать самостоятельно).
 * Существует метод summator, возвращающий сумму всех элементов коллекции.
 * Существует метод splitter, выполняющий поочередное деление всех хранящихся в объекте элементов на делитель, являющийся аргументом метода. Хранящиеся в объекте данные полностью заменяются результатами деления.
 * Необходимо правильно переопределить методы toString, hashCode, equals, чтобы можно было использовать MathBox для вывода данных на экран и хранение объектов этого класса в коллекциях (например, hashMap). Выполнение контракта обязательно!
 * Создать метод, который получает на вход Integer и если такое значение есть в коллекции, удаляет его.
 */
public class Main {
    /**
     * задаются (прямо в тексте) значения N (число элементов массива) и maxNumber (числа-элементы массива не должны превышать данное значение)
     * numberArray - это массив Number из условия задачи. Условие "элементы не могут повторяться" рассмотрено только для элементов
     * массива numberArray. Это делается mathUtils.removeDuplicates(numberArray, maxNumber) - модифицированным Quicksort.
     * Далее условие "элементы не могут повторяться" не рассматривается, например для случая, когда несколько элементов коллекции станут
     * равными нулю при общем делении элементов в методе mathBox.splitter. Далее для демонстрации запускаются методы MathBox:
     * summator, toString, hashCode.
     */
    public static void main(String[] args) {
        int N = 100;
        long maxNumber = 1000000;
        Number[] numberArray = new Number[N];
        MathUtils mathUtils = new MathUtils();
        MathBox mathBox;
        String lineSeparator = System.getProperty("line.separator");
        for (int i = 0; i < N; ++i) {
            numberArray[i] = mathUtils.generateNumber(maxNumber);
        }

/*  тест удаления дубликатов
       numberArray[0] = (byte) 127;
        numberArray[1] = (short) 127;
        numberArray[20] = (float) 127;
        numberArray[30] = (double) 127;*/

        try {
            mathUtils.removeDuplicates(numberArray, maxNumber);
        } catch (StackOverflowError e) {
            System.out.println(lineSeparator + "Exception: StackOverflowError" + lineSeparator);
        }

        mathBox = new MathBox(numberArray);
        NumberFormat formatter = new DecimalFormat("#0.00");

        try {
            System.out.println(lineSeparator + "Сумма значений(summator коллекции): " + formatter.format(mathBox.summator()) + lineSeparator);
        } catch (Exception e) {
            System.out.println(lineSeparator + "Exception: переполнение при суммировании" + lineSeparator);
        }

        mathBox.splitter(200);
        System.out.println("Значения(toString): " + lineSeparator);
        System.out.println(mathBox.toString());
        System.out.println(lineSeparator + "mathBox.hashCode: " + mathBox.hashCode());

//        HashMap<Integer, MathBox> hm = new HashMap();
//        hm.put(mathBox.hashCode(), mathBox);
//        mathBox = new MathBox(numberArray);
//        hm.put(mathBox.hashCode(), mathBox);
    }
}


