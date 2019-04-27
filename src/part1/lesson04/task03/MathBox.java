package part1.lesson04.task03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * класс MathBox
 *
 * @see Main
 */
public class MathBox<T extends Number> extends part1.lesson04.task03.ObjectBox<T> {

    /**
     * конструктор
     *
     * @param numberArray массив Number, уникальных чисел, генерируется в классе Main
     * @see Main
     */
    MathBox(T[] numberArray) {
        setArrayList(new ArrayList<T>(Arrays.asList(numberArray)));
    }

    /**
     * добавляет объект в arrayList
     *
     * @param obj объект для добавления
     */

    void addObject(T obj) {
        if (!getArrayList().contains(obj)) {
            getArrayList().add(obj);
        }
    }

    /**
     * суммирует элементы arrayList, возвращает сумму
     *
     * @return сумма элементов arrayList
     */
    double summator() {
        double sum = 0;
        for (Object temp : getArrayList()) {
            sum += ((Number) temp).doubleValue();
        }
        return sum;
    }

    /**
     * делит элементы arrayList, элементы заменяются результатом деления
     *
     * @param divisor - делитель элементов arrayList
     */
    void splitter(T divisor) {
        for (int i = 0; i < getArrayList().size(); i++) {
            getArrayList().set(i, (T) new Double(getArrayList().get(i).doubleValue() / divisor.doubleValue()));
        }
    }

    /**
     * возвращает хешкод объекта класса MathBox. Для расчетов используется коллекция
     * arrayList, принадлежащая объекту.
     *
     * @return хешкод объекта MathBox
     */
    @Override
    public int hashCode() {
        int code = Integer.hashCode(getArrayList().size());
        for (int i = 0; i < getArrayList().size(); i++) {
            code = 37 * code + Double.hashCode(((Number) getArrayList().get(i)).doubleValue());

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
        List<Object> objToArrayList;
        if (this == obj) return true;
        if (!(obj instanceof MathBox)) return false;
        objToArrayList = ((MathBox) obj).getArrayList();
        if (getArrayList().size() != objToArrayList.size()) return false;
        for (int i = 0; i < getArrayList().size(); i++) {
            if (((Number) getArrayList().get(i)).doubleValue() != ((Number) objToArrayList.get(i)).doubleValue())
                return false;
        }
        return true;
    }

    /**
     * удаление элемента из arrayList. (можно было просто напрямую использовать функцию
     * deleteObject)
     */
    boolean deleteNumber(int number) {
        return deleteObject(number);
    }
}
