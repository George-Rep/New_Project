package part1.lesson02.task03;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Scanner;

/**
 * Задача: дан массив объектов Person. Класс Person характеризуется полями age (возраст, целое число 0-100), sex
 * (пол – объект класса Sex со строковыми константами внутри MAN, WOMAN), name (имя - строка).
 * Создать два класса, методы которых будут реализовывать сортировку объектов.
 * Предусмотреть единый интерфейс для классов сортировки.
 * Реализовать два различных метода сортировки этого массива по правилам:
 * <p>
 * первые идут мужчины
 * выше в списке тот, кто более старший
 * имена сортируются по алфавиту
 * <p>
 * Программа должна вывести на экран отсортированный список и время работы каждого алгоритма сортировки.
 * Предусмотреть генерацию исходного массива (10000 элементов и более).
 * Если имена людей и возраст совпадают, выбрасывать в программе пользовательское исключение.
 * <p>
 * <p>
 * <p>
 * Класс Person характеризуется полями age (возраст, целое число 0-100),
 * sex (пол – объект класса Sex , name (имя - строка). Сделан геттер
 * свойства sex из указанного выше объекта класса Sex.
 * @see Main
 *
 */
class Person {
    private int age;
    private Sex sex;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex.getSex();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Person(int age, Sex sex, String name) {
        this.age = age;
        this.sex = sex;
        this.name = name;
    }
}

/**
 * класс Sex со строковыми константами внутри MAN, WOMAN
 */
class Sex {
    public static final String MAN = "MAN";
    public static final String WOMAN = "WOMAN";
    //enum sex {MAN,WOMAN};
    private String sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    Sex(String sex) {
        this.sex = sex;
    }
}

/**
 * класс MathUtils
 * содержит вспомогательные функции: 1)генерация одного слова из ФИО (например фамилии)
 * 2) сравнение двух объектов Person
 * 3) swap (смена местами) двух объектов Person
 *
 */
class MathUtils {
    /**
     * generateNamePart генерирует одно слово из ФИО (например фамилию).
     * Для данной задачи будем считать, что ФИО будет состоять из двух слов
     * (хотя одного достаточно) латинскими буквами, первая буква слов заглавная, слова случайной длины от 2 символов
     * до параметра maxLength в generateNamePart. Для формирования слова используется StringBuilder.append.
     */
    static String generateNamePart(int maxLength) {
        SecureRandom secureRandom = new SecureRandom();
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int strlength = 2 + secureRandom.nextInt(maxLength - 1);
        StringBuilder stringBuilder = new StringBuilder(strlength);
        int charIndex = secureRandom.nextInt(26);
        stringBuilder.append(letters.charAt(charIndex + 26));
        for (int i = 1; i < strlength; i++) {
            charIndex = secureRandom.nextInt(26);
            stringBuilder.append(letters.charAt(charIndex));
        }
        return stringBuilder.toString();
    }

    /**
     * сравнение двух объектов Person
     * возвращает значение больше нуля, если person1>person2; меньше нуля, если person1<person2,
     * ноль при равенстве. Под person1>person2 в данной задаче понимается в т.ч., что
     * по возрасту "старше < моложе" (см. условия задачи, "выше в списке тот, кто более старший").
     */
    static int comparePersons(Person person1, Person person2) {
        if (person1.getSex().compareTo(person2.getSex()) != 0)
            return person1.getSex().compareTo(person2.getSex());
        if (person1.getAge() != person2.getAge())
            return person2.getAge() - person1.getAge();
        try {
            if (person1.getName().compareTo(person2.getName()) == 0)
                throw new Exception("Exception: same");
        } catch (Exception e) {
            System.out.println("Exception при основной сортировке" + System.getProperty("line.separator")
                    + person1.getSex() + " " + person1.getAge() + " " + person1.getName()
                    + System.getProperty("line.separator"));
        }
        return person1.getName().compareTo(person2.getName());

    }

    /**
     * метод swap (смена местами) двух объектов Person, находящихся в массиве
     * personArray , номера элементов - arrayIndex1 и arrayIndex2
     *
     */
    static void Swap(Person[] personArray, int arrayIndex1, int arrayIndex2) {
        Person temp;
        temp = personArray[arrayIndex1];
        personArray[arrayIndex1] = personArray[arrayIndex2];
        personArray[arrayIndex2] = temp;
    }

}

/**
 * интерфейс Sortingalgo, декларирует метод sort для классов с алгоритмами сортировки
 * принимает массив для сортировки, отдает время работы в миллисекундах.
 */
interface Sortingalgo {
    long sort(Person[] arrayForSorting);
}

/**
 * класс сортировки пузырьком
 */
class BubbleSort implements Sortingalgo {
    /**
     * метод сортирует массив arrayForSorting
     */
    public long sort(Person[] arrayForSorting) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < arrayForSorting.length - 1; i++)
            for (int j = 0; j < arrayForSorting.length - i - 1; j++)
                if (MathUtils.comparePersons(arrayForSorting[j], arrayForSorting[j + 1]) > 0) {
                    MathUtils.Swap(arrayForSorting, j, j + 1);
                }
        return new Timestamp(System.currentTimeMillis()).getTime() - timestamp.getTime();
    }
}

/**
 * класс сортировки - вариант quicksort
 */
class QSort implements Sortingalgo {
    /**
     * partition, метод разбиения quicksort ("элементы меньше опорного помещаются перед ним, а больше или равные после")
     */
    int partition(Person[] arrayPart, int lowIndex, int highIndex) {
        Person pivot = arrayPart[highIndex];
        int i = (lowIndex - 1);
        for (int j = lowIndex; j < highIndex; j++) {
            if (!(MathUtils.comparePersons(arrayPart[j], pivot) > 0)) {
                i++;
                MathUtils.Swap(arrayPart, i, j);
            }
        }
        MathUtils.Swap(arrayPart, i + 1, highIndex);
        return i + 1;
    }

