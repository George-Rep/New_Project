package package1;

import org.apache.commons.jci.compilers.CompilationResult;
import org.apache.commons.jci.compilers.EclipseJavaCompiler;
import org.apache.commons.jci.compilers.EclipseJavaCompilerSettings;
import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.readers.FileResourceReader;
import org.apache.commons.jci.stores.FileResourceStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
    static ResourceBundle resource = ResourceBundle.getBundle("demo");
    static final String LINESEPARATOR = System.getProperty("line.separator");
    static final String methodHeader =
            resource.getString("methodHeader.1") + LINESEPARATOR
                    + resource.getString("methodHeader.2") + LINESEPARATOR
                    + resource.getString("methodHeader.3") + LINESEPARATOR
                    + resource.getString("methodHeader.4") + LINESEPARATOR;
    static final String methodFooter = resource.getString("methodFooter");
    static final String outputFile = resource.getString("outputFile");

    static public String getResourceBundleString(String key) {
        return new String(resource.getString(key).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    static void input() {
        ArrayList<String> methodStrings = new ArrayList<>();
        Scanner inputScanner = new Scanner(System.in);
        String str;
        System.out.println(getResourceBundleString("enterString"));
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
    }

    static CompilationResult compilation(File targetDir) {
        System.out.println(getResourceBundleString("compilationString"));
        EclipseJavaCompilerSettings eclipseJavaCompilerSettings = new EclipseJavaCompilerSettings();
        eclipseJavaCompilerSettings.setSourceVersion(getResourceBundleString("version"));
        JavaCompiler compiler = new EclipseJavaCompiler(eclipseJavaCompilerSettings);


        File sourceDir = new File(getResourceBundleString("sourceDir"));
        String[] sources = new String[]{getResourceBundleString("sources")};
        return compiler.compile(sources, new FileResourceReader(sourceDir), new FileResourceStore(targetDir));

    }

    /**
     * <p>
     * <p>
     * С консоли считываются строки - код метода doWork. Они записываются в "./src/main/java/package1/SomeClass.java"
     * в тело метода doWork. Такой путь необходим в данной реализации, иначе нужно будет создавать где-то новые каталоги,
     * и компилировать дополнительно и интерфейс Worker. После полного завершения работы программы файл
     * "./src/main/java/package1/SomeClass.java" удаляется, либо его можно удалить самостоятельно.
     * С помощью Apache JCI файл компилируется (в папку с остальными class файлами пакета). Используется Eclipse компилятор. При успешной компиляции класс из
     * скомпилированного файла подгружается загрузчиком, вызывается метод doWork объекта подгруженного класса
     * (код метода ранее вводился с клавиатуры).
     */
    public static void main(String[] args) {
        input();
        File targetDir = null;
        try {
            URL str2 = Main.class.getProtectionDomain().getCodeSource().getLocation();
            targetDir = Paths.get(str2.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        CompilationResult result = compilation(targetDir);
        if ((result.getErrors().length == 0) && (result.getWarnings().length == 0)) {
            ClassLoader cl = new MyClassLoader(targetDir.toString() + getResourceBundleString("pathToClass"));
            Class<?> workerClass = null;
            try {
                workerClass = cl.loadClass(getResourceBundleString("className"));
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
            System.out.println(getResourceBundleString("errorString"));
        }
        (new File(getResourceBundleString("outputFile"))).delete();
        System.out.println(getResourceBundleString("endWorkString"));
    }
}
