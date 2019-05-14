package part1.lesson08.task01;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * демонстрационный класс, также класс SomeClassParent ниже.
 */
public class SomeClass extends SomeClassParent {

    private byte b;
    private char c;
    private int i2;
    private long l;
    private double d;
    private float f;
    private String u;

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

            System.out.print(
                    Modifier.toString(declaredField.getModifiers()) + " " +
                            declaredField.getType().getSimpleName() + " " +
                            declaredField.getName() + ": ");

            System.out.println(declaredField.get(object));

            declaredField.setAccessible(false);
        }
    }

    public byte getB() {
        return b;
    }

    public char getC() {
        return c;
    }

    public int getI() {
        return i2;
    }

    public long getL() {
        return l;
    }

    public double getD() {
        return d;
    }

    public float getF() {
        return f;
    }

    public String getU() {
        return u;
    }


    public void setB(byte b) {
        this.b = b;
    }

    public void setC(char c) {
        this.c = c;
    }

    public void setI(int i2) {
        this.i2 = i2;
    }

    public void setL(long l) {
        this.l = l;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void setF(float f) {
        this.f = f;
    }

    public void setU(String u) {
        this.u = u;
    }

}

class SomeClassParent {
    private int i2;

    public int getI2() {
        return i2;
    }

    public void setI2(int i2) {
        this.i2 = i2;
    }

}