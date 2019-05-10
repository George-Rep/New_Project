package part1.lesson06.task01;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Написать программу, читающую текстовый файл. Программа должна составлять отсортированный по алфавиту список слов,
 * найденных в файле и сохранять его в файл-результат. Найденные слова не должны повторяться, регистр не должен
 * учитываться. Одно слово в разных падежах – это разные слова.
 */
public class Main {
    static final String LINESEPARATOR = System.getProperty("line.separator");
    /**
     * В консоли вводятся имена файлов. Для безопасности упрощенно сделано, чтобы нельзя было перезаписать
     * существующий файл. Строки из файла делятся методом String.split(), регулярное выражение.
     * Сделано так, чтобы апострофы и дефисы не рассматривались как разделители слов, при этом дефис
     * в первом или последнем символе слова удаляется. Рассчитано на обычные текстовые файлы в т.ч.
     * с буквами русского алфавита (в кодировке "Windows-1251")
     *
     */
    public static void main(String[] args) {
        String path; //= "sample.txt";
        BufferedReader reader;
        HashSet<String> hashSet = new HashSet<>();
        TreeSet<String> treeSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        Scanner inputScanner = new Scanner(System.in);
        BufferedWriter bufferedWriter=null;

        System.out.print("Введите имя файла для чтения:");
        path = inputScanner.nextLine();
        File inputFile = new File(path);
        if (!inputFile.isFile()) return;
        System.out.print("Введите имя файла для записи:");
        path = inputScanner.nextLine();
        File outputFile = new File(path);
        if (outputFile.isFile()) {
            System.out.print("Файл с таким именем уже существует. Нужно задать другое имя файла");
            return;
        }

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "Windows-1251"));
            String line = reader.readLine();
            if (line != null) {
                do {
                    if (!line.equals("")) {
                        String[] arr = line.split("[ \\s!\"#$%&()*+,./:;<=>?@\\[\\]^_{|}~…«»\\xA0№—]+");

                        for (String arrStr : arr) {
                            String arrStr2 = null;
                            if (arrStr.length() > 1) {

                                if (arrStr.charAt(0) == '-') {
                                    arrStr2 = arrStr.substring(1);
                                } else if (arrStr.charAt(arrStr.length() - 1) == '-') {
                                    arrStr2 = arrStr.substring(0, arrStr.length() - 1);
                                } else arrStr2 = arrStr;
                            } else arrStr2 = arrStr;
                            if ((!arrStr.equals("-")) && (!arrStr.equals("")))
                                hashSet.add(arrStr2);
                        }
                    }
                    line = reader.readLine();
                } while (line != null);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        treeSet.addAll(hashSet);
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "Windows-1251"));
            for (String s : treeSet) {
                bufferedWriter.write(s + LINESEPARATOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try
            {
                if (bufferedWriter != null)
                    bufferedWriter.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
