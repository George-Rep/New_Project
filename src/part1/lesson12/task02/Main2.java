package part1.lesson12.task02;

import java.util.ArrayList;
import java.util.List;

/**
 * рекомендуется например -XX:MaxMetaspaceSize=128m
 * Задание 2.  Доработать программу так, чтобы ошибка OutOfMemoryError возникала в Metaspace /Permanent Generation
 */
public class Main2 {
    private List<Class<?>> classList = new ArrayList<>();
    private int index;

    /**
     * в цикле запускаем метод loadMyClass().
     */
    public static void main(String[] args) {
        Main2 classGenerator = new Main2();
        System.out.println("Старт");
        while (true) {
            classGenerator.loadMyClass();
        }
    }
    /**
     * Загружаем один и тот же класс SomeClass, с помощью загрузчика MyClassLoader. Добавляем полученный объект
     * класса Class в список classList, каждый пятый объект удаляем из списка.
     */
    private void loadMyClass() {
        ClassLoader classLoader = new MyClassLoader(classList);
        try {
            Class.forName("part1.lesson12.task02.SomeClass", true, classLoader);
            index++;
            System.out.println(index);
            if (index % 5 == 0) classList.remove(0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}