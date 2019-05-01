package part1.lesson05.task01;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

/**
 * Картотека домашних животных. У каждого животного есть уникальный идентификационный номер, кличка, хозяин (объект класс Person с полями – имя, возраст, пол), вес.
 * <p>
 * Реализовать:
 * <p>
 * метод добавления животного в общий список (учесть, что добавление дубликатов должно приводить к исключительной ситуации)
 * поиск животного по его кличке (поиск должен быть эффективным)
 * изменение данных животного по его идентификатору
 * вывод на экран списка животных в отсортированном порядке. Поля для сортировки –  хозяин, кличка животного, вес.
 */
public class Main {
    static final String LINESEPARATOR = System.getProperty("line.separator");
    static final String DASHES = "-----------" + LINESEPARATOR;

    /**
     * метод получения объекта Person (владелец животного).
     * Предлагается либо создать новый объект по данным из консоли, либо
     * использовать существующий объект Person (на данный момент заимствуется у объекта Animal с указанным id )
     *
     * @param inputScanner Scanner , чтобы не создавать новый.
     * @param idMap        HashMap для проверки того, что объект с указанным id существует.
     * @return объект Person, сформированный, либо ранее существовавший.
     */
    static Person getOwner(Scanner inputScanner, Map<Integer, Animal> idMap) {
        String input;
        int age = 0;
        Person.Sex sex = Person.Sex.WOMAN;
        String ownerName = "";
        Person person = null;
        Animal animal;
        System.out.println("1.с добавлением нового хозяина" + LINESEPARATOR + "2.хозяин зарегистрирован" + LINESEPARATOR
                + "3. назад" + LINESEPARATOR + "Выберите действие : ");
        input = inputScanner.nextLine();
        if ("3".equals(input)) {
            System.out.println(DASHES);

        } else {
            switch (Integer.parseInt(input)) {
                case 1:
                    System.out.print("имя хозяина:");
                    ownerName = inputScanner.nextLine();
                    System.out.print("возраст хозяина:");
                    age = scanPositiveInteger(inputScanner);
                    while (true) {
                        System.out.print("пол хозяина (Ж/М):");
                        input = inputScanner.nextLine();
                        if (input.equals("Ж")) {
                            sex = Person.Sex.WOMAN;
                            break;
                        } else if (input.equals("М")) {
                            sex = Person.Sex.MAN;
                            break;
                        }
                    }
                    person = new Person(age, sex, ownerName);

                    System.out.println(DASHES);
                    break;
                case 2:
                    do {
                        System.out.print("Введите ID животного, у которого тот же хозяин:");
                        animal = idMap.get(scanPositiveInteger(inputScanner));
                    } while (animal == null);
                    person = animal.getOwner();
                    System.out.println(DASHES);
                    break;
            }
        }
        return person;
    }

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
                System.out.println("положительное целое число:");
                inputScanner.next();
            }
            number = inputScanner.nextInt();
        } while (number <= 0);
        inputScanner.nextLine();
        return number;
    }

    /**
     * метод получения положительного числа (double) с консоли
     *
     * @param inputScanner Scanner
     * @return double, положительное число из консоли
     */
    static double scanPositiveDouble(Scanner inputScanner) {
        double number;
        do {
            while (!inputScanner.hasNextDouble()) {
                System.out.println("положительное целое число:");
                inputScanner.next();
            }
            number = inputScanner.nextDouble();
        } while (number <= 0);
        inputScanner.nextLine();
        return number;
    }

    /**
     * генерируется массив объектов Animal. Для уникальности идентификационных номеров данные номера вставляются в
     * HashSet и затем используются номера из HashSet. Дальше используется меню с циклами.
     * Доступны операции "1.добавление , 2.поиск, 3.изменение,  4.список". При добавлении и изменении при вводе
     * данных хозяина животного возможно отказаться от операции; ввести новые данные хозяина, либо использовать
     * существующие данные. Помимо HashSet используется еще три коллекции. Map<String, ArrayList<Animal>> nameMap
     * позволяет хранить списки животных по ключу "кличка". HashMap idMap - для быстрого поиска животных по ключу id.
     * TreeMap<Integer, Animal> idSortMap - для сортировки данных. Сортировка идет по значениям TreeMap idSortMap, с помощью
     * HashMap idMap.
     * <p>
     * 1) При добавлении происходит вставка в 3 коллекции. Генерируется исключение, если такой id уже существует.
     * 2) При поиске по кличке идет поиск в HashMap nameMap.
     * 3) При изменении данных по id возможно задать и новый id. Если меняется кличка животного, то в nameMap производится
     * удаление из [списка животных] со старой кличкой и вставка в [список животных] с новой кличкой. Происходит
     * удаление элемента и вставка с новыми данными в TreeMap idSortMap для проведения сортировки.
     * Если меняется id, то происходит удаление
     * и вставка в idMap.
     * 4) для получения отсортированного списка печатается содержимое TreeMap idSortMap.
     * <p>
     * <p>
     * параметр maxNameWordLength - максимальная длина слова при начальной генерации данных.
     */
    public static void main(String[] args) {
        int N;
        int maxNameWordLength = 12;

        int age = 0;
        Person.Sex sex = Person.Sex.WOMAN;
        String animalName = "", ownerName = "";
        int id = 0;
        double weight = 0.0;
        Animal animal = null, prevAnimal = null;
        ArrayList<Animal> nameList;
        HashSet<Integer> hashSet = new HashSet<Integer>();
        Map<String, ArrayList<Animal>> nameMap = new HashMap<>();
        Map<Integer, Animal> idMap = new HashMap<>();
        TreeMap<Integer, Animal> idSortMap = new TreeMap<>(new CompareElements(idMap));
        Person person = null;
        String input;
        MathUtils mathUtils = new MathUtils();
        SecureRandom secureRandom = new SecureRandom();
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Введите количество элементов (случайная начальная генерация): ");
        N = scanPositiveInteger(inputScanner);
        System.out.println(DASHES);
        for (int i = 0; i < N; ++i) {
            hashSet.add(secureRandom.nextInt(Integer.MAX_VALUE));
        }
        int j = 0;
        while (hashSet.size() < N) {
            hashSet.add(secureRandom.nextInt(Integer.MAX_VALUE));
            j++;
        }

        for (int temp : hashSet) {
            age = secureRandom.nextInt(101);
            sex = (secureRandom.nextInt(2) == 1) ? Person.Sex.WOMAN : Person.Sex.MAN;
            ownerName = mathUtils.generateNamePart(maxNameWordLength) + " " + mathUtils.generateNamePart(maxNameWordLength);
            animalName = mathUtils.generateNamePart(maxNameWordLength);
            id = temp;
            weight = 0.01 + secureRandom.nextDouble() * 100;
            animal = new Animal(id, animalName, new Person(age, sex, ownerName), weight);
            mathUtils.addToList(nameMap, animalName, animal);
            idMap.put(id, animal);
            idSortMap.put(id, animal);
        }
        while (true) {
            System.out.println("1.добавление" + LINESEPARATOR + "2.поиск" + LINESEPARATOR + "3.изменение" + LINESEPARATOR
                    + "4.список" + LINESEPARATOR + "5.выход" + LINESEPARATOR + "Выберите действие : ");

            input = inputScanner.nextLine();
            System.out.println(DASHES);
            if ("5".equals(input)) {
                System.out.println("Exit!");
                break;
            }
            System.out.println("input : " + input + LINESEPARATOR + DASHES);
            switch (Integer.parseInt(input)) {
                case 1:
                    System.out.println("добавление:" + LINESEPARATOR + "ID животного:");
                    id = scanPositiveInteger(inputScanner);
                    System.out.print("кличка животного:");
                    animalName = inputScanner.nextLine();
                    System.out.print("вес животного:");
                    weight = scanPositiveDouble(inputScanner);
                    System.out.println(DASHES);
                    person = getOwner(inputScanner, idMap);
                    if (person != null) {
                        animal = new Animal(id, animalName, person, weight);
                        try {
                            if (idMap.get(id) != null) throw new IOException("уже существует");
                            mathUtils.addToList(nameMap, animalName, animal);
                            idMap.put(id, animal);
                            idSortMap.put(id, animal);
                            System.out.println("запись добавлена" + LINESEPARATOR + DASHES);
                        } catch (IOException e) {
                            System.out.println("животное с таким ID уже существует" + LINESEPARATOR + DASHES);
                        }
                    }
                    break;
                case 2:
                    System.out.print("Введите кличку животного:");
                    animalName = inputScanner.nextLine();
                    nameList = nameMap.get(animalName);
                    System.out.println(animalName);
                    if (nameList != null) {
                        for (Animal animal2 : nameList) {
                            System.out.println(animal2);
                        }
                    } else System.out.print("ничего не найдено :");
                    System.out.println(LINESEPARATOR);
                    break;

                case 3:
                    try {
                        do {
                            System.out.print("ID животного:");
                            id = scanPositiveInteger(inputScanner);
                            prevAnimal = idMap.get(id);
                        }
                        while (prevAnimal == null);
                        System.out.println(prevAnimal);
                        System.out.print("новый ID животного:");
                        id = scanPositiveInteger(inputScanner);
                        System.out.print("новая кличка животного:");
                        animalName = inputScanner.nextLine();
                        System.out.print("новый вес животного :");
                        weight = scanPositiveDouble(inputScanner);
                        System.out.println(DASHES);
                        person = getOwner(inputScanner, idMap);
                        if (person != null) {
                            animal = new Animal(id, animalName, person, weight);
                            if ((id == prevAnimal.getId()) && (animalName.equals(prevAnimal.getName())) && (weight == prevAnimal.getWeight())
                                    && (person == prevAnimal.getOwner())) {
                            } else {
                                if (id != prevAnimal.getId()) {
                                    if (idMap.get(id) == null) {

                                        if (!animal.getName().equals(prevAnimal.getName())) {
                                            mathUtils.removeFromList(nameMap, prevAnimal.getName(), prevAnimal.getId());
                                            mathUtils.addToList(nameMap, animal.getName(), prevAnimal);
                                        }
                                        idSortMap.remove(prevAnimal.getId());
                                        idMap.remove(prevAnimal.getId());
                                        prevAnimal.setId(id);
                                        prevAnimal.setName(animalName);
                                        prevAnimal.setOwner(person);
                                        prevAnimal.setWeight(weight);
                                        idMap.put(id, prevAnimal);
                                        idSortMap.put(id, prevAnimal);

                                        System.out.println("Данные изменены" + LINESEPARATOR + DASHES);
                                    } else {
                                        System.out.println("указанное новое значение ID уже существует" + LINESEPARATOR + DASHES);
                                    }
                                } else {

                                    if (!animal.getName().equals(prevAnimal.getName())) {
                                        mathUtils.removeFromList(nameMap, prevAnimal.getName(), prevAnimal.getId());
                                        mathUtils.addToList(nameMap, animal.getName(), prevAnimal);
                                    }

                                    idSortMap.remove(prevAnimal.getId());
                                    prevAnimal.setName(animalName);
                                    prevAnimal.setOwner(person);
                                    prevAnimal.setWeight(weight);
                                    idSortMap.put(id, prevAnimal);

                                    System.out.println("Данные изменены" + LINESEPARATOR + DASHES);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(DASHES + "некорректные значения" + LINESEPARATOR + DASHES);
                    }
                    System.out.println(LINESEPARATOR);
                    break;
                case 4:
                    for (Map.Entry<Integer, Animal> entry : idSortMap.entrySet()) {
                        System.out.println(entry.getValue());
                    }
                    System.out.println("Количество элементов: " + idSortMap.size() + LINESEPARATOR + DASHES);
                    break;
            }
        }
    }
}