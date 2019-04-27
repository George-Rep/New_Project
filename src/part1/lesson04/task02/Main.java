package part1.lesson04.task02;


import java.util.Scanner;

/**
 * Создать класс ObjectBox, который будет хранить коллекцию Object.
 * У класса должен быть метод addObject, добавляющий объект в коллекцию.
 * У класса должен быть метод deleteObject, проверяющий наличие объекта в коллекции и при наличии удаляющий его.
 * Должен быть метод dump, выводящий содержимое коллекции в строку.
 */
public class Main {
    /**
     * создается объект ObjectBox, результаты выполнения методов addObject и deleteObject
     * демонстрируются на экране с помощью метода dump
     */
    public static void main(String[] args) {
        ObjectBox objectBox = new ObjectBox();

        objectBox.addObject(new Scanner(System.in));
        objectBox.addObject(objectBox);
        objectBox.addObject(4);
        System.out.println("После добавления");
        System.out.println(objectBox.dump());

        objectBox.deleteObject(objectBox);
        System.out.println();
        System.out.println("После удаления");
        System.out.println(objectBox.dump());


    }
}