    /**
     * сортировка части массива от lowIndex до highIndex
     */
    private void arrayPartSort(Person[] arrayPart, int lowIndex, int highIndex) {
        if (lowIndex < highIndex) {
            int partitioningIndex = partition(arrayPart, lowIndex, highIndex);
            arrayPartSort(arrayPart, lowIndex, partitioningIndex - 1);
            arrayPartSort(arrayPart, partitioningIndex + 1, highIndex);
        }
    }

    /**
     * основной метод класса, сортирует quicksort'ом массив arrayForSorting
     */
    public long sort(Person[] arrayForSorting) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        arrayPartSort(arrayForSorting, 0, arrayForSorting.length - 1);
        return new Timestamp(System.currentTimeMillis()).getTime() - timestamp.getTime();
    }
}

/**
 * класс сортировки QSort2 (вариант quicksort)
 * сортировка ведется только по именам и возрасту, для корректной реализации условия:
 * "Если имена людей и возраст совпадают, выбрасывать в программе пользовательское исключение"
 */
class QSort2 extends QSort {
    /**
     * Аналогично MathUtils.comparePersons , но сортировка ведется только по именам и возрасту/
     * <p>
     * Сравнение двух объектов Person
     * возвращает значение больше нуля, если person1>person2; меньше нуля, если person1<person2,
     * равно нулю при равенстве. Под person1>person2 в данной задаче понимается в т.ч., что
     * по возрасту "старше < моложе" (см. условия задачи).
     */
    private int comparePersons2(Person person1, Person person2) {
//        if (person1.getSex().compareTo(person2.getSex()) != 0)
//            return person1.getSex().compareTo(person2.getSex());
        if (person1.getAge() != person2.getAge())
            return person2.getAge() - person1.getAge();
        try {
            if (person1.getName().compareTo(person2.getName()) == 0)
                throw new Exception("Exception: same");
        } catch (Exception e) {
            System.out.println("Exception при специальной сортировке" + System.getProperty("line.separator")
                    + person1.getSex() + " " + person1.getAge() + " "
                    + person1.getName() + System.getProperty("line.separator") +
                    person2.getSex() + " " + person2.getAge() + " " + person2.getName()
                    + System.getProperty("line.separator"));

        }
        return person1.getName().compareTo(person2.getName());

    }

    /**
     * partition, метод разбиения quicksort ("элементы меньше опорного помещаются перед ним, а больше или равные после").
     * Отличается от QSort.partition используемой функцией сравнения (здесь - comparePersons2)
     */
    int partition(Person[] arrayPart, int lowIndex, int highIndex) {
        Person pivot = arrayPart[highIndex];
        int i = (lowIndex - 1);
        for (int j = lowIndex; j < highIndex; j++) {
            if (!(comparePersons2(arrayPart[j], pivot) > 0)) {
                i++;
                MathUtils.Swap(arrayPart, i, j);
            }
        }
        MathUtils.Swap(arrayPart, i + 1, highIndex);
        return i + 1;
    }
}

/**
 * класс с методом main
 */
public class Main {
    /**
     * генерируется массив объектов Person.
     * <p>
     * сортировка приоисходит три раза, каждый раз на вход подается копия одного и того же
     * изначально сгенерированного массива. QSort2.sort производится для поиска совпадений и
     * корректного выполнения условия "Если имена людей и возраст совпадают, выбрасывать в программе
     * пользовательское исключение.".
     * Далее запускаются алгоритмы сортировки пузырьком и Quicksort,
     * выводится время потраченное на сортировку каждым алгоритмом.
     *
     */
    public static void main(String[] args) {
        int N;
        int maxNameWordLength = 12;

        Scanner inputScanner = new Scanner(System.in);
        do {
            System.out.println("Enter N (<50000):");
            while (!inputScanner.hasNextInt()) {
                System.out.println("введите положительное число меньше 50000");
                inputScanner.next();
            }
            N = inputScanner.nextInt();
        } while ((N <= 0) || (N > 50000));

        Person[] personArray1 = new Person[N];
        Person[] personArray2 = new Person[N];
        Person[] personArray3 = new Person[N];
        int age;
        Sex sex;
        String name;
        long time1, time2;

        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < N; ++i) {
            age = secureRandom.nextInt(101);
            sex = (secureRandom.nextInt(2) == 1) ? new Sex(Sex.WOMAN) : new Sex(Sex.MAN);
            name = MathUtils.generateNamePart(maxNameWordLength) + " " + MathUtils.generateNamePart(maxNameWordLength);
            personArray1[i] = new Person(age, sex, name);
            personArray2[i] = new Person(age, sex, name);
            personArray3[i] = new Person(age, sex, name);
        }
        // для проверки работы исключений при совпадении
/*        personArray1[10].setName(personArray1[0].getName());
        personArray1[10].setAge(personArray1[0].getAge());
        personArray2[10].setName(personArray2[0].getName());
        personArray2[10].setAge(personArray2[0].getAge());
        personArray3[10].setName(personArray3[0].getName());
        personArray3[10].setAge(personArray3[0].getAge());*/
        new QSort2().sort(personArray1);
        time1 = new BubbleSort().sort(personArray2);
        time2 = new QSort().sort(personArray3);
        for (int i = 0; i < N; ++i)
            System.out.println(personArray3[i].getSex() + " " + personArray3[i].getAge()
                    + " " + personArray3[i].getName());
        System.out.println(System.getProperty("line.separator") + "Bubblesort: " + time1 / 1000.0 + "с"
                + System.getProperty("line.separator") + "Quicksort: " + time2 / 1000.0 + "с");

    }
}
