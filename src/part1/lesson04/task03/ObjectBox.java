package part1.lesson04.task03;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @see Main
 */
public class ObjectBox<T> {


    private List<T> arrayList = new ArrayList<>();

    public List<T> getArrayList() {
        return arrayList;
    }

    public void setArrayList(List<T> arrayList) {
        this.arrayList = arrayList;
    }

    /**
     * возвращает текстовое представление объекта. Между элементами
     * выводится сивол новой строки
     *
     * @return текстовое представление объекта
     */
    String dump() {
        System.out.println();
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<T> iterator = arrayList.iterator();
        if (iterator.hasNext()) stringBuilder.append(iterator.next());
        while (iterator.hasNext()) {
            stringBuilder.append(lineSeparator).append(iterator.next());
        }
        return stringBuilder.toString();
    }


    void addObject(T obj) {
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
        while (iterator.hasNext()) {
            if (iterator.next().equals(obj)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    /**
     * возвращает текстовое представление объекта. Между элементами
     * выводится сивол новой строки
     *
     * @return текстовое представление объекта
     */
    @Override
    public String toString() {
        return dump();
    }
}
