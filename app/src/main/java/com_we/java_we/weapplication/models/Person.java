package com_we.java_we.weapplication.models;

public class Person {

    private String name, age, description;
    private int image;

    public Person(String name, String age, String description, int image) {
        this.name = name;
        this.age = age;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

}
