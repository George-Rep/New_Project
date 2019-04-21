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
 * класс ObjectWrapper является Wrapper'ом для класса Person,
 * сделан для того, чтобы в дальнейшем менять местами (swap) объекты Person.
 */
class ObjectWrapper {
    Person person;

    ObjectWrapper(Person person) {
        this.person = person;
    }
}

/**
 * класс MathUtils
 * содержит вспомогательные функции: 1)генерация одного слова из ФИО (например фамилии)
 * 2) сравнение двух объектов Person (ObjectWrapper.person)
 * 3) swap (смена местами) двух объектов Person (ObjectWrapper.person)
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
     * сравнение двух объектов Person, в обертке (ObjectWrapper.person)
     * возвращает значение больше нуля, если person1>person2; меньше нуля, если person1<person2,
     * ноль при равенстве. Под person1>person2 в данной задаче понимается в т.ч., что
     * по возрасту "старше < моложе" (см. условия задачи, "выше в списке тот, кто более старший").
     */
    static int comparePersons(ObjectWrapper person1, ObjectWrapper person2) {
        if (person1.person.getSex().compareTo(person2.person.getSex()) != 0)
            return person1.person.getSex().compareTo(person2.person.getSex());
        if (person1.person.getAge() != person2.person.getAge())
            return person2.person.getAge() - person1.person.getAge();
        try {
            if (person1.person.getName().compareTo(person2.person.getName()) == 0)
                throw new Exception("Exception: same");
        } catch (Exception e) {
            System.out.println("Exception при основной сортировке" + System.getProperty("line.separator")
                    + person1.person.getSex() + " " + person1.person.getAge() + " " + person1.person.getName()
                    + System.getProperty("line.separator"));
        }
        return person1.person.getName().compareTo(person2.person.getName());

    }

    /**
     * метод swap (смена местами) двух объектов Person в обертках (ObjectWrapper.person)
     */
    static void Swap(ObjectWrapper person1, ObjectWrapper person2) {
        Person temp;
        temp = person1.person;
        person1.person = person2.person;
        person2.person = temp;
    }

}

/**
 * интерфейс Sortingalgo, декларирует метод sort для классов с алгоритмами сортировки
 * принимает массив для сортировки, отдает время работы в миллисекундах.
 */
interface Sortingalgo {
    long sort(ObjectWrapper[] arrayForSorting);
}

/**
 * класс сортировки пузырьком
 */
