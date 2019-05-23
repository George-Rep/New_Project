package part1.lesson12.task01;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * рекомендуется -Xmx50m или меньше
 * Необходимо создать программу, которая продемонстрирует утечку памяти в Java. При этом объекты должны не только
 * создаваться, но и периодически частично удаляться, чтобы GC имел возможность очищать часть памяти. Через некоторое
 * время программа должна завершиться с ошибкой OutOfMemoryError c пометкой Java Heap Space.
 */
public class Main1 {
    /**
     * рекомендуется -Xmx50m или меньше
     * список list наполняется строками из одного символа, каждая пятая строка затем удаляется из списка
     */
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        Random random = new Random();
        try {
            for (int i = 0; i < 100000000; i++) {
                String str = "" + random.nextInt();
                list.add(str);
                if (i % 5 == 0) {
                    Thread.sleep(1);
                    list.remove(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
