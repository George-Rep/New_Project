package part1.lesson11.task01;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * класс Animal описывает домашнее животное.
 * Поля: уникальный идентификационный номер, кличка, хозяин (объект класс Person ), вес.
 *
 * @see Person
 */
public class Animal implements Comparable {
    private int id;
    private String name;
    private Person owner;
    private double weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Animal(int id, String name, Person owner, double weight) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.weight = weight;
    }

    /**
     * метод для печати полей класса, с "названиями".
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        Map<String, String> lHashMap = new LinkedHashMap<>();
        lHashMap.put("ID", Integer.toString(id));
        lHashMap.put("кличка", name);
        lHashMap.put("имя хозяина", owner.getName());
        lHashMap.put("возраст хозяина", Integer.toString(owner.getAge()));
        lHashMap.put("пол хозяина", ((owner.getSex() == Person.Sex.MAN) ? "M" : "Ж"));
        lHashMap.put("вес животного", df.format(weight));
        return lHashMap.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("; "));

    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Animal)) return false;
        return id == ((Animal) obj).getId();
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (this.getOwner() != ((Animal) o).getOwner())
            return this.getOwner().compareTo(((Animal) o).getOwner());
        if (this.getName().compareToIgnoreCase(((Animal) o).getName()) != 0)
            return this.getName().compareToIgnoreCase(((Animal) o).getName());
        if (this.getWeight() != ((Animal) o).getWeight())
            return Double.compare(this.getWeight(), ((Animal) o).getWeight());
        if (this.getId() != ((Animal) o).getId())
            return Integer.compare(this.getId(), ((Animal) o).getId());
        return 0;
    }
}