class BubbleSort implements Sortingalgo {
    /**
     * метод сортирует массив arrayForSorting
     */
    public long sort(ObjectWrapper[] arrayForSorting) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < arrayForSorting.length - 1; i++)
            for (int j = 0; j < arrayForSorting.length - i - 1; j++)
                if (MathUtils.comparePersons(arrayForSorting[j], arrayForSorting[j + 1]) > 0) {
                    MathUtils.Swap(arrayForSorting[j], arrayForSorting[j + 1]);

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
    int partition(ObjectWrapper[] arrayPart, int lowIndex, int highIndex) {
        ObjectWrapper pivot = arrayPart[highIndex];
        int i = (lowIndex - 1);
        for (int j = lowIndex; j < highIndex; j++) {
            if (!(MathUtils.comparePersons(arrayPart[j], pivot) > 0)) {
                i++;
                MathUtils.Swap(arrayPart[i], arrayPart[j]);
            }
        }
        MathUtils.Swap(arrayPart[i + 1], arrayPart[highIndex]);
        return i + 1;
    }

    /**
     * сортировка части массива от lowIndex до highIndex
     */
    private void arrayPartSort(ObjectWrapper[] arrayPart, int lowIndex, int highIndex) {
        if (lowIndex < highIndex) {
            int partitioningIndex = partition(arrayPart, lowIndex, highIndex);
            arrayPartSort(arrayPart, lowIndex, partitioningIndex - 1);
            arrayPartSort(arrayPart, partitioningIndex + 1, highIndex);
        }
    }

    /**
     * основной метод класса, сортирует quicksort'ом массив arrayForSorting
     */
    public long sort(ObjectWrapper[] arrayForSorting) {
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
     * Сравнение двух объектов Person, в обертке (ObjectWrapper.person)
     * возвращает значение больше нуля, если person1>person2; меньше нуля, если person1<person2,
     * равно нулю при равенстве. Под person1>person2 в данной задаче понимается в т.ч., что
     * по возрасту "старше < моложе" (см. условия задачи).
     */
private    int comparePersons2(ObjectWrapper person1, ObjectWrapper person2) {
//        if (person1.person.getSex().compareTo(person2.person.getSex()) != 0)
//            return person1.person.getSex().compareTo(person2.person.getSex());
        if (person1.person.getAge() != person2.person.getAge())
            return person2.person.getAge() - person1.person.getAge();
        try {
            if (person1.person.getName().compareTo(person2.person.getName()) == 0)
                throw new Exception("Exception: same");
        } catch (Exception e) {
            System.out.println("Exception при специальной сортировке" + System.getProperty("line.separator")
                    + person1.person.getSex() + " " + person1.person.getAge() + " "
                    + person1.person.getName() + System.getProperty("line.separator") +
                    person2.person.getSex() + " " + person2.person.getAge() + " " + person2.person.getName()
                    + System.getProperty("line.separator"));

        }
        return person1.person.getName().compareTo(person2.person.getName());

    }

    /**
     * partition, метод разбиения quicksort ("элементы меньше опорного помещаются перед ним, а больше или равные после").
     * Отличается от QSort.partition используемой функцией сравнения (здесь - comparePersons2)
     */
    int partition(ObjectWrapper[] arrayPart, int lowIndex, int highIndex) {
        ObjectWrapper pivot = arrayPart[highIndex];
        int i = (lowIndex - 1);
        for (int j = lowIndex; j < highIndex; j++) {
            if (!(comparePersons2(arrayPart[j], pivot) > 0)) {
                i++;
                MathUtils.Swap(arrayPart[i], arrayPart[j]);
            }
        }
        MathUtils.Swap(arrayPart[i + 1], arrayPart[highIndex]);
        return i + 1;
    }
}

/**
 * класс с методом main
 */
public class Main {
    /**
     * генерируется массив объектов Person. Но, для задач смены элементов местами при сортировке он
     * неудобен, поэтому на его основе делается соответсвующий массив объектов ObjectWrapper(.person)
     * и сортировка ведется в последнем.
     * <p>
     * сортировка приоисходит три раза, каждый раз на вход подается копия одного и того же
     * изначально сгенерированного массива. QSort2.sort производится для поиска совпадений и
     * корректного выполнения условия "Если имена людей и возраст совпадают, выбрасывать в программе
     * пользовательское исключение.".
     * Далее запускаются алгоритм сортировки пузырьком и Quicksort,
     * выводится время потраченное на сортировку каждым алгоритмом.
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

        Person[] personArray = new Person[N];
        ObjectWrapper[] personWrapperArray1 = new ObjectWrapper[N];
        ObjectWrapper[] personWrapperArray2 = new ObjectWrapper[N];
        ObjectWrapper[] personWrapperArray3 = new ObjectWrapper[N];
        int age;
        Sex sex;
        String name;
        long time1, time2;

        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < N; ++i) {
            age = secureRandom.nextInt(101);
/*            if (secureRandom.nextInt(2) == 1)
                sex = new Sex(Sex.WOMAN);
            else sex = new Sex(Sex.MAN);*/
            sex = (secureRandom.nextInt(2) == 1) ? new Sex(Sex.WOMAN) : new Sex(Sex.MAN);
            name = MathUtils.generateNamePart(maxNameWordLength) + " " + MathUtils.generateNamePart(maxNameWordLength);
            personArray[i] = new Person(age, sex, name);
            personWrapperArray1[i] = new ObjectWrapper(personArray[i]);
            personWrapperArray2[i] = new ObjectWrapper(personArray[i]);
            personWrapperArray3[i] = new ObjectWrapper(personArray[i]);
        }

        // для проверки работы исключений при совпадении
  /*      personWrapperArray1[10].person.setName(personWrapperArray1[0].person.getName());
        personWrapperArray1[10].person.setAge(personWrapperArray1[0].person.getAge());
        personWrapperArray2[10].person.setName(personWrapperArray2[0].person.getName());
        personWrapperArray2[10].person.setAge(personWrapperArray2[0].person.getAge());
        personWrapperArray3[10].person.setName(personWrapperArray3[0].person.getName());
        personWrapperArray3[10].person.setAge(personWrapperArray3[0].person.getAge());*/
        new QSort2().sort(personWrapperArray1);
        time1 = new BubbleSort().sort(personWrapperArray2);
        time2 = new QSort().sort(personWrapperArray3);
        for (int i = 0; i < N; ++i)
            System.out.println(personWrapperArray3[i].person.getSex() + " " + personWrapperArray3[i].person.getAge()
                    + " " + personWrapperArray3[i].person.getName());
        System.out.println(System.getProperty("line.separator") + "Bubblesort: " + time1 / 1000.0 + "с"
                + System.getProperty("line.separator") + "Quicksort: " + time2 / 1000.0 + "с");

    }
}
