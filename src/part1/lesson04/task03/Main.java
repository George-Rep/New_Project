package part1.lesson04.task03;


import java.text.DecimalFormat;
import java.text.NumberFormat;
//import java.util.HashMap;

/**
 * Доработать классы MathBox и ObjectBox таким образом, чтобы MathBox был наследником ObjectBox.
 * Необходимо сделать такую связь, правильно распределить поля и методы. Функциональность в целом должна сохраниться.
 * При попытке положить Object в MathBox должно создаваться исключение.
 */
public class Main {
    /**
     * задаются (прямо в тексте) значения N (число элементов массива) и maxNumber (числа-элементы массива не должны превышать данное значение)
     * numberArray - это массив Number из условия задачи. Условие "элементы не могут повторяться" рассмотрено только для элементов
     * массива numberArray. Это делается mathUtils.removeDuplicates(numberArray, maxNumber) - модифицированным Quicksort.
     * Далее условие "элементы не могут повторяться" не рассматривается, например для случая, когда несколько элементов коллекции станут
     * равными нулю при общем делении элементов в методе mathBox.splitter. Далее для демонстрации запускаются методы MathBox:
     * summator, toString, hashCode. Показано, как для разных объектов MathBox, с одинаковым содержимым, меняется
     * хешкод и значение equals, при добавлении новых элементов в коллекцию и затем при удалении этих новых элементов.
     */
    public static void main(String[] args) {
        int N = 100;
        long maxNumber = 1000000;
        Number[] numberArray = new Number[N];
        MathUtils mathUtils = new MathUtils();
        MathBox mathBox;
        MathBox mathBox2;
        ObjectBox objectBox;
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
        objectBox = new ObjectBox();
        objectBox.addObject(mathUtils);
        mathBox.addObject(4);

        try {
//            mathBox.addObject(mathUtils); //проверка генерации исключения
        } catch (java.lang.ClassCastException e) {
            System.out.println(lineSeparator + "Exception: в Mathbox добавляют не-Number" + lineSeparator);
        }

        NumberFormat formatter = new DecimalFormat("#0.00");
        System.out.println(lineSeparator + "Cумма элементов коллекции: " + formatter.format(mathBox.summator()));

        mathBox.splitter(500);
        System.out.println("Значения после деления(toString): " + lineSeparator + mathBox.toString() + lineSeparator);

//        HashMap<Integer, MathBox> hm = new HashMap();
//        hm.put(mathBox.hashCode(), mathBox);
//        mathBox = new MathBox(numberArray);
//        hm.put(mathBox.hashCode(), mathBox);

        mathBox = new MathBox(numberArray);
        mathBox2 = new MathBox(numberArray);
        System.out.println("Хешкод mathBox1: " + mathBox.hashCode());
        System.out.println("Хешкод mathBox2: " + mathBox2.hashCode());
        System.out.println("equals mathBox1,mathBox2: " + mathBox.equals(mathBox2));
        mathBox.addObject(4);
        mathBox.addObject(5);
        System.out.println(lineSeparator + "Добавлены элементы в mathBox1" + lineSeparator);
        System.out.println("Хешкод mathBox1 после добавления элементов: " + mathBox.hashCode());
        System.out.println("equals mathBox1,mathBox2 после добавления элементов в 1: " + mathBox.equals(mathBox2));
        mathBox.deleteObject(4);
        mathBox.deleteObject(5);
        System.out.println(lineSeparator + "Удалены элементы из mathBox1" + lineSeparator);
        System.out.println("Хешкод mathBox1 после удаления элементов: " + mathBox.hashCode());
        System.out.println("equals mathBox1,mathBox2 после удаления элементов из 1: " + mathBox.equals(mathBox2));

//        objectBox.addObject(objectBox);
//        System.out.println(objectBox.dump());
    }
}


