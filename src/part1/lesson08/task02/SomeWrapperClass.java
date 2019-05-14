package part1.lesson08.task02;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * демонстрационный класс. Включает методы для
 * демонстрационного вывода состояния объекта на консоль.
 */
public class SomeWrapperClass {

    private SomeClass[] classVar;

    public SomeClass[] getClassVar() {
        return classVar;
    }

    public void setClassVar(SomeClass[] classVar) {
        this.classVar = classVar;
    }

    /**
     * метод для вывода элемента массива.
     * Примитивы и строки выводятся сразу
     * для ссылочных значений вызывается метод printData.
     *
     * @param object объект для вывода
     * @throws IllegalAccessException, обрабатывается в main
     */
    void printObject(Object object) throws IllegalAccessException {
        String simpleName = object.getClass().getSimpleName();

        if ((simpleName.equals("byte")) || (simpleName.equals("Byte"))
                || (simpleName.equals("short")) || (simpleName.equals("Short"))
                || (simpleName.equals("int")) || (simpleName.equals("Integer"))
                || (simpleName.equals("long")) || (simpleName.equals("Long"))
                || (simpleName.equals("float")) || (simpleName.equals("Float"))
                || (simpleName.equals("double")) || (simpleName.equals("Double"))
                || (simpleName.equals("char")) || (simpleName.equals("Char"))
                || (simpleName.equals("boolean")) || (simpleName.equals("Boolean"))
                || (simpleName.equals("String"))
        ) {
            System.out.println(object);
            return;
        }
        printData(object.getClass(), object);

    }

    /**
     * метод для вывода на консоль поля объекта. Примитивы и строки записываются сразу,
     * для ссылочных значений, если они не равны null, вызывается метод printData.
     *
     * @param field  поле объекта object
     * @param object объект для вывода
     * @throws IllegalAccessException, обрабатывается в main
     */
    void printField(Field field, Object object) throws IllegalAccessException {
        String simpleName = field.getType().getSimpleName();

        if ((simpleName.equals("byte"))
                || (simpleName.equals("short"))
                || (simpleName.equals("int"))
                || (simpleName.equals("long"))
                || (simpleName.equals("float"))
                || (simpleName.equals("double"))
                || (simpleName.equals("char"))
                || (simpleName.equals("boolean"))
                || (simpleName.equals("String"))
        ) {

            System.out.print(
                    Modifier.toString(field.getModifiers()) + " " +
                            field.getType().getSimpleName() + " " +
                            field.getName() + ": ");
            field.setAccessible(true);
            System.out.println(field.get(object));
            return;
        }

        Object object2 = field.get(object);
        if (object2 == null) {
            System.out.println("null");
        } else {
            printData(object2.getClass(), object2);
        }
    }

    /**
     * основной метод для вывода на консоль состояния объекта. Рекурсивно вызывает себя для вывода состояния объекта, включая
     * значения унаследованных переменных.
     *
     * @param classOf один из классов объекта object, т.е. непосредственно класс объекта,
     *                или один из родительских классов по рекурсии.
     * @param object  объект для вывода
     * @throws IllegalAccessException, обрабатывается в main
     */
    void printData(Class classOf, Object object) throws IllegalAccessException {
        if (classOf.getSuperclass() != null) {
            printData(classOf.getSuperclass(), object);
        }

        Field[] fields = classOf.getDeclaredFields();
        if (fields.length > 0) {
            System.out.println("\r\n" + classOf.getSimpleName());

        }
        for (Field declaredField : fields) {
            declaredField.setAccessible(true);
            if (declaredField.getType().isArray()) {
                System.out.println(
                        Modifier.toString(declaredField.getModifiers()) + " " +
                                declaredField.getType().getSimpleName() + " " +
                                declaredField.getName() + ": ");


                if (declaredField.get(object) == null) {
                    System.out.println("null");
                } else {
                    int length = Array.getLength(declaredField.get(object));

                    for (int i = 0; i < length; i++) {
                        Object arrayElement = Array.get(declaredField.get(object), i);
                        String arrayName = declaredField.getName();
                        System.out.print(arrayName + " " + i + ": ");
                        if (arrayElement == null) {
                            System.out.println("null");
                        } else {
                            printObject(arrayElement);
                        }
                    }
                }
            } else {
                printField(declaredField, object);
            }
            declaredField.setAccessible(false);
        }
    }
}
