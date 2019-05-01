package part1.lesson05.task01;

import java.security.SecureRandom;
import java.util.*;

/**
 * вспомогательный класс с функциями генерации строк и
 * работы с HashMap<String, ArrayList<Animal>>
 *
 */
public class MathUtils {
    /**
     * generateNamePart генерирует одно слово.
     * Латинскими буквами, первая буква слов заглавная, слова случайной длины от 2 символов
     * до значения параметра maxLength.
     * @param maxLength максимальная длина генерируемого слова
     * @return сгененрированное слово
     */
    String generateNamePart(int maxLength) {
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
     * значения HashMap являются ArrayList. данная функция создает элемент в HashMap, если элемента по
     * ключу mapKey нет. Если элемент есть, то ArrayList дополняется (newAnimal).
     * В данном случае в ArrayList на момент выполнения метода не может быть элемента равного newAnimal,
     * поэтому проверки на содержание newAnimal в ArrayList нет.
     *
     *
     * @param nameMap HashMap<String, ArrayList<Animal>> коллекция для вставки нового объекта
     * @param mapKey кличка животного / ключ для коллекции
     * @param newAnimal объект Animal, который нужно добавить в ArrayList (т.е. в Value HashMap )
     *
     */
    public void addToList(Map<String, ArrayList<Animal>> nameMap, String mapKey, Animal newAnimal) {
        ArrayList<Animal> itemsList = nameMap.get(mapKey);

        if (itemsList == null) {
            itemsList = new ArrayList<Animal>();
            itemsList.add(newAnimal);
            nameMap.put(mapKey, itemsList);
        } else {
            itemsList.add(newAnimal);
        }
    }
    /**
     * значения HashMap являются ArrayList. данная функция удаляет объект класса Animal по id из ArrayList
     * (ArrayList находится по ключу mapKey).
     * Если это был единственный элемент в ArrayList, то удаляется и элемент HashMap.
     *
     * В HashMap для ключа mapKey гарантированно есть в ArrayList один объект Animal с
     * полем id равным полученному в параметре id.
     *
     * @param nameMap HashMap<String, ArrayList<Animal>> коллекция для удаления объекта
     * @param mapKey кличка животного / ключ для коллекции
     * @param id идентификационный номер животного, для поиска объекта Animal в Value HashMap (в ArrayList)
     *
     */
    public void removeFromList(Map<String, ArrayList<Animal>> nameMap, String mapKey, int id) {
        ArrayList<Animal> itemsList = nameMap.get(mapKey);
        Iterator<Animal> i = itemsList.iterator();
        while (i.hasNext()) {
            Animal o = i.next();
            if (o.getId() == id) {
                i.remove();
                break;
            }
        }
        if (itemsList.size() == 0) {
            nameMap.remove(mapKey);
        }
    }
}
