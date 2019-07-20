package package1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Специальный загрузчик класса
 */
public class MyClassLoader extends ClassLoader {
    private String pathToClass;

    MyClassLoader() {
    }

    /**
     * @param pathToClass путь к скомпилированному файлу SomeClass.class (включает имя)
     */
    MyClassLoader(String pathToClass) {
        this.pathToClass = pathToClass;
    }

//    @Override
//    public Class<?> loadClass(String name) throws ClassNotFoundException {
//        if ("part1.lesson09.task01.SomeClass".equals(name)) {
//           return findClass(name);
//       }
//        return super.loadClass(name);
//    }

    /**
     * инстанцируем класс package1.SomeClass, байт-код читается из файла .class
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (Main.getResourceBundleString("className").equals(name)) {
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(pathToClass));
                return defineClass(name, bytes, 0, bytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.findClass(name);
    }
}
