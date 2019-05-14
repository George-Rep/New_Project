package part1.lesson08.task01;

/**
 * Необходимо разработать класс, реализующий следующие методы:
 * void serialize (Object object, String file);
 * Object deSerialize(String file);
 * Методы выполняют сериализацию объекта Object в файл file и десериализацию объекта из этого файла.
 * Обязательна сериализация и десериализация "плоских" объектов (все поля объекта - примитивы, или String).
 */
public class Main {
    /**
     * для "сериализации/десериализации" используется файл "serial.bin" в корне проекта.
     * Для демонстрации объект SomeClass наполняется значениями, затем он
     * с помощью Serializer.serialize сериализуется, затем
     * в уже другую переменную десериализуется с помощью Serializer.deSerialize
     * При десериализации предполагается, что для вновь создаваемых объектов есть
     * конструкторы без параматров. Игнорируются геттеры и сеттеры, и все значения
     * упрощенно сохраняются при сериализации и восстанавливаются при десериализации.
     * Не учитываются static и transient
     */
    public static void main(String[] args) {
        String file = "serial.bin";
        SomeClass someClass = new SomeClass(), someClass2;
        someClass.setB((byte) 1);
        someClass.setC('f');
        someClass.setI(2);
        someClass.setL(3L);
        someClass.setD(4.1);
        someClass.setF(5.2f);
        someClass.setU("Сериал");
        someClass.setI2(22);
        System.out.println("До сериализации");
        try {
            someClass.printData(SomeClass.class, someClass);
            Serializer serializer = new Serializer();
            serializer.serialize(someClass, file);
            System.out.println("-----");
            System.out.println("Посде десериализации");
            someClass2 = (SomeClass) serializer.deSerialize(file);
            someClass2.printData(SomeClass.class, someClass2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
