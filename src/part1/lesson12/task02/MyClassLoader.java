package part1.lesson12.task02;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Специальный загрузчик класса
 */
public class MyClassLoader extends ClassLoader {
    private List<Class<?>> classList;
    /**
     * Конструктор. Получаем список для хранения объектов класса Class (возвращаемых defineClass() )
     */
    MyClassLoader(List<Class<?>> classList) {
        this.classList = classList;
    }
    /**
     * Переопределенный метод ClassLoader, для имени класса part1.lesson12.task02.SomeClass получаем экземпляр класса
     * Class из файла "part1/lesson12/task02/SomeClass.class".
     */
    @Override
    public Class<?> loadClass(String classNameWithPackage) throws ClassNotFoundException {
        if (classNameWithPackage.equals("part1.lesson12.task02.SomeClass")) {
            try {
                byte[] classData = Files.readAllBytes(Paths.get(getResource("part1/lesson12/task02/SomeClass.class").toURI()));
                Class<?> c = defineClass(classNameWithPackage, classData, 0, classData.length);
                classList.add(c);
                return c;
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return super.loadClass(classNameWithPackage);
    }
}

