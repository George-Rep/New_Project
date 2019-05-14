package part1.lesson08.task02;


/**
 * Предусмотреть работу c любыми типами полей (полями могут быть ссылочные типы).
 * <p>
 * Требование: Использовать готовые реализации (Jaxb, jackson и т.д.) запрещается.
 * см. task01
 */
public class Main {
    /**
     * для "сериализации/десериализации" используется файл "serial.bin" в корне проекта.
     * Для демонстрации объект SomeWrapperClass наполняется значениями, затем он
     * с помощью Serializer.serialize сериализуется, затем
     * в уже другую переменную десериализуется с помощью Serializer.deSerialize
     * На данный момент не реализована работа с Collection,Map.
     * Реализована работа с массивами и null значениямии.
     * Реализована работа с ссылочными типами, которые при рекурсивном обходе построены
     * из примитивов или String (также в комбинации с массивами).
     * При десериализации предполагается, что для вновь создаваемых объектов есть
     * конструкторы без параматров. Игнорируются геттеры и сеттеры, и все значения
     * упрощенно сохраняются при сериализации и восстанавливаются при десериализации.
     * Не учитываются static и transient
     */
    public static void main(String[] args) {
        String file = "serial.bin";
        SomeClass someClass = new SomeClass();
        SomeWrapperClass someWrapperClass = new SomeWrapperClass(), someWrapperClass2;
        someClass.setB((byte) 1);
        someClass.setC('f');
        someClass.setI(2);
        someClass.setL(3L);
        someClass.setD(4.1);
        someClass.setF(5.2f);
        someClass.setU("Сериал");
        someClass.setI2(22);
        someClass.setArrayVar(new int[5]);
        someClass.getArrayVar()[2] = 4;
        someWrapperClass.setClassVar(new SomeClass[2]);
        someWrapperClass.getClassVar()[0] = someClass;
        someClass = new SomeClass();
        someClass.setB((byte) 10);
        someClass.setC('h');
        someClass.setI(20);
        someClass.setL(30L);
        someClass.setD(40.1);
        someClass.setF(50.2f);
        someClass.setU("Сериал2");
        someClass.setI2(23);
        someClass.setArrayVar(new int[6]);
        someClass.getArrayVar()[4] = 4;
        someWrapperClass.getClassVar()[1] = someClass;
        System.out.println("\r\nДо сериализации\r\n");
        try {
            someWrapperClass.printData(SomeWrapperClass.class, someWrapperClass);
            Serializer serializer = new Serializer();
            serializer.serialize(someWrapperClass, file);
            someWrapperClass2 = (SomeWrapperClass) serializer.deSerialize(file);
            System.out.println("-----");
            System.out.println("\r\nПосле десериализации\r\n");
            someWrapperClass2.printData(SomeWrapperClass.class, someWrapperClass2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
