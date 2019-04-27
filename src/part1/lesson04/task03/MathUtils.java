package part1.lesson04.task03;

import java.security.SecureRandom;
import java.sql.Timestamp;
/**
 * класс вспомагательных функций для
 * генерации Number и обеспечения уникальности элементов массивв
 * (несколько методов представляют методы модифицированной Quicksort)
 */
public class MathUtils {
    /**
     * генерирует случайное число, ограниченное параметром
     * @param maxNumber генерируемые числа-элементы массива не будут превышать данное значение
     * @return число в обертке Number
     */
        Number generateNumber(long maxNumber) {
            long maxNumberForType;
            if (maxNumber>Integer.MAX_VALUE) {maxNumberForType=Integer.MAX_VALUE;}
            else {maxNumberForType=maxNumber;}
            SecureRandom secureRandom = new SecureRandom();
            switch (secureRandom.nextInt(6)) {
                case 0:
                    return (byte) secureRandom.nextInt((int) maxNumberForType);
                case 1:
                    return (short) secureRandom.nextInt((int) maxNumberForType);
                case 2:
                    return secureRandom.nextInt((int) maxNumberForType);
                case 3:
                    return -maxNumber + (long) secureRandom.nextDouble() * 2 * maxNumber;
                case 4:
                    return -maxNumber + secureRandom.nextFloat() * 2 * maxNumber;
                case 5:
                    return -maxNumber + secureRandom.nextDouble() * 2 * maxNumber;
            }


            return secureRandom.nextInt((int) maxNumber);
        }

    /**
     * перестановка местами элементов массива с индексами arrayIndex1 и arrayIndex2
     * @param numberArray массив, в котором будет происходить перестановка элементов
     * @param arrayIndex1 индекс элемента массива 1
     * @param arrayIndex2 индекс элемента массива 2
     */

        void swap(Number[] numberArray, int arrayIndex1, int arrayIndex2) {
            Number temp;
            temp = numberArray[arrayIndex1];
            numberArray[arrayIndex1] = numberArray[arrayIndex2];
            numberArray[arrayIndex2] = temp;
        }
    /**
     * partition, метод разбиения quicksort ("элементы меньше опорного помещаются перед ним, а больше или равные после")
     * При обнаружении одинаковых элементов, один из них меняется на заново сгенерированное случайное число.
     * @param numberArrayPart массив для сортировки
     * @param lowIndex начальный индекс фрагмента массива для сортировки
     * @param highIndex конечный индекс фрагмента массива для сортировки
     * @param maxNumber см. generateNumber
     * @return индекс опорного значения (которое делит таблицу на две части, в которых далее идет сортировка)
     */
        int partition(Number[] numberArrayPart, int lowIndex, int highIndex, long maxNumber) {
            Number pivot = numberArrayPart[highIndex], newRandomNumber;
            double pivotValue, arrayValue;
            int i = (lowIndex - 1);
            for (int j = lowIndex; j < highIndex; j++) {
                pivotValue = pivot.doubleValue();
                arrayValue = numberArrayPart[j].doubleValue();
                if (arrayValue == pivotValue) {
                    do
                        newRandomNumber = generateNumber(maxNumber);
                    while (newRandomNumber.doubleValue() == pivotValue);
                    arrayValue = newRandomNumber.doubleValue();
                    numberArrayPart[j] = newRandomNumber;
                }
                if (arrayValue < pivotValue) {
                    i++;
                    swap(numberArrayPart, i, j);
                }

            }
            swap(numberArrayPart, i + 1, highIndex);
            return i + 1;
        }

    /**
     * сортировка части массива от lowIndex до highIndex
     * @param numberArrayPart массив для сортировки
     * @param lowIndex начальный индекс фрагмента массива для сортировки
     * @param highIndex конечный индекс фрагмента массива для сортировки
     * @param maxNumber см. generateNumber
     */
        private void arrayPartSort(Number[] numberArrayPart, int lowIndex, int highIndex, long maxNumber) {
            if (lowIndex < highIndex) {
                int partitioningIndex = partition(numberArrayPart, lowIndex, highIndex, maxNumber);
                arrayPartSort(numberArrayPart, lowIndex, partitioningIndex - 1, maxNumber);
                arrayPartSort(numberArrayPart, partitioningIndex + 1, highIndex, maxNumber);
            }
        }

    /**
     * основной метод класса, сортирует quicksort'ом массив arrayForSorting
     * В данном случае в процессе сортировки обеспечивается уникальность случайных чисел-элементов массива
     * @param arrayForSorting  массив для сортировки
     * @param maxNumber  см. generateNumber
     * @return время выполнения
     */
        public long removeDuplicates(Number[] arrayForSorting, long maxNumber) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            arrayPartSort(arrayForSorting, 0, arrayForSorting.length - 1, maxNumber);
            return new Timestamp(System.currentTimeMillis()).getTime() - timestamp.getTime();
        }
    }
