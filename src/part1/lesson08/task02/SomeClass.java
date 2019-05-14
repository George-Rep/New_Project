package part1.lesson08.task02;


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
    private int[] arrayVar;

    public int getI2() {
        return i2;
    }

    public void setI2(int i2) {
        this.i2 = i2;
    }

    public int[] getArrayVar() {
        return arrayVar;
    }

    public void setArrayVar(int[] arrayVar) {
        this.arrayVar = arrayVar;
    }
}