package part1.lesson04.task02;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @see Main
 */
public class ObjectBox {
   private ArrayList<Object> arrayList = new ArrayList<>();

    /**
     * возвращает текстовое представление объекта. Между элементами
     * выводится сивол новой строки
     *
     * @return текстовое представление объекта
     */
    String dump() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Object> iterator = arrayList.iterator();
        if (iterator.hasNext()) stringBuilder.append(iterator.next());
        while (iterator.hasNext()) {
            stringBuilder.append(lineSeparator).append(iterator.next());
        }
        return stringBuilder.toString();
    }

    void addObject(Object obj) {
        if (!arrayList.contains(obj)) {
            arrayList.add(obj);
        }
    }

    /**
     * метод удаления из arrayList элемента, заданного в параметре.
     *
     * @param obj объект, которое нужно удалить из arrayList
     * @return true, если элемент был в коллекции и был удален
     */
    boolean deleteObject(Object obj) {
        Iterator iterator = arrayList.iterator();
        Object obj2;
        while (iterator.hasNext()) {
            obj2 = (Object) iterator.next();
            if (obj2.equals(obj)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
