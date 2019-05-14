package part1.lesson08.task02;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * класс с методами
 * void serialize (Object object, String file);
 * Object deSerialize(String file);
 * для "сериализации/десериализации"
 * и вспомогательными методами.
 *
 * @see Main
 */
public class Serializer {
    /**
     * основной метод для сериализации объекта. Рекурсивно вызывает себя для записи состояния объекта, включая
     * значения унаследованных переменных. Также может использоваться для сериализации поля объекта, являющегося
     * объектом.
     *
     * @param dataOutputStream поток для записи
     * @param classOf          один из классов объекта object, т.е. непосредственно класс объекта,
     *                         или один из родительских классов по рекурсии.
     * @param object           объект для записи
     * @throws IllegalAccessException, IOException, обрабатываются в serialize
     */
    void writeData(DataOutputStream dataOutputStream, Class classOf, Object object) throws IllegalAccessException, IOException {

        if (classOf.getSuperclass() != null) {
            writeData(dataOutputStream, classOf.getSuperclass(), object);
        }
        Field[] fields = classOf.getDeclaredFields();

        for (Field declaredField : fields) {
            declaredField.setAccessible(true);
            if (declaredField.getType().isArray()) {
                if (declaredField.get(object) == null) {
                    writeObject(0, dataOutputStream);
                } else {
                    writeObject(1, dataOutputStream);
                    int length = Array.getLength(declaredField.get(object));
                    writeObject(length, dataOutputStream);
                    for (int i = 0; i < length; i++) {
                        Object arrayElement = Array.get(declaredField.get(object), i);
                        //System.out.println(arrayElement);
                        if (arrayElement == null) {
                            writeObject(0, dataOutputStream);
                        } else {
                            writeObject(1, dataOutputStream);
                            writeObject(arrayElement, dataOutputStream);
                        }

                    }
                }
            } else {
                writeField(declaredField, dataOutputStream, object);
            }
        }
    }

    /**
     * метод для сериализации поля объекта. Примитивы и строки записываются сразу,
     * для ссылочных значений, если они не равны null, вызывается метод writeData.
     * Ранее он вызывался для объекта в целом, сейчас для поля объекта, являющегося объектом.
     *
     * @param field            поле объекта object
     * @param dataOutputStream поток для записи
     * @param object           объект для записи
     * @throws IllegalAccessException, IOException, обрабатываются в serialize
     */
    void writeField(Field field, DataOutputStream dataOutputStream, Object object) throws IllegalAccessException, IOException {
        String simpleName = field.getType().getSimpleName();
        if (simpleName.equals("byte")) {
            dataOutputStream.writeByte(field.getByte(object));
            return;
        }
        if (simpleName.equals("short")) {
            dataOutputStream.writeShort(field.getShort(object));
            return;
        }
        if (simpleName.equals("int")) {
            dataOutputStream.writeInt(field.getInt(object));
            return;
        }
        if (simpleName.equals("long")) {
            dataOutputStream.writeLong(field.getLong(object));
            return;
        }
        if (simpleName.equals("float")) {
            dataOutputStream.writeFloat(field.getFloat(object));
            return;
        }
        if (simpleName.equals("double")) {
            dataOutputStream.writeDouble(field.getDouble(object));
            return;
        }
        if (simpleName.equals("char")) {
            dataOutputStream.writeChar(field.getChar(object));
            return;
        }
        if (simpleName.equals("boolean")) {
            dataOutputStream.writeBoolean(field.getBoolean(object));
            return;
        }
        if (simpleName.equals("String")) {
            dataOutputStream.writeUTF((String) field.get(object));
            return;
        }

        Object object2 = field.get(object);
        if (object2 == null) {
            writeObject(0, dataOutputStream);
        } else {
            writeObject(1, dataOutputStream);
            writeData(dataOutputStream, object2.getClass(), object2);
        }

    }

