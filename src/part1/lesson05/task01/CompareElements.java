package part1.lesson05.task01;

import java.util.Comparator;
import java.util.Map;

/**
 * Класс "implements Comparator", кастомный компаратор для использования
 * в TreeMap, для сортировки не по ключам, а по значениям. Для этого
 * используется дополнительная HashMap, которая передается в конструктор.
 *
 */
class CompareElements implements Comparator<Integer> {
    private Map<Integer, Animal> map;
    /**
     * конструктор
     * @param map HashMap для уточнения объектов сравнения (см. compare)
     *
     */
    public CompareElements(Map<Integer, Animal> map) {
        this.map = map;
    }
    /**
     * вспомогательная функция для сравнения Animal.Person
     * @param person1
     * @param person2 объекты класса Person для сравнения
     * @return см. compare
     *
     */
    int comparePersons(Person person1, Person person2) {
        if (person1 == person2) return 0;
        if (person1.getName().compareTo(person2.getName()) != 0)
            return person1.getName().compareTo(person2.getName());
        if (person1.getAge() != person2.getAge())
            return person1.getAge() - person2.getAge();
        if (person1.getSex().compareTo(person2.getSex()) != 0)
            return person1.getSex().compareTo(person2.getSex());
        return 0;
    }
    /**
     * функция для сравнения Animal
     * @param animal1
     * @param animal2 объекты класса Animal для сравнения
     * @return см. compare
     *
     */
    int compareAnimals(Animal animal1, Animal animal2) {
        if (animal1 == animal2) return 0;
        if (comparePersons(animal1.getOwner(), animal2.getOwner()) != 0)
            return comparePersons(animal1.getOwner(), animal2.getOwner());
        if (animal1.getName().compareToIgnoreCase(animal2.getName()) != 0)
            return animal1.getName().compareToIgnoreCase(animal2.getName());
        if (animal1.getWeight() > animal2.getWeight())
            return (int) Math.ceil(animal1.getWeight() - animal2.getWeight());
        if (animal1.getWeight() < animal2.getWeight())
            return (int) Math.floor(animal1.getWeight() - animal2.getWeight());
        if (animal1.getId() < animal2.getId())
            return animal1.getId() - animal2.getId();
        return 0;
    }
    /**
     * вместо идентификационных номеров сравниваются 2 объекта Animal, соответствующие
     * данным идентификационным номерам. Для этого объекты Animal ищутся в HashMap map.
     * @param id1
     * @param id2 идентификационные номера для сравнения
     * @return . Сравниваются два объекта Animal, соответствующие id1 и id2. Возвращается
     * 0, если объекты равны; значение меньше нуля, если объект с id1 "меньше" чем объект с id2;
     * значение больше нуля, если объект с id1 "больше" чем объект с id2.
     *
     */
    @Override
    public int compare(Integer id1, Integer id2) {
        return compareAnimals(map.get(id1), map.get(id2));
    }
}

