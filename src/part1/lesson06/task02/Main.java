package part1.lesson06.task02;

import java.io.*;
import java.util.Scanner;

/**
 * Создать генератор текстовых файлов, работающий по следующим правилам:
 *
 *     Предложение состоит из 1<=n1<=15 слов. В предложении после произвольных слов могут находиться запятые.
 *     Слово состоит из 1<=n2<=15 латинских букв
 *     Слова разделены одним пробелом
 *     Предложение начинается с заглавной буквы
 *     Предложение заканчивается (.|!|?)+" "
 *     Текст состоит из абзацев. в одном абзаце 1<=n3<=20 предложений.
 *     В конце абзаца стоит разрыв строки и перенос каретки.
 *     Есть массив слов 1<=n4<=1000. Есть вероятность probability вхождения одного из слов этого массива
 *     в следующее предложение (1/probability).
 *
 * Необходимо написать метод getFiles(String path, int n, int size, String[] words, int probability), который
 * создаст n файлов размером size в каталоге path. words - массив слов, probability - вероятность.
 */
public class Main {
    static final String LINESEPARATOR = System.getProperty("line.separator");

    /**
     * метод получения положительного числа (int) с консоли
     *
     * @param inputScanner Scanner
     * @return int, положительное число из консоли
     */
    static int scanPositiveInteger(Scanner inputScanner) {
        int number;
        do {
            while (!inputScanner.hasNextInt()) {

                inputScanner.next();
            }
            number = inputScanner.nextInt();
            if (number < 1) {
                System.out.println("положительное целое число:");
            }
        } while (number <= 0);
        inputScanner.nextLine();
        return number;
    }
    /**
     * Метод для гарантии того, чтобы путь к каталогу содержал "/" в последнем символе.
     * К такому пути например можно в дальнейшем корректно добавить имя файла.
     *
     * @param path путь к каталогу
     * @return путь к каталогу, гарантированно заканчивающийся на "/"
     */
    private static String trailingSlash(String path) {
        return path.endsWith("/") ? path : path + "/";
    }

    /**
     * Метод main.
     * Вводятся с консоли параметры, в т.ч. :имя пустого каталога "path"; вероятность "probability" (целое число,
     * на самом деле вероятность будет рассчитана как "1/probability"); количество слов в словаре words "n4".
     * (в данном случае словарь words генерируется случайно, но его можно будет посмотреть в каталоге path,
     * в файле dictionary.txt)
     * После ввода параметров генерируется массив words, записывается файл path\dictionary.txt с элементами
     * массива words, запускается метод getFiles(String path, int n, int size, String[] words, int probability)
     *
     * @see Main
     * @see Generator
     *
     */
    public static void main(String[] args) {

        Generator generator = new Generator();

        Scanner inputScanner = new Scanner(System.in);
        String path;
        File outputFile, directoryCheck;
        int n4;
        BufferedWriter bufferedWriter=null;

        do {
            System.out.print("Введите имя каталога (пустого): ");
            path = inputScanner.nextLine();
            directoryCheck = new File(path);
        } while (!((directoryCheck.isDirectory()) && (directoryCheck.list().length == 0)));

        path = trailingSlash(path.trim());

        System.out.print("Введите вероятность (положительное число \"p\", собственно вероятность будет рассчитана как \"1/p\" ): ");
        int probability = scanPositiveInteger(inputScanner);
        System.out.print("Введите количество файлов: ");
        int repeats = scanPositiveInteger(inputScanner);
        System.out.print("Введите размер файлов: ");
        int size = scanPositiveInteger(inputScanner);
        System.out.print(LINESEPARATOR + "Будет случайно сгенерирован и показан в файле " + path + "dictionary.txt" + LINESEPARATOR
                + "массив слов для подстановки (words)." + LINESEPARATOR
                + "Введите количество слов в массиве words: " + LINESEPARATOR);
        do {
            n4 = scanPositiveInteger(inputScanner);
            if (n4 > 1000) System.out.println("Необходимо число меньше или равное 1000");
        } while (n4 > 1000);

        String[] words = new String[n4];

        outputFile = new File(path + "dictionary.txt");
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "Windows-1251"));
            for (int i = 0; i < n4; i++) {
                words[i] = generator.generateNamePart(15, true);
                bufferedWriter.write(words[i] + LINESEPARATOR);
            }
            bufferedWriter.close();
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
        generator.getFiles(path, repeats, size, words, probability);
    }
}
