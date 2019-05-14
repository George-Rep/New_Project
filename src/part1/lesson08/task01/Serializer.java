package part1.lesson08.task01;

import java.io.*;
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
     * значения унаследованных переменных.
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
            String simpleName = declaredField.getType().getSimpleName();
            declaredField.setAccessible(true);
            if (simpleName.equals("byte")) {
                dataOutputStream.writeByte(declaredField.getByte(object));
            }
            if (simpleName.equals("short")) {
                dataOutputStream.writeShort(declaredField.getShort(object));
            }
            if (simpleName.equals("int")) {
                dataOutputStream.writeInt(declaredField.getInt(object));
            }
            if (simpleName.equals("long")) {
                dataOutputStream.writeLong(declaredField.getLong(object));
            }
            if (simpleName.equals("float")) {
                dataOutputStream.writeFloat(declaredField.getFloat(object));
            }
            if (simpleName.equals("double")) {
                dataOutputStream.writeDouble(declaredField.getDouble(object));
            }
            if (simpleName.equals("char")) {
                dataOutputStream.writeChar(declaredField.getChar(object));
            }
            if (simpleName.equals("boolean")) {
                dataOutputStream.writeBoolean(declaredField.getBoolean(object));
            }
            if (simpleName.equals("String")) {
                dataOutputStream.writeUTF((String) declaredField.get(object));
            }
            declaredField.setAccessible(false);
        }
    }

    /**
     * основной метод для десериализации объекта. Рекурсивно вызывает себя для считывания из файла состояния объекта, включая
     * значения унаследованных переменных.
     *
     * @param dataInputStream поток для чтения
     * @param classOf         один из классов объекта object, т.е. непосредственно класс объекта,
     *                        или один из родительских классов по рекурсии.
     * @param object          объект для чтения.
     * @throws IOException, IllegalAccessException,
     *                      обрабатываются в serialize
     */
    void readData(DataInputStream dataInputStream, Class classOf, Object object) throws IOException, IllegalAccessException {

        if (classOf.getSuperclass() != null) {
            readData(dataInputStream, classOf.getSuperclass(), object);
        }

        Field[] fields = classOf.getDeclaredFields();
        for (Field declaredField : fields) {
            String simpleName = declaredField.getType().getSimpleName();
            declaredField.setAccessible(true);
            if (simpleName.equals("byte")) {
                declaredField.setByte(object, dataInputStream.readByte());
            }
            if (simpleName.equals("short")) {
                declaredField.setShort(object, dataInputStream.readShort());
            }
            if (simpleName.equals("int")) {
                declaredField.setInt(object, dataInputStream.readInt());
            }
            if (simpleName.equals("long")) {
                declaredField.setLong(object, dataInputStream.readLong());
            }
            if (simpleName.equals("float")) {
                declaredField.setFloat(object, dataInputStream.readFloat());
            }
            if (simpleName.equals("double")) {
                declaredField.setDouble(object, dataInputStream.readDouble());
            }
            if (simpleName.equals("char")) {
                declaredField.setChar(object, dataInputStream.readChar());
            }
            if (simpleName.equals("boolean")) {
                declaredField.setBoolean(object, dataInputStream.readBoolean());
            }
            if (simpleName.equals("String")) {
                declaredField.set(object, dataInputStream.readUTF());
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
        String test;
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
