package part1.lesson07.task01;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Дан массив случайных чисел. Написать программу для вычисления факториалов всех элементов массива.
 * Использовать пул потоков для решения задачи.
 */
public class Main {
    /**
     * Метод для получения с консоли целого числа int не менее параметра minValue
     *
     * @param inputScanner Scanner
     * @param minValue     минимально допустимое значение числа
     * @return целое число int не менее параметра minValue
     */
    private static int scanPositiveInteger(Scanner inputScanner, int minValue) {
        int number;
        do {
            while (!inputScanner.hasNextInt()) {
                inputScanner.next();
            }
            number = inputScanner.nextInt();
            if (number < minValue) {
                System.out.println("положительное целое число не менее " + minValue + ":");
            }
        } while (number < minValue);
        inputScanner.nextLine();
        return number;
    }

    /**
     * С консоли считываются количество элементов массива случайных чисел, максимальное значение элементов.
     * Поставлена проверка для исключения ситуации, когда например количество уникальных целых чисел==10,
     * а максимальное значение ограничено 5. Рассчитывается общее количество потоков для FixedThreadPool.
     * Предполагается, что по одному элементу массива будет работать половина от общего количества потоков.
     * Т.е. если потоков например 4, то по одному элементу вычисления будут проводиться двумя потоками.
     * <p>
     * Генерируются случайные числа согласно введенным параметрам. Числа генерируются уникальными (с помощью
     * HashSet) и сортируются( с помощью TreeSet).
     * <p>
     * При вычислениях факториалов чисел из массива "используются результаты предыдущих вычислений" следующим образом:
     * Есть массив отсортированных уникальных чисел. Для наименьшего числа действительно будет вычисляться просто
     * факториал. Для остальных чисел будет вычисляться _частное_ от деления факториала числа на факториал предыдущего
     * числа (другими словами произведение всех чисел от {[предыдущее число]+1} до [число - текущий элемент массива])
     * Таким образом, при завершении вычислений для получения факториалов нужно будет последовательно в цикле по
     * элементам массива начиная со второго умножить факториал предыдущего элемента на указанное _частное_ для
     * текущего элемента. Т.е. факториал первого элемента умножаем на _частное_ для второго элемента - получаем
     * факториал для второго элемента и т.д.
     * <p>
     * Все указанные вычисления частей и _частных_ факториалов могут проводиться независимо, не дожидаясь вычисления
     * факториала какого-либо конкретного элемента. В данном случае FixedThreadPool просто получает сразу все
     * необходимые задачи. Вычисление факториала первого элемента или _частного_ для других элементов массива
     * выполняется [общее число потоков]/2  задачами, если умножаемых чисел не меньше чем потоков. Если меньше,
     * то одному такому элементу массива соответствует одна задача.
     * <p>
     * Для хранения результатов вычислений используется AtomicReferenceArray<BigInteger>. Изначально его элементы
     * равны 1. Каждая задача пула потоков вычисляет часть факториала или целый факториал и умножает соответствующий
     * элемент массива на вычисленное значение. Например, если вычисление _частного_ (см. выше) для элемента массива
     * случайных чисел разделено на несколько задач, то каждая из этих задач, используя
     * AtomicReferenceArray.compareAndSet умножает один и тот же соответствующий элемент AtomicReferenceArray на
     * результат своих вычислений.
     * <p>
     * Ожидание результатов вычислений производится с помощью метода пула потоков
     * awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)   после shutdown().
     */
    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Введите количество элементов в массиве случайных чисел: ");
        int arraySize = scanPositiveInteger(inputScanner, 1);
        System.out.println("Максимальное значение элемента: ");
        int maxArrayElementValue = scanPositiveInteger(inputScanner, (int) Math.ceil(arraySize * 1.1) + 1);

        int arrayIndex = 0;
        int threadsNumber = Math.max(Runtime.getRuntime().availableProcessors(), 2);
        int threadsForElement = (int) Math.ceil(threadsNumber / 2.0);
        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);
        BigInteger[] bigIntArray = new BigInteger[arraySize];


        for (int i = 0; i < arraySize; ++i) {
            bigIntArray[i] = BigInteger.ONE;
        }
        AtomicReferenceArray<BigInteger> atomicArray = new AtomicReferenceArray<>(bigIntArray);
        int[] randomIntArray = new int[arraySize];
        SecureRandom secureRandom = new SecureRandom();

        HashSet<Integer> hashSet = new HashSet<>();
        TreeSet<Integer> treeSet;

        for (int i = 0; i < arraySize; ++i) {
            hashSet.add(1 + secureRandom.nextInt(maxArrayElementValue));
        }
        while (hashSet.size() < arraySize) {
            hashSet.add(1 + secureRandom.nextInt(maxArrayElementValue));
        }
        treeSet = new TreeSet<>(hashSet);
        for (Integer randomNumber : treeSet) {
            randomIntArray[arrayIndex] = randomNumber;
            arrayIndex++;
        }

        if (randomIntArray[0] > threadsForElement) {
            int range = (randomIntArray[0] - 1) / threadsForElement;
            for (int k = 1; k < threadsForElement; ++k) {
                executor.submit(new RunnableThread(randomIntArray[0] - k * range + 1, randomIntArray[0] - (k - 1) * range, 0, atomicArray));
            }
            executor.submit(new RunnableThread(2, randomIntArray[0] - (threadsForElement - 1) * range, 0, atomicArray));

        } else {
            executor.submit(new RunnableThread(1, randomIntArray[0], 0, atomicArray));
        }

        for (int i = 1; i < arraySize; ++i) {
            int elementsDiff = randomIntArray[i] - randomIntArray[i - 1];
            if (elementsDiff > (threadsForElement - 1)) {
                int range = elementsDiff / threadsForElement;
                for (int k = 1; k < threadsForElement; ++k) {
                    executor.submit(new RunnableThread(randomIntArray[i] - k * range + 1, randomIntArray[i] - (k - 1) * range, i, atomicArray));
                }
                executor.submit(new RunnableThread(randomIntArray[i - 1] + 1, randomIntArray[i] - (threadsForElement - 1) * range, i, atomicArray));

            } else {
                executor.submit(new RunnableThread(randomIntArray[i - 1] + 1, randomIntArray[i], i, atomicArray));
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\r\n1) " + randomIntArray[0] + "!\r\n" + atomicArray.get(0));
        for (int i = 1; i < arraySize; ++i) {
            atomicArray.set(i, atomicArray.get(i).multiply(atomicArray.get(i - 1)));
            System.out.println((i + 1) + ") " + randomIntArray[i] + "!\r\n" + atomicArray.get(i));
        }
    }
}
