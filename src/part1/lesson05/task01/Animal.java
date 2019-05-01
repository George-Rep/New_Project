package part1.lesson05.task01;

import java.text.DecimalFormat;
/**
 * класс Animal описывает домашнее животное.
 * Поля: уникальный идентификационный номер, кличка, хозяин (объект класс Person ), вес.
 * @see Person
 */
public class Animal {
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID: ");
        stringBuilder.append(id);
        stringBuilder.append(" ;кличка: ");
        stringBuilder.append(name);
        stringBuilder.append(" ;имя хозяина: ");
        stringBuilder.append(owner.getName());
        stringBuilder.append(" ;возраст хозяина: ");
        stringBuilder.append(owner.getAge());
        stringBuilder.append(" ;пол хозяина: ");
        stringBuilder.append(((owner.getSex() == Person.Sex.MAN) ? "M" : "Ж"));
        stringBuilder.append(" ;вес животного: ");
        stringBuilder.append(df.format(weight));
        return stringBuilder.toString();
    }
}
