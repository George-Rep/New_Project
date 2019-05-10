package part1.lesson07.task01;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Класс потока.
 * см. конструктор.
 */
public class RunnableThread implements Runnable {
    private int startAt;
    private int endAt;
    private int arrayIndex;
    private AtomicReferenceArray<BigInteger> atomicArray;

    /**
     * конструктор.
     *
     * @param startAt     начальное число, см. endAt
     * @param endAt       конечное число, по алгоритму необходимо найти произведение чисел от startAt до endAt
     * @param arrayIndex  индекс случайного числа(для этих чисел вычисляются факториалы) в упорядоченном массиве
     *                    atomicArray
     * @param atomicArray упорядоченный массив случайных чисел, использующий AtomicReferenceArray
     */
    RunnableThread(int startAt, int endAt, int arrayIndex, AtomicReferenceArray<BigInteger> atomicArray) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.arrayIndex = arrayIndex;
        this.atomicArray = atomicArray;
    }

    /**
     * метод интерфейса Runnable
     * В переменную product помещается произведение всех чисел от startAt до endAt включительно.
     * Если startAt==endAt, то это будет просто startAt, без умножения.
     * Далее элемент массива AtomicReferenceArray atomicArray с индексом arrayIndex с помощью compareAndSet
     * умножается на product. (Изначально до запуска потоков RunnableThread элементы atomicArray равны 1.
     * см. также описание main в классе Main.
     * )
     *
     * @see Main
     */
    @Override
    public void run() {
        BigInteger product = BigInteger.valueOf(startAt);
        for (int index = startAt + 1; index < endAt + 1; index++) {
            product = product.multiply(BigInteger.valueOf(index));
        }
        boolean success = false;
        while (!success) {
            BigInteger previous = atomicArray.get(arrayIndex);
            BigInteger next = previous.multiply(product);
            success = atomicArray.compareAndSet(arrayIndex, previous, next);
        }
    }
}