    /**
     * метод для сериализации элемента массива. Также может быть использован,
     * чтобы записать число.
     * Примитивы и строки записываются сразу, обертки над примитивами записываются
     * как примитивы.
     * для ссылочных значений вызывается метод writeData.
     * Ранее он вызывался для объекта в целом, сейчас для части объекта, являщейся объектом.
     *
     * @param object           объект для записи
     * @param dataOutputStream поток для записи
     * @throws IOException, IllegalAccessException, обрабатываются в serialize
     */
    void writeObject(Object object, DataOutputStream dataOutputStream) throws IOException, IllegalAccessException {
        String simpleName = object.getClass().getSimpleName();

        if ((simpleName.equals("byte")) || (simpleName.equals("Byte"))) {
            dataOutputStream.writeByte((byte) object);
            return;
        }
        if ((simpleName.equals("short")) || (simpleName.equals("Short"))) {
            dataOutputStream.writeShort((short) object);
            return;
        }
        if ((simpleName.equals("int")) || (simpleName.equals("Integer"))) {
            dataOutputStream.writeInt((int) object);
            return;
        }
        if ((simpleName.equals("long")) || (simpleName.equals("Long"))) {
            dataOutputStream.writeLong((long) object);
            return;
        }
        if ((simpleName.equals("float")) || (simpleName.equals("Float"))) {
            dataOutputStream.writeFloat((float) object);
            return;
        }
        if ((simpleName.equals("double")) || (simpleName.equals("Double"))) {
            dataOutputStream.writeDouble((double) object);
            return;
        }
        if ((simpleName.equals("char")) || (simpleName.equals("Char"))) {
            dataOutputStream.writeChar((char) object);
            return;
        }
        if ((simpleName.equals("boolean")) || (simpleName.equals("Boolean"))) {
            dataOutputStream.writeBoolean((boolean) object);
            return;
        }
        if (simpleName.equals("String")) {
            dataOutputStream.writeUTF((String) object);
            return;
        }
        writeData(dataOutputStream, object.getClass(), object);

    }

    /**
     * метод для десериализации элемента массива. Также может быть использован,
     * чтобы прочитать число из файла сериализации.
     * Примитивы (возвращаются в обертках, а считываются как примитивы) и строки считываются сразу,
     * для ссылочных значений вызывается метод readData.
     * Ранее он вызывался для объекта в целом, сейчас для части объекта, являющейся объектом.
     *
     * @param object          объект для чтения.
     * @param dataInputStream поток для чтения
     * @return прочитанный из файла сериализации объект
     * @throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException,
     *                      обрабатываются в deSerialize
     */
    Object readObject(Object object, DataInputStream dataInputStream) throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        String simpleName = object.getClass().getSimpleName();
        Object returnObject;

        if ((simpleName.equals("byte")) || (simpleName.equals("Byte"))) {
            returnObject = dataInputStream.readByte();
            return returnObject;
        }
        if ((simpleName.equals("short")) || (simpleName.equals("Short"))) {
            returnObject = dataInputStream.readShort();
            return returnObject;
        }
        if ((simpleName.equals("int")) || (simpleName.equals("Integer"))) {
            returnObject = dataInputStream.readInt();
            return returnObject;
        }
        if ((simpleName.equals("long")) || (simpleName.equals("Long"))) {
            returnObject = dataInputStream.readLong();
            return returnObject;
        }
        if ((simpleName.equals("float")) || (simpleName.equals("Float"))) {
            returnObject = dataInputStream.readFloat();
            return returnObject;
        }
        if ((simpleName.equals("double")) || (simpleName.equals("Double"))) {
            returnObject = dataInputStream.readDouble();
            return returnObject;
        }
        if ((simpleName.equals("char")) || (simpleName.equals("Char"))) {
            returnObject = dataInputStream.readChar();
            return returnObject;

        }
        if ((simpleName.equals("boolean")) || (simpleName.equals("Boolean"))) {
            returnObject = dataInputStream.readBoolean();
            return returnObject;
        }
        if (simpleName.equals("String")) {
            returnObject = dataInputStream.readUTF();
            return returnObject;
        }

