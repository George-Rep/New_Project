package part1.lesson04.task01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * класс MathBox
 *
 * @see Main
 */
public class MathBox {
  private  ArrayList<Number> arrayList;

    /**
     * конструктор
     *
     * @param numberArray массив Number, уникальных чисел, генерируется в классе Main
     * @see Main
     */
    MathBox(Number[] numberArray) {
        arrayList = new ArrayList<Number>(Arrays.asList(numberArray));
    }

    /**
     * суммирует элементы arrayList, возвращает сумму
     *
     * @return сумма элементов arrayList
     */
    double summator() {
        double sum = 0;
        for (Number temp : arrayList) {
            sum += temp.doubleValue();
        }
        return sum;
    }

    /**
     * делит элементы arrayList, элементы заменяются результатом деления
     *
     * @param divisor - делитель элементов arrayList
     */
    void splitter(double divisor) {
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.set(i, arrayList.get(i).doubleValue() / divisor);
        }
    }

    /**
     * возвращает текстовое представление объекта. Между элементами
     * выводится сивол новой строки
     *
     * @return текстовое представление объекта
     */
    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Number> iterator = arrayList.iterator();

        if (iterator.hasNext()) stringBuilder.append(iterator.next());
        while (iterator.hasNext()) {
            stringBuilder.append(lineSeparator).append(iterator.next());
        }
        return stringBuilder.toString();
    }

    /**
     * возвращает хешкод объекта класса MathBox. Для расчетов используется коллекция
     * arrayList, принадлежащая объекту.
     *
     * @return хешкод объекта MathBox
     */
    @Override
    public int hashCode() {
        int code = Integer.hashCode(arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            code = 37 * code + Double.hashCode(arrayList.get(i).doubleValue());
        }
        return code;
    }

    /**
     * equals. True если объект Mathbox "равен" объекту из параметра obj.
     * false, если есть отличия. true если это тот же самый объект.
     * Сравнивается число элементов и далее doubleValue элементов.
     *
     * @param obj объект для сравнения
     * @return true если объекты "равны"
     */
    @Override
    public boolean equals(Object obj) {
        ArrayList<Number> objToArrayList;
        if (this == obj) return true;
        if (!(obj instanceof MathBox)) return false;
        objToArrayList = ((MathBox) obj).arrayList;
        if (!(arrayList.size() == objToArrayList.size())) return false;
        for (int i = 0; i < arrayList.size(); i++) {
            if (!(arrayList.get(i).doubleValue() == objToArrayList.get(i).doubleValue())) return false;
        }
        return true;
    }

    /**
     * метод удаления из arrayList элемента, заданного в параметре.
     *
     * @param number число, которое нужно удалить из arrayList
     * @return true, если элемент был в коллекции и был удален
     */
    boolean deleteNumber(int number) {
        Iterator iterator = arrayList.iterator();
        Number number2;
        while (iterator.hasNext()) {
            number2 = (Number) iterator.next();
            if (number2.doubleValue() == (double) number) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
