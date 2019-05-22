package part1.lesson11.task01;

/**
 * Класс Person. В данном случае описывает хозяина животного.
 * Поля - имя, возраст, пол.
 */
class Person implements Comparable {
    enum Sex {MAN, WOMAN}

    private String name;
    private int age;
    private Sex sex;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
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

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (this.getName().compareTo(((Person) o).getName()) != 0)
            return this.getName().compareTo(((Person) o).getName());
        if (this.getAge() != ((Person) o).getAge())
            return Integer.compare(this.getAge(), ((Person) o).getAge());
        if (this.getSex().compareTo(((Person) o).getSex()) != 0)
            return this.getSex().compareTo(((Person) o).getSex());
        return 0;
    }
}