        readData(dataInputStream, object.getClass(), object);
        return object;

    }

    /**
     * метод для десериализации поля объекта. Примитивы и строки считываются сразу,
     * для ссылочных значений, если они не равны null, вызывается метод readData.
     * Ранее он вызывался для объекта в целом, сейчас для поля объекта, являющегося объектом.
     *
     * @param field           поле объекта object
     * @param dataInputStream поток для чтения
     * @param object          объект для чтения
     * @throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException,
     *                      обрабатываются в deSerialize
     */
    void readField(Field field, DataInputStream dataInputStream, Object object) throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        String simpleName = field.getType().getSimpleName();
        int nullVar = 0;
        if (simpleName.equals("byte")) {
            field.setByte(object, dataInputStream.readByte());
            return;
        }
        if (simpleName.equals("short")) {
            field.setShort(object, dataInputStream.readShort());
            return;
        }
        if (simpleName.equals("int")) {
            field.setInt(object, dataInputStream.readInt());
            return;
        }
        if (simpleName.equals("long")) {
            field.setLong(object, dataInputStream.readLong());
            return;
        }
        if (simpleName.equals("float")) {
            field.setFloat(object, dataInputStream.readFloat());
            return;
        }
        if (simpleName.equals("double")) {
            field.setDouble(object, dataInputStream.readDouble());
            return;
        }
        if (simpleName.equals("char")) {
            field.setChar(object, dataInputStream.readChar());
            return;
        }
        if (simpleName.equals("boolean")) {
            field.setBoolean(object, dataInputStream.readBoolean());
            return;
        }
        if (simpleName.equals("String")) {
            field.set(object, dataInputStream.readUTF());
            return;
        }

        nullVar = (int) readObject(nullVar, dataInputStream);
        if (nullVar == 0) {
            return;
        } else {
            Object object2 = field.getType().getConstructor().newInstance();
            field.set(object, object2);
            //=field.get(object);
            readData(dataInputStream, object2.getClass(), object2);
        }

    }

    /**
     * основной метод для десериализации объекта. Рекурсивно вызывает себя для считывания из файла состояния объекта, включая
     * значения унаследованных переменных. Также может использоваться для десериализации поля объекта, являющегося
     * объектом.
     *
     * @param dataInputStream поток для чтения
     * @param classOf         один из классов объекта object, т.е. непосредственно класс объекта,
     *                        или один из родительских классов по рекурсии.
     * @param object          объект для чтения.
     * @throws IOException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException,
     *                      обрабатываются в serialize
     */
    void readData(DataInputStream dataInputStream, Class classOf, Object object) throws IOException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        int nullVar = 0;
        if (classOf.getSuperclass() != null) {
            readData(dataInputStream, classOf.getSuperclass(), object);
        }

        Field[] fields = classOf.getDeclaredFields();
        for (Field declaredField : fields) {

            declaredField.setAccessible(true);

            if (declaredField.getType().isArray()) {

                nullVar = (int) readObject(nullVar, dataInputStream);
                if (nullVar == 0) {
                } else {
                    int length = 0;
                    length = (int) readObject(length, dataInputStream);
                    declaredField.set(object, Array.newInstance(declaredField.getType().getComponentType(), length));

                    for (int i = 0; i < length; i++) {
                        nullVar = (int) readObject(nullVar, dataInputStream);
                        if (nullVar == 0) {
                        } else {
                            if (!declaredField.getType().getComponentType().isPrimitive()) {
                                Array.set(declaredField.get(object), i, declaredField.getType().getComponentType().getConstructor().newInstance());
                            }
                            Object arrayElement = Array.get(declaredField.get(object), i);
                            arrayElement = readObject(arrayElement, dataInputStream);
                            Array.set(declaredField.get(object), i, arrayElement);
                        }
                    }
                }
            } else {
                readField(declaredField, dataInputStream, object);
            }

            declaredField.setAccessible(false);
        }
    }

    /**
     * метод (из условия задачи) для сериализации объекта.
     * Вызывает writeData - основной метод сериализации.
     *
     * @param object объект для сериализации.
     * @param file   файл сериализации (запись)
     */
    void serialize(Object object, String file) {
        Class classOf = object.getClass();

        try (DataOutputStream dataOutputStream =
                     new DataOutputStream(new FileOutputStream(file))) {
            dataOutputStream.writeUTF(object.getClass().getName());
            writeData(dataOutputStream, classOf, object);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод (из условия задачи) для десериализации объекта.
     * Вызывает readData - основной метод десериализации.
     * Возвращает десериализованный объект.
     *
     * @param file файл сериализации (чтение)
     * @return десериализованный объект
     */
    Object deSerialize(String file) {
        String className;
        Object returnObject = null;
        Class classOf;
        try (DataInputStream dataInputStream =
                     new DataInputStream(new FileInputStream(file))) {

            className = dataInputStream.readUTF();
            Class<?> objectClass = Class.forName(className);
            returnObject = objectClass.getConstructor().newInstance();

            classOf = returnObject.getClass();
            readData(dataInputStream, classOf, returnObject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnObject;
    }
}
