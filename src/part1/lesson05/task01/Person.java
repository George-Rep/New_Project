package part1.lesson05.task01;
/**
 * Класс Person. В данном случае описывает хозяина животного.
 *  Поля - имя, возраст, пол.
 */
class Person {
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
}





