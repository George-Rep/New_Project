package part1.lesson09.task01;

import org.apache.commons.jci.compilers.*;
import org.apache.commons.jci.readers.FileResourceReader;
import org.apache.commons.jci.stores.FileResourceStore;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Дан интерфейс
 * <p>
 * public interface Worker {
 * <p>
 * void doWork();
 * <p>
 * }
 * <p>
 * Необходимо написать программу, выполняющую следующее:
 * <p>
 * Программа с консоли построчно считывает код метода doWork. Код не должен требовать импорта дополнительных классов.
 * После ввода пустой строки считывание прекращается и считанные строки добавляются в тело метода
 * public void doWork() в файле SomeClass.java.
 * Файл SomeClass.java компилируется программой (в рантайме) в файл SomeClass.class.
 * Полученный файл подгружается в программу с помощью кастомного загрузчика
 * Метод, введенный с консоли, исполняется в рантайме (вызывается у экземпляра объекта подгруженного класса)
 */
public class Main {
    static final String LINESEPARATOR = System.getProperty("line.separator");
    static final String methodHeader =
            "package part1.lesson09.task01;" + LINESEPARATOR
                    + "public class SomeClass implements Worker {" + LINESEPARATOR
                    + "@Override" + LINESEPARATOR
                    + "public void doWork() {" + LINESEPARATOR;
    static final String methodFooter = "}}";
    static final String outputFile = "./src/part1/lesson09/task01/SomeClass.java";

    /**
     * Требования:
     * Apache Commons: JCI (commons-jci-core-1.1.jar, commons-jci-eclipse-1.1.jar), Logging (commons-logging-1.2.jar),
     * IO (commons-io-2.6.jar). Eclipse JDT Core Batch Compiler (например отсюда
     * http://download.eclipse.org/eclipse/downloads/drops4/R-4.11-201903070500/#JDTCORE)
     * <p>
     * <p>
     * С консоли считываются строки - код метода doWork. Они записываются в "./src/part1/lesson09/task01/SomeClass.java"
     * в тело метода doWork. Такой путь необходим в данной реализации, иначе нужно будет создавать где-то новые каталоги,
     * и компилировать дополнительно и интерфейс Worker. После полного завершения работы программы файл
     * "./src/part1/lesson09/task01/SomeClass.java" удаляется, либо его можно удалить самостоятельно.
     * С помощью Apache JCI файл компилируется (в папку с остальными class файлами пакета). Используется Eclipse компилятор. При успешной компиляции класс из
     * скомпилированного файла подгружается загрузчиком, вызывается метод doWork объекта подгруженного класса
     * (код метода ранее вводился с клавиатуры).
     */
    public static void main(String[] args) {

        ArrayList<String> methodStrings = new ArrayList<>();
        Scanner inputScanner = new Scanner(System.in);
        String str;
        System.out.println("Введите код метода:");
        do {
            str = inputScanner.nextLine();
            methodStrings.add(str);
        }
        while (!str.equals(""));
        inputScanner.close();

        try (
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
        ) {
            bufferedWriter.write(methodHeader);
            for (String s : methodStrings) {
                bufferedWriter.write(s + LINESEPARATOR);
            }
            bufferedWriter.write(methodFooter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Компиляция");
        EclipseJavaCompilerSettings eclipseJavaCompilerSettings = new EclipseJavaCompilerSettings();
        eclipseJavaCompilerSettings.setSourceVersion("1.7");
        JavaCompiler compiler = new EclipseJavaCompiler(eclipseJavaCompilerSettings);

        File targetDir = null;
        URL str2 = Main.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            targetDir = Paths.get(str2.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File sourceDir = new File("./src");
        String[] sources = new String[]{"part1/lesson09/task01/SomeClass.java"};
        CompilationResult result = compiler.compile(sources, new FileResourceReader(sourceDir), new FileResourceStore(targetDir));

        if ((result.getErrors().length == 0) && (result.getWarnings().length == 0)) {
            ClassLoader cl = new MyClassLoader(targetDir.toString() + "\\part1\\lesson09\\task01\\SomeClass.class");
            Class<?> workerClass = null;
            try {
                workerClass = cl.loadClass("part1.lesson09.task01.SomeClass");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Worker newWorker = null;
            try {
                newWorker = (Worker) workerClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            newWorker.doWork();
        } else {
            System.out.println("Ошибка или предупреждение на стадии компиляции");
        }

        (new File("./src/part1/lesson09/task01/SomeClass.java")).delete();
        System.out.println("Завершение работы");
    }
}